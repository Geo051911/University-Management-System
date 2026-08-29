package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mainpackage.DBConnection;

@WebServlet("/professor-enter-grades")
public class ProfessorEnterGradesServlet extends HttpServlet {

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

        int professorId =
                (Integer) session.getAttribute("userId");

        String courseCode =
                request.getParameter("courseCode");

        response.setContentType("text/html");

        PrintWriter out =
                response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Enter Grades</title>");
        out.println("<link rel='stylesheet' href='style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println("<div class='container'>");

        out.println("<h1>Enter Grades</h1>");

        /*
         * STEP 1:
         * No course selected yet.
         * Show courses assigned to this professor.
         */
        if (courseCode == null || courseCode.isBlank()) {

            String courseSql =
                    "SELECT course_code, course_name " +
                    "FROM courses " +
                    "WHERE professor_id = ? " +
                    "ORDER BY course_code";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(courseSql)
            ) {

                statement.setInt(1, professorId);

                ResultSet result =
                        statement.executeQuery();

                out.println(
                        "<form action='professor-enter-grades' method='get'>"
                );

                out.println("<label>Select Course</label>");

                out.println(
                        "<select name='courseCode' required>"
                );

                boolean foundCourse = false;

                while (result.next()) {

                    foundCourse = true;

                    String code =
                            result.getString("course_code");

                    String name =
                            result.getString("course_name");

                    out.println(
                            "<option value='" + code + "'>" +
                            code + " - " + name +
                            "</option>"
                    );
                }

                out.println("</select>");

                if (foundCourse) {

                    out.println(
                            "<button type='submit'>" +
                            "Continue" +
                            "</button>"
                    );

                } else {

                    out.println(
                            "<p>No courses assigned to you.</p>"
                    );
                }

                out.println("</form>");

            } catch (Exception e) {

                e.printStackTrace();

                out.println(
                        "<p class='error-message'>" +
                        "Error loading courses." +
                        "</p>"
                );
            }

        } else {

            /*
             * STEP 2:
             * Show enrolled students who do NOT already have a grade.
             */
            String studentsSql =
                    "SELECT " +
                    "students.user_id, " +
                    "students.registration_number, " +
                    "users.name, " +
                    "users.surname " +
                    "FROM courses " +
                    "JOIN course_students " +
                    "ON courses.course_id = course_students.course_id " +
                    "JOIN students " +
                    "ON course_students.student_id = students.user_id " +
                    "JOIN users " +
                    "ON students.user_id = users.user_id " +
                    "LEFT JOIN grades " +
                    "ON grades.student_id = students.user_id " +
                    "AND grades.course_id = courses.course_id " +
                    "WHERE courses.professor_id = ? " +
                    "AND courses.course_code = ? " +
                    "AND grades.grade_id IS NULL " +
                    "ORDER BY students.registration_number";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(studentsSql)
            ) {

                statement.setInt(1, professorId);
                statement.setString(2, courseCode);

                ResultSet result =
                        statement.executeQuery();

                out.println(
                        "<h2>" + courseCode + "</h2>"
                );

                boolean foundStudent = false;

                while (result.next()) {

                    foundStudent = true;

                    int studentId =
                            result.getInt("user_id");

                    int registrationNumber =
                            result.getInt("registration_number");

                    String name =
                            result.getString("name");

                    String surname =
                            result.getString("surname");

                    out.println("<form method='post' " +
                            "action='professor-enter-grades'>");

                    out.println(
                            "<p><strong>" +
                            registrationNumber + " - " +
                            name + " " + surname +
                            "</strong></p>"
                    );

                    out.println(
                            "<input type='hidden' " +
                            "name='studentId' " +
                            "value='" + studentId + "'>"
                    );

                    out.println(
                            "<input type='hidden' " +
                            "name='courseCode' " +
                            "value='" + courseCode + "'>"
                    );

                    out.println(
                            "<label>Grade</label>"
                    );

                    out.println(
                            "<input type='number' " +
                            "name='grade' " +
                            "step='0.01' required>"
                    );

                    out.println(
                            "<button type='submit'>" +
                            "Save Grade" +
                            "</button>"
                    );

                    out.println("</form>");
                }

                if (!foundStudent) {

                    out.println(
                            "<p>All enrolled students " +
                            "have already been graded.</p>"
                    );
                }

                out.println(
                        "<a href='professor-enter-grades'>" +
                        "Choose Another Course" +
                        "</a>"
                );

            } catch (Exception e) {

                e.printStackTrace();

                out.println(
                        "<p class='error-message'>" +
                        "Error loading students." +
                        "</p>"
                );
            }
        }

        out.println(
                "<a href='professorMenu.jsp'>" +
                "Back to Professor Menu" +
                "</a>"
        );

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }


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

        int professorId =
                (Integer) session.getAttribute("userId");

        try {

            int studentId =
                    Integer.parseInt(
                            request.getParameter("studentId")
                    );

            String courseCode =
                    request.getParameter("courseCode");

            BigDecimal grade =
                    new BigDecimal(
                            request.getParameter("grade")
                    );

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
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setInt(1, studentId);
                statement.setBigDecimal(2, grade);

                statement.setInt(3, studentId);
                statement.setString(4, courseCode);

                statement.setInt(5, professorId);
                statement.setInt(6, studentId);

                int rows =
                        statement.executeUpdate();

                if (rows > 0) {

                    response.sendRedirect(
                            "professor-enter-grades" +
                            "?courseCode=" + courseCode
                    );

                } else {

                    response.setContentType("text/html");

                    response.getWriter().println(
                            "<h2>Grade could not be saved.</h2>" +
                            "<p>The student may already have a grade " +
                            "or the course does not belong to you.</p>"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Error saving grade.</h2>"
            );
        }
    }
}