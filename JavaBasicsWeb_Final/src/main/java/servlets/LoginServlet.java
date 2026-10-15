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

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("index.html");
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username =
                request.getParameter(
                        "username"
                );

        String password =
                request.getParameter(
                        "password"
                );

        String selectedRole =
                request.getParameter(
                        "role"
                );


        if (username == null ||
                username.isBlank() ||
                password == null ||
                password.isBlank()) {

            redirectToLogin(
                    response,
                    selectedRole,
                    "login-invalid"
            );

            return;
        }


        String sql =
                "SELECT u.user_id, " +
                "u.Username, " +
                "u.password_hash, " +
                "u.password_salt, " +

                "CASE " +
                "WHEN s.user_id IS NOT NULL THEN 'student' " +
                "WHEN p.user_id IS NOT NULL THEN 'professor' " +
                "WHEN sec.user_id IS NOT NULL THEN 'secretary' " +
                "END AS user_role " +

                "FROM users u " +

                "LEFT JOIN students s " +
                "ON u.user_id = s.user_id " +

                "LEFT JOIN professors p " +
                "ON u.user_id = p.user_id " +

                "LEFT JOIN secretaries sec " +
                "ON u.user_id = sec.user_id " +

                "WHERE u.Username = ?";


        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    username.trim()
            );


            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                if (!result.next()) {

                    redirectToLogin(
                            response,
                            selectedRole,
                            "login-invalid"
                    );

                    return;
                }


                int userId =
                        result.getInt(
                                "user_id"
                        );

                String storedHash =
                        result.getString(
                                "password_hash"
                        );

                String storedSalt =
                        result.getString(
                                "password_salt"
                        );

                String actualRole =
                        result.getString(
                                "user_role"
                        );


                if (storedHash == null ||
                        storedSalt == null ||
                        actualRole == null) {

                    redirectToLogin(
                            response,
                            selectedRole,
                            "login-error"
                    );

                    return;
                }


                boolean correctPassword =
                        PasswordUtil.checkPassword(
                                password,
                                storedHash,
                                storedSalt
                        );


                if (!correctPassword ||
                        !actualRole.equals(
                                selectedRole
                        )) {

                    redirectToLogin(
                            response,
                            selectedRole,
                            "login-invalid"
                    );

                    return;
                }


                /*
                 * Login successful.
                 * Create/get the user's session.
                 */

                HttpSession session =
                        request.getSession(true);


                session.setAttribute(
                        "userId",
                        userId
                );

                session.setAttribute(
                        "username",
                        username.trim()
                );

                session.setAttribute(
                        "role",
                        actualRole
                );


                /*
                 * Redirect to the correct dashboard.
                 * encodeRedirectURL is kept because
                 * it solved our session tracking issue.
                 */

                if ("student".equals(actualRole)) {

                    response.sendRedirect(
                            response.encodeRedirectURL(
                                    "studentMenu.jsp"
                            )
                    );

                    return;
                }


                if ("professor".equals(actualRole)) {

                    response.sendRedirect(
                            response.encodeRedirectURL(
                                    "professorMenu.jsp"
                            )
                    );

                    return;
                }


                if ("secretary".equals(actualRole)) {

                    response.sendRedirect(
                            response.encodeRedirectURL(
                                    "secretaryMenu.jsp"
                            )
                    );

                    return;
                }
            }


        } catch (Exception e) {

            e.printStackTrace();


            redirectToLogin(
                    response,
                    selectedRole,
                    "login-error"
            );
        }
    }


    /*
     * Sends the user back to the correct
     * login page when authentication fails.
     */

    private void redirectToLogin(
            HttpServletResponse response,
            String role,
            String status)
            throws IOException {

        String page;


        if ("professor".equals(role)) {

            page =
                    "professorLogin.html";

        } else if ("secretary".equals(role)) {

            page =
                    "secretaryLogin.html";

        } else {

            page =
                    "studentLogin.html";
        }


        response.sendRedirect(
                response.encodeRedirectURL(
                        page +
                        "?status=" +
                        status
                )
        );
    }
}