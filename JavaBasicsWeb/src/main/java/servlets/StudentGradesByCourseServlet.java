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

@WebServlet("/student-grades-course")
public class StudentGradesByCourseServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"student".equals(session.getAttribute("role"))) {

            response.sendRedirect("index.html");
            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");

        String courseCode =
                request.getParameter("courseCode");

        response.setContentType("text/html");

        PrintWriter out =
                response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Grades by Course</title>");
        out.println("<link rel='stylesheet' href='style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println("<div class='container'>");

        out.println("<h1>Grades by Course</h1>");

        // If no course has been selected yet
        if (courseCode == null || courseCode.isBlank()) {

            String coursesSql =
                    "SELECT courses.course_code, courses.course_name " +
                    "FROM grades " +
                    "JOIN courses ON grades.course_id = courses.course_id " +
                    "WHERE grades.student_id = ? " +
                    "ORDER BY courses.course_code";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(coursesSql)
            ) {

                statement.setInt(1, userId);

                ResultSet result =
                        statement.executeQuery();

                out.println(
                        "<form action='student-grades-course' method='get'>"
                );

                out.println("<label>Select Course</label>");

                out.println(
                        "<select name='courseCode' required>"
                );

                while (result.next()) {

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

                out.println(
                        "<button type='submit'>View Grade</button>"
                );

                out.println("</form>");

            } catch (Exception e) {

                e.printStackTrace();

                out.println(
                        "<p class='error'>Error loading courses.</p>"
                );
            }

        } else {

            String gradeSql =
                    "SELECT courses.course_code, " +
                    "courses.course_name, " +
                    "courses.semester, " +
                    "grades.grade " +
                    "FROM grades " +
                    "JOIN courses " +
                    "ON grades.course_id = courses.course_id " +
                    "WHERE grades.student_id = ? " +
                    "AND courses.course_code = ?";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(gradeSql)
            ) {

                statement.setInt(1, userId);
                statement.setString(2, courseCode);

                ResultSet result =
                        statement.executeQuery();

                if (result.next()) {

                    out.println("<table>");

                    out.println(
                            "<tr>" +
                            "<th>Course Code</th>" +
                            "<th>Course</th>" +
                            "<th>Semester</th>" +
                            "<th>Grade</th>" +
                            "</tr>"
                    );

                    out.println("<tr>");

                    out.println(
                            "<td>" +
                            result.getString("course_code") +
                            "</td>"
                    );

                    out.println(
                            "<td>" +
                            result.getString("course_name") +
                            "</td>"
                    );

                    out.println(
                            "<td>" +
                            result.getInt("semester") +
                            "</td>"
                    );

                    out.println(
                            "<td>" +
                            result.getBigDecimal("grade") +
                            "</td>"
                    );

                    out.println("</tr>");
                    out.println("</table>");

                } else {

                    out.println(
                            "<p>No grade found for this course.</p>"
                    );
                }

                out.println(
                        "<a href='student-grades-course'>" +
                        "Choose Another Course</a>"
                );

            } catch (Exception e) {

                e.printStackTrace();

                out.println(
                        "<p class='error'>Error loading grade.</p>"
                );
            }
        }

        out.println(
                "<a href='studentMenu.jsp'>Back to Student Menu</a>"
        );

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}