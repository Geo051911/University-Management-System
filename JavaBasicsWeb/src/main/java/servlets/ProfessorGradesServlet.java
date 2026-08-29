package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/professor-grades")
public class ProfessorGradesServlet extends HttpServlet {

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
        out.println("<title>Professor Grades</title>");
        out.println("<link rel='stylesheet' href='style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println("<div class='container'>");

        out.println("<h1>Grades by Course</h1>");

        /*
         * FIRST PAGE:
         * No course has been selected yet.
         * Show the professor's assigned courses.
         */
        if (courseCode == null || courseCode.isBlank()) {

            String coursesSql =
                    "SELECT course_code, course_name " +
                    "FROM courses " +
                    "WHERE professor_id = ? " +
                    "ORDER BY course_code";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(coursesSql)
            ) {

                statement.setInt(1, professorId);

                ResultSet result =
                        statement.executeQuery();

                out.println(
                        "<form action='professor-grades' method='get'>"
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
                            "View Grades" +
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
             * SECOND PAGE:
             * A course was selected.
             * Show students who already have a grade.
             */

            String gradesSql =
                    "SELECT " +
                    "students.registration_number, " +
                    "users.name, " +
                    "users.surname, " +
                    "grades.grade " +
                    "FROM courses " +
                    "JOIN grades " +
                    "ON courses.course_id = grades.course_id " +
                    "JOIN students " +
                    "ON grades.student_id = students.user_id " +
                    "JOIN users " +
                    "ON students.user_id = users.user_id " +
                    "WHERE courses.professor_id = ? " +
                    "AND courses.course_code = ? " +
                    "ORDER BY students.registration_number";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(gradesSql)
            ) {

                statement.setInt(1, professorId);
                statement.setString(2, courseCode);

                ResultSet result =
                        statement.executeQuery();

                out.println(
                        "<h2>" + courseCode + "</h2>"
                );

                out.println("<table>");

                out.println(
                        "<tr>" +
                        "<th>Registration Number</th>" +
                        "<th>Name</th>" +
                        "<th>Surname</th>" +
                        "<th>Grade</th>" +
                        "</tr>"
                );

                boolean foundGrade = false;

                while (result.next()) {

                    foundGrade = true;

                    out.println("<tr>");

                    out.println(
                            "<td>" +
                            result.getInt("registration_number") +
                            "</td>"
                    );

                    out.println(
                            "<td>" +
                            result.getString("name") +
                            "</td>"
                    );

                    out.println(
                            "<td>" +
                            result.getString("surname") +
                            "</td>"
                    );

                    out.println(
                            "<td>" +
                            result.getBigDecimal("grade") +
                            "</td>"
                    );

                    out.println("</tr>");
                }

                out.println("</table>");

                if (!foundGrade) {

                    out.println(
                            "<p>No graded students found for this course.</p>"
                    );
                }

                out.println(
                        "<a href='professor-grades'>" +
                        "Choose Another Course" +
                        "</a>"
                );

            } catch (Exception e) {

                e.printStackTrace();

                out.println(
                        "<p class='error-message'>" +
                        "Error loading grades." +
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
}