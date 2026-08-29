package servlets;

import java.io.IOException;
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

@WebServlet("/assign-professor")
public class AssignProfessorServlet extends HttpServlet {

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

        try {

            int professorNumber =
                    Integer.parseInt(
                            request.getParameter("professorNumber")
                    );

            try (
                    Connection connection =
                            DBConnection.getConnection()
            ) {

                String professorSql =
                        "SELECT user_id " +
                        "FROM professors " +
                        "WHERE professor_number = ?";

                PreparedStatement professorStatement =
                        connection.prepareStatement(professorSql);

                professorStatement.setInt(
                        1,
                        professorNumber
                );

                ResultSet professorResult =
                        professorStatement.executeQuery();

                if (!professorResult.next()) {

                    response.setContentType("text/html");

                    response.getWriter().println(
                            "<html>" +
                            "<head>" +
                            "<link rel='stylesheet' href='style.css'>" +
                            "</head>" +
                            "<body>" +
                            "<div class='message-box'>" +
                            "<h1>Professor Not Found</h1>" +
                            "<a href='assignProfessor.jsp'>" +
                            "Try Again</a>" +
                            "</div>" +
                            "</body>" +
                            "</html>"
                    );

                    return;
                }

                int professorId =
                        professorResult.getInt("user_id");

                String updateSql =
                        "UPDATE courses " +
                        "SET professor_id = ? " +
                        "WHERE course_code = ?";

                PreparedStatement updateStatement =
                        connection.prepareStatement(updateSql);

                updateStatement.setInt(
                        1,
                        professorId
                );

                updateStatement.setString(
                        2,
                        courseCode
                );

                int rows =
                        updateStatement.executeUpdate();

                response.setContentType("text/html");

                if (rows > 0) {

                    response.getWriter().println(
                            "<html>" +
                            "<head>" +
                            "<link rel='stylesheet' href='style.css'>" +
                            "</head>" +
                            "<body>" +
                            "<div class='message-box'>" +
                            "<h1>Professor Assigned Successfully</h1>" +
                            "<p>Course: " + courseCode + "</p>" +
                            "<a href='secretaryMenu.jsp'>" +
                            "Back to Secretary Menu</a>" +
                            "</div>" +
                            "</body>" +
                            "</html>"
                    );

                } else {

                    response.getWriter().println(
                            "<html>" +
                            "<head>" +
                            "<link rel='stylesheet' href='style.css'>" +
                            "</head>" +
                            "<body>" +
                            "<div class='message-box'>" +
                            "<h1>Course Not Found</h1>" +
                            "<a href='assignProfessor.jsp'>" +
                            "Try Again</a>" +
                            "</div>" +
                            "</body>" +
                            "</html>"
                    );
                }
            }

        } catch (NumberFormatException e) {

            response.getWriter().println(
                    "Invalid professor number."
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                    "Error assigning professor."
            );
        }
    }
}