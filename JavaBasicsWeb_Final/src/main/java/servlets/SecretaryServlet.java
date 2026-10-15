package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
import mainpackage.PasswordUtil;

@WebServlet(urlPatterns = {
        "/students",
        "/professors",
        "/courses",
        "/courses-professors",
        "/enrollments",
        "/add-student",
        "/add-professor",
        "/add-course",
        "/add-student-course",
        "/assign-professor"
})
public class SecretaryServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isSecretary(request, response)) {
            return;
        }

        setNoCache(response);

        String path = request.getServletPath();

        if ("/students".equals(path)) {
            request.setAttribute("view", "students");
            request.setAttribute("rows", loadStudents());

        } else if ("/professors".equals(path)) {
            request.setAttribute("view", "professors");
            request.setAttribute("rows", loadProfessors());

        } else if ("/courses".equals(path)) {
            request.setAttribute("view", "courses");
            request.setAttribute("rows", loadCourses());

        } else if ("/courses-professors".equals(path)) {
            request.setAttribute("view", "coursesProfessors");
            request.setAttribute("rows", loadCoursesProfessors());

        } else if ("/enrollments".equals(path)) {
            request.setAttribute("view", "enrollments");
            request.setAttribute("rows", loadEnrollments());

        } else {
            response.sendRedirect("secretaryMenu.jsp");
            return;
        }

        request.getRequestDispatcher("/secretaryData.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isSecretary(request, response)) {
            return;
        }

        String path = request.getServletPath();

        if ("/add-student".equals(path)) {
            addStudent(request, response);

        } else if ("/add-professor".equals(path)) {
            addProfessor(request, response);

        } else if ("/add-course".equals(path)) {
            addCourse(request, response);

        } else if ("/add-student-course".equals(path)) {
            addStudentToCourse(request, response);

        } else if ("/assign-professor".equals(path)) {
            assignProfessor(request, response);

        } else {
            response.sendRedirect("secretaryMenu.jsp");
        }
    }

    private void addStudent(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String department = request.getParameter("department");
        String registrationText =
                request.getParameter("registrationNumber");

        int registrationNumber;

        try {
            registrationNumber = Integer.parseInt(registrationText);
        } catch (Exception e) {
            redirect(response, "addStudent.html", "student-invalid");
            return;
        }

        if (registrationNumber < 1) {
            redirect(response, "addStudent.html", "student-invalid");
            return;
        }

        if (isBlank(username) ||
                isBlank(password) ||
                isBlank(name) ||
                isBlank(surname) ||
                isBlank(department)) {

            redirect(response, "addStudent.html", "student-error");
            return;
        }

        Connection connection = null;

        try {
            String salt = PasswordUtil.generateSalt();
            String hash = PasswordUtil.hashPassword(password, salt);

            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String userSql =
                    "INSERT INTO users " +
                    "(Username, name, surname, department, " +
                    "password_hash, password_salt) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            int userId;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    userSql,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                statement.setString(1, username.trim());
                statement.setString(2, name.trim());
                statement.setString(3, surname.trim());
                statement.setString(4, department.trim());
                statement.setString(5, hash);
                statement.setString(6, salt);
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new Exception("User ID was not generated.");
                    }
                    userId = keys.getInt(1);
                }
            }

            String studentSql =
                    "INSERT INTO students " +
                    "(user_id, registration_number) VALUES (?, ?)";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(studentSql)
            ) {
                statement.setInt(1, userId);
                statement.setInt(2, registrationNumber);
                statement.executeUpdate();
            }

            connection.commit();
            redirect(response, "addStudent.html", "student-success");

        } catch (Exception e) {

            e.printStackTrace();

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception ignored) {
                }
            }

            redirect(response, "addStudent.html", "student-error");

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void addProfessor(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String department = request.getParameter("department");
        String professorNumberText = professorNumberParameter(request);

        int professorNumber;

        try {
            professorNumber = Integer.parseInt(professorNumberText);
        } catch (Exception e) {
            redirect(response, "addProfessor.html", "professor-invalid");
            return;
        }

        if (professorNumber < 1) {
            redirect(response, "addProfessor.html", "professor-invalid");
            return;
        }

        if (isBlank(username) ||
                isBlank(password) ||
                isBlank(name) ||
                isBlank(surname) ||
                isBlank(department)) {

            redirect(response, "addProfessor.html", "professor-error");
            return;
        }

        Connection connection = null;

        try {
            String salt = PasswordUtil.generateSalt();
            String hash = PasswordUtil.hashPassword(password, salt);

            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String userSql =
                    "INSERT INTO users " +
                    "(Username, name, surname, department, " +
                    "password_hash, password_salt) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            int userId;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    userSql,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                statement.setString(1, username.trim());
                statement.setString(2, name.trim());
                statement.setString(3, surname.trim());
                statement.setString(4, department.trim());
                statement.setString(5, hash);
                statement.setString(6, salt);
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new Exception("User ID was not generated.");
                    }
                    userId = keys.getInt(1);
                }
            }

            String professorSql =
                    "INSERT INTO professors " +
                    "(user_id, professor_number) VALUES (?, ?)";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(professorSql)
            ) {
                statement.setInt(1, userId);
                statement.setInt(2, professorNumber);
                statement.executeUpdate();
            }

            connection.commit();
            redirect(response, "addProfessor.html", "professor-success");

        } catch (Exception e) {

            e.printStackTrace();

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception ignored) {
                }
            }

            redirect(response, "addProfessor.html", "professor-error");

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void addCourse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        String semesterText = request.getParameter("semester");
        
        if (isBlank(courseCode) || isBlank(courseName)) {
            redirect(response, "addCourse.html", "course-error");
            return;
        }

        if (isBlank(courseCode) || isBlank(courseName)) {
            redirect(response, "addCourse.html", "course-error");
            return;
        }

        int semester;

        try {
            semester = Integer.parseInt(semesterText);
        } catch (Exception e) {
            redirect(response, "addCourse.html", "course-invalid");
            return;
        }

        if (semester < 1) {
            redirect(response, "addCourse.html", "course-invalid");
            return;
        }

        String sql =
                "INSERT INTO courses " +
                "(course_code, course_name, semester) " +
                "VALUES (?, ?, ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, courseCode.trim());
            statement.setString(2, courseName.trim());
            statement.setInt(3, semester);
            statement.executeUpdate();

            redirect(response, "addCourse.html", "course-success");

        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, "addCourse.html", "course-error");
        }
    }

    private void addStudentToCourse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String registrationText =
                request.getParameter("registrationNumber");
        String courseCode = request.getParameter("courseCode");

        int registrationNumber;

        try {
            registrationNumber = Integer.parseInt(registrationText);
        } catch (Exception e) {
            redirect(
                    response,
                    "addStudentToCourse.html",
                    "enrollment-invalid"
            );
            return;
        }

        if (registrationNumber < 1) {
            redirect(
                    response,
                    "addStudentToCourse.html",
                    "enrollment-invalid"
            );
            return;
        }

        if (isBlank(courseCode)) {
            redirect(
                    response,
                    "addStudentToCourse.html",
                    "enrollment-not-found"
            );
            return;
        }

        String sql =
                "INSERT INTO course_students " +
                "(student_id, course_id) " +
                "SELECT students.user_id, courses.course_id " +
                "FROM students, courses " +
                "WHERE students.registration_number = ? " +
                "AND courses.course_code = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, registrationNumber);
            statement.setString(2, courseCode.trim());

            int rows = statement.executeUpdate();

            redirect(
                    response,
                    "addStudentToCourse.html",
                    rows > 0
                            ? "enrollment-success"
                            : "enrollment-not-found"
            );

        } catch (Exception e) {
            e.printStackTrace();
            redirect(
                    response,
                    "addStudentToCourse.html",
                    "enrollment-error"
            );
        }
    }

    private void assignProfessor(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String courseCode = request.getParameter("courseCode");
        String professorNumberText = professorNumberParameter(request);

        int professorNumber;

        try {
            professorNumber = Integer.parseInt(professorNumberText);
        } catch (Exception e) {
            redirect(response, "assignProfessor.jsp", "assign-invalid");
            return;
        }

        if (professorNumber < 1) {
            redirect(response, "assignProfessor.jsp", "assign-invalid");
            return;
        }

        if (isBlank(courseCode)) {
            redirect(
                    response,
                    "assignProfessor.jsp",
                    "assign-course-not-found"
            );
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            String professorSql =
                    "SELECT user_id FROM professors " +
                    "WHERE professor_number = ?";

            int professorId;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(professorSql)
            ) {

                statement.setInt(1, professorNumber);

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        redirect(
                                response,
                                "assignProfessor.jsp",
                                "assign-professor-not-found"
                        );
                        return;
                    }

                    professorId = result.getInt("user_id");
                }
            }

            String updateSql =
                    "UPDATE courses SET professor_id = ? " +
                    "WHERE course_code = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(updateSql)
            ) {

                statement.setInt(1, professorId);
                statement.setString(2, courseCode.trim());

                int rows = statement.executeUpdate();

                redirect(
                        response,
                        "assignProfessor.jsp",
                        rows > 0
                                ? "assign-success"
                                : "assign-course-not-found"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, "assignProfessor.jsp", "assign-error");
        }
    }

    private List<Map<String, Object>> loadStudents() {

        List<Map<String, Object>> rows = new ArrayList<>();

        String sql =
                "SELECT users.name, users.surname, " +
                "students.registration_number " +
                "FROM students " +
                "JOIN users ON students.user_id = users.user_id " +
                "ORDER BY students.registration_number";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()
        ) {

            while (result.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("registrationNumber", result.getInt("registration_number"));
                row.put("name", result.getString("name"));
                row.put("surname", result.getString("surname"));
                rows.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private List<Map<String, Object>> loadProfessors() {

        List<Map<String, Object>> rows = new ArrayList<>();

        String sql =
                "SELECT users.name, users.surname, " +
                "professors.professor_number " +
                "FROM professors " +
                "JOIN users ON professors.user_id = users.user_id " +
                "ORDER BY professors.professor_number";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()
        ) {

            while (result.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("professorNumber", result.getInt("professor_number"));
                row.put("name", result.getString("name"));
                row.put("surname", result.getString("surname"));
                rows.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private List<Map<String, Object>> loadCourses() {

        List<Map<String, Object>> rows = new ArrayList<>();

        String sql =
                "SELECT course_code, course_name, semester " +
                "FROM courses " +
                "ORDER BY semester, course_code";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()
        ) {

            while (result.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("courseCode", result.getString("course_code"));
                row.put("courseName", result.getString("course_name"));
                row.put("semester", result.getInt("semester"));
                rows.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private List<Map<String, Object>> loadCoursesProfessors() {

        List<Map<String, Object>> rows = new ArrayList<>();

        String sql =
                "SELECT courses.course_code, courses.course_name, " +
                "courses.semester, " +
                "users.name AS professor_name, " +
                "users.surname AS professor_surname " +
                "FROM courses " +
                "LEFT JOIN professors " +
                "ON courses.professor_id = professors.user_id " +
                "LEFT JOIN users " +
                "ON professors.user_id = users.user_id " +
                "ORDER BY courses.semester, courses.course_code";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()
        ) {

            while (result.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("courseCode", result.getString("course_code"));
                row.put("courseName", result.getString("course_name"));
                row.put("semester", result.getInt("semester"));
                row.put("professorName", result.getString("professor_name"));
                row.put("professorSurname", result.getString("professor_surname"));
                rows.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private List<Map<String, Object>> loadEnrollments() {

        List<Map<String, Object>> rows = new ArrayList<>();

        String sql =
                "SELECT users.name, users.surname, " +
                "students.registration_number, " +
                "courses.course_code, courses.course_name " +
                "FROM course_students " +
                "JOIN students " +
                "ON course_students.student_id = students.user_id " +
                "JOIN users ON students.user_id = users.user_id " +
                "JOIN courses " +
                "ON course_students.course_id = courses.course_id " +
                "ORDER BY students.registration_number, courses.course_code";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()
        ) {

            while (result.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("registrationNumber", result.getInt("registration_number"));
                row.put("name", result.getString("name"));
                row.put("surname", result.getString("surname"));
                row.put("courseCode", result.getString("course_code"));
                row.put("courseName", result.getString("course_name"));
                rows.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    private boolean isSecretary(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"secretary".equals(session.getAttribute("role"))) {

            response.sendRedirect("index.html");
            return false;
        }

        return true;
    }

    private String professorNumberParameter(HttpServletRequest request) {

        String value = request.getParameter("professorNumber");

        if (value == null) {
            value = request.getParameter("profNumber");
        }

        return value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void redirect(
            HttpServletResponse response,
            String page,
            String status)
            throws IOException {

        response.sendRedirect(
                response.encodeRedirectURL(
                        page + "?status=" + status
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
