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
import mainpackage.PasswordUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username =
                request.getParameter("username");

        String password =
                request.getParameter("password");

        String selectedRole =
                request.getParameter("role");

        String sql =
                "SELECT u.user_id, u.Username, " +
                "u.password_hash, u.password_salt, " +
                "CASE " +
                "WHEN s.user_id IS NOT NULL THEN 'student' " +
                "WHEN p.user_id IS NOT NULL THEN 'professor' " +
                "WHEN sec.user_id IS NOT NULL THEN 'secretary' " +
                "END AS user_role " +
                "FROM users u " +
                "LEFT JOIN students s ON u.user_id = s.user_id " +
                "LEFT JOIN professors p ON u.user_id = p.user_id " +
                "LEFT JOIN secretaries sec ON u.user_id = sec.user_id " +
                "WHERE u.Username = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);

            ResultSet result =
                    statement.executeQuery();

            if (result.next()) {

                int userId =
                        result.getInt("user_id");

                String storedHash =
                        result.getString("password_hash");

                String storedSalt =
                        result.getString("password_salt");

                String actualRole =
                        result.getString("user_role");

                boolean correctPassword =
                        PasswordUtil.checkPassword(
                                password,
                                storedHash,
                                storedSalt
                        );

                if (correctPassword
                        && selectedRole.equals(actualRole)) {

                    HttpSession session =
                            request.getSession();

                    session.setAttribute(
                            "userId",
                            userId
                    );

                    session.setAttribute(
                            "username",
                            username
                    );

                    session.setAttribute(
                            "role",
                            actualRole
                    );

                    if (actualRole.equals("student")) {

                        response.sendRedirect(
                                "studentMenu.jsp"
                        );

                    } else if (actualRole.equals("professor")) {

                        response.sendRedirect(
                                "professorMenu.jsp"
                        );

                    } else if (actualRole.equals("secretary")) {

                        response.sendRedirect(
                                "secretaryMenu.jsp"
                        );
                    }

                } else {

                    response.getWriter().println(
                            "Invalid username, password or user type."
                    );
                }

            } else {

                response.getWriter().println(
                        "Invalid username, password or user type."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                    "Login error: " + e.getMessage()
            );
        }
    }
}