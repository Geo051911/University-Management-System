package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mainpackage.DBConnection;

@WebServlet("/add-course")
public class AddCourseServlet extends HttpServlet {

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                !"secretary".equals(session.getAttribute("role"))) {

            response.sendRedirect("index.html");
            return;
        }

        String courseCode =
                request.getParameter("courseCode");

        String courseName =
                request.getParameter("courseName");

        try {

            int semester =
                    Integer.parseInt(
                            request.getParameter("semester")
                    );

            if (semester < 1) {
                response.getWriter().println(
                        "Semester must be greater than 0."
                );
                return;
            }

            String sql =
                    "INSERT INTO courses " +
                    "(course_code, course_name, semester) " +
                    "VALUES (?, ?, ?)";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(1, courseCode);
                statement.setString(2, courseName);
                statement.setInt(3, semester);

                statement.executeUpdate();

                response.setContentType("text/html");

                response.getWriter().println(
                        "<html>" +
                        "<head>" +
                        "<link rel='stylesheet' href='style.css'>" +
                        "</head>" +
                        "<body>" +
                        "<div class='message-box'>" +
                        "<h1>Course Added Successfully</h1>" +
                        "<p>" + courseCode + " - " +
                        courseName + "</p>" +
                        "<a href='secretaryMenu.jsp'>" +
                        "Back to Secretary Menu</a>" +
                        "</div>" +
                        "</body>" +
                        "</html>"
                );
            }

        } catch (NumberFormatException e) {

            response.getWriter().println(
                    "Invalid semester."
            );

        } catch (SQLException e) {

            response.setContentType("text/html");

            response.getWriter().println(
                    "<html>" +
                    "<head>" +
                    "<link rel='stylesheet' href='style.css'>" +
                    "</head>" +
                    "<body>" +
                    "<div class='message-box'>" +
                    "<h1>Course Could Not Be Added</h1>" +
                    "<p>The course code may already exist.</p>" +
                    "<a href='addCourse.html'>Try Again</a>" +
                    "</div>" +
                    "</body>" +
                    "</html>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                    "Error adding course."
            );
        }
    }
}