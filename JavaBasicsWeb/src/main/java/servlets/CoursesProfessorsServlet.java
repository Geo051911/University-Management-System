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

@WebServlet("/courses-professors")
public class CoursesProfessorsServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"secretary".equals(session.getAttribute("role"))) {

            response.sendRedirect("index.html");
            return;
        }

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String sql =
                "SELECT courses.course_code, " +
                "courses.course_name, " +
                "courses.semester, " +
                "users.name, " +
                "users.surname " +
                "FROM courses " +
                "LEFT JOIN professors " +
                "ON courses.professor_id = professors.user_id " +
                "LEFT JOIN users " +
                "ON professors.user_id = users.user_id " +
                "ORDER BY courses.course_code";

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Courses and Professors</title>");
        out.println("<link rel='stylesheet' href='style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println("<div class='container'>");

        out.println("<h1>Courses and Professors</h1>");

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            ResultSet result =
                    statement.executeQuery();

            out.println("<table>");

            out.println(
                    "<tr>" +
                    "<th>Course Code</th>" +
                    "<th>Course</th>" +
                    "<th>Semester</th>" +
                    "<th>Professor</th>" +
                    "</tr>"
            );

            while (result.next()) {

                String professorName;

                if (result.getString("name") == null) {

                    professorName =
                            "Not Assigned";

                } else {

                    professorName =
                            result.getString("name") +
                            " " +
                            result.getString("surname");
                }

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
                        professorName +
                        "</td>"
                );

                out.println("</tr>");
            }

            out.println("</table>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<p class='error-message'>" +
                    "Error loading courses." +
                    "</p>"
            );
        }

        out.println(
                "<a href='secretaryMenu.jsp'>" +
                "Back to Secretary Menu" +
                "</a>"
        );

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}