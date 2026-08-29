package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mainpackage.DBConnection;

@WebServlet("/secretary-login")
public class SecretaryLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String username =
                request.getParameter("username");

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT users.user_id " +
                    "FROM users " +
                    "JOIN secretaries " +
                    "ON users.user_id = secretaries.user_id " +
                    "WHERE users.Username = ?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, username);

            ResultSet result =
                    statement.executeQuery();

            if (result.next()) {

                response.sendRedirect("secretaryMenu.html");

            } else {

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Login Failed</title>");
                out.println("<link rel='stylesheet' href='style.css'>");
                out.println("</head>");

                out.println("<body>");

                out.println("<div class='container'>");

                out.println("<h1>Login Failed</h1>");

                out.println(
                        "<p>Secretary username not found.</p>"
                );

                out.println(
                        "<a href='secretaryLogin.html'>" +
                        "&larr; Try Again</a>"
                );

                out.println(
                        "<a href='index.html'>" +
                        "&larr; Back to Home</a>"
                );

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

            connection.close();

        } catch (Exception e) {

            out.println(
                    "Login could not be completed."
            );
        }
    }
}	