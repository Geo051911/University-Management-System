package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mainpackage.DBConnection;

@WebServlet(urlPatterns = {
        "/professor-grades",
        "/professor-enter-grades"
})
public class ProfessorServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"professor".equals(session.getAttribute("role"))) {

            response.sendRedirect("index.html");
            return;
        }

        setNoCache(response);

        int professorId = (Integer) session.getAttribute("userId");
        String courseCode = request.getParameter("courseCode");

        List<Map<String, Object>> courses = loadCourses(professorId);

        request.setAttribute("courses", courses);
        request.setAttribute("selectedCourse", courseCode);

        if ("/professor-enter-grades".equals(request.getServletPath())) {

            if (courseCode != null && !courseCode.isBlank()) {
                request.setAttribute(
                        "students",
                        loadUngradedStudents(
                                professorId,
                                courseCode.trim()
                        )
                );
            }

            request.getRequestDispatcher("/professorEnterGrades.jsp")
                    .forward(request, response);

        } else {

            if (courseCode != null && !courseCode.isBlank()) {
                request.setAttribute(
                        "grades",
                        loadCourseGrades(
                                professorId,
                                courseCode.trim()
                        )
                );
            }

            request.getRequestDispatcher("/professorGrades.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"professor".equals(session.getAttribute("role"))) {

            response.sendRedirect("index.html");
            return;
        }

        if (!"/professor-enter-grades".equals(request.getServletPath())) {
            response.sendRedirect("professorMenu.jsp");
            return;
        }

        int professorId = (Integer) session.getAttribute("userId");

        String courseCode = request.getParameter("courseCode");
        String studentIdText = request.getParameter("studentId");
        String gradeText = request.getParameter("grade");

        int studentId;
        BigDecimal grade;

        try {
            studentId = Integer.parseInt(studentIdText);
            grade = new BigDecimal(gradeText);
        } catch (Exception e) {
            redirectGrade(response, courseCode, "grade-invalid");
            return;
        }

        if (courseCode == null ||
                courseCode.isBlank() ||
                studentId < 1 ||
                grade.compareTo(BigDecimal.ZERO) < 0 ||
                grade.compareTo(BigDecimal.TEN) > 0) {

            redirectGrade(response, courseCode, "grade-invalid");
            return;
        }

        String sql =
                "INSERT INTO grades " +
                "(student_id, course_id, grade) " +
                "SELECT ?, courses.course_id, ? " +
                "FROM courses " +
                "JOIN course_students " +
                "ON courses.course_id = course_students.course_id " +
                "LEFT JOIN grades existing_grade " +
                "ON existing_grade.student_id = ? " +
                "AND existing_grade.course_id = courses.course_id " +
                "WHERE courses.course_code = ? " +
                "AND courses.professor_id = ? " +
                "AND course_students.student_id = ? " +
                "AND existing_grade.grade_id IS NULL";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, studentId);
            statement.setBigDecimal(2, grade);
            statement.setInt(3, studentId);
            statement.setString(4, courseCode.trim());
            statement.setInt(5, professorId);
            statement.setInt(6, studentId);

            int rows = statement.executeUpdate();

            redirectGrade(
                    response,
                    courseCode,
                    rows > 0 ? "grade-success" : "grade-error"
            );

        } catch (Exception e) {

            e.printStackTrace();
            redirectGrade(response, courseCode, "grade-error");
        }
    }

    private List<Map<String, Object>> loadCourses(int professorId) {

        List<Map<String, Object>> courses = new ArrayList<>();

        String sql =
                "SELECT course_code, course_name " +
                "FROM courses " +
                "WHERE professor_id = ? " +
                "ORDER BY course_code";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, professorId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Map<String, Object> row = new HashMap<>();
                    row.put("courseCode", result.getString("course_code"));
                    row.put("courseName", result.getString("course_name"));
                    courses.add(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }

    private List<Map<String, Object>> loadCourseGrades(
            int professorId,
            String courseCode) {

        List<Map<String, Object>> grades = new ArrayList<>();

        String sql =
                "SELECT students.registration_number, " +
                "users.name, users.surname, grades.grade " +
                "FROM courses " +
                "JOIN grades ON courses.course_id = grades.course_id " +
                "JOIN students ON grades.student_id = students.user_id " +
                "JOIN users ON students.user_id = users.user_id " +
                "WHERE courses.professor_id = ? " +
                "AND courses.course_code = ? " +
                "ORDER BY students.registration_number";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, professorId);
            statement.setString(2, courseCode);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Map<String, Object> row = new HashMap<>();
                    row.put(
                            "registrationNumber",
                            result.getInt("registration_number")
                    );
                    row.put("name", result.getString("name"));
                    row.put("surname", result.getString("surname"));
                    row.put("grade", result.getBigDecimal("grade"));
                    grades.add(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return grades;
    }

    private List<Map<String, Object>> loadUngradedStudents(
            int professorId,
            String courseCode) {

        List<Map<String, Object>> students = new ArrayList<>();

        String sql =
                "SELECT students.user_id, " +
                "students.registration_number, " +
                "users.name, users.surname " +
                "FROM courses " +
                "JOIN course_students " +
                "ON courses.course_id = course_students.course_id " +
                "JOIN students " +
                "ON course_students.student_id = students.user_id " +
                "JOIN users ON students.user_id = users.user_id " +
                "LEFT JOIN grades " +
                "ON grades.student_id = students.user_id " +
                "AND grades.course_id = courses.course_id " +
                "WHERE courses.professor_id = ? " +
                "AND courses.course_code = ? " +
                "AND grades.grade_id IS NULL " +
                "ORDER BY students.registration_number";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, professorId);
            statement.setString(2, courseCode);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Map<String, Object> row = new HashMap<>();
                    row.put("studentId", result.getInt("user_id"));
                    row.put(
                            "registrationNumber",
                            result.getInt("registration_number")
                    );
                    row.put("name", result.getString("name"));
                    row.put("surname", result.getString("surname"));
                    students.add(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }

    private void redirectGrade(
            HttpServletResponse response,
            String courseCode,
            String status)
            throws IOException {

        String encodedCourseCode =
                URLEncoder.encode(
                        courseCode == null ? "" : courseCode,
                        StandardCharsets.UTF_8
                );

        response.sendRedirect(
                response.encodeRedirectURL(
                        "professor-enter-grades" +
                        "?courseCode=" + encodedCourseCode +
                        "&status=" + status
                )
        );
    }

    private void setNoCache(HttpServletResponse response) {

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate"
        );
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
