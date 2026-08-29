	package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mainpackage.DBConnection;

@WebServlet("/add-student")
public class AddStudentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
    	
    	HttpSession session = request.getSession(false);

    	if (session == null ||
    	        !"secretary".equals(session.getAttribute("role"))) {

    	    response.sendRedirect("index.html");
    	    return;
    	}

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String department = request.getParameter("department");

        try {

            int registrationNumber =
                    Integer.parseInt(
                            request.getParameter("registrationNumber")
                    );

            Connection connection =
                    DBConnection.getConnection();

            connection.setAutoCommit(false);

            try {

                String sql1 =
                        "INSERT INTO users " +
                        "(Username, name, surname, department) " +
                        "VALUES (?, ?, ?, ?)";

                PreparedStatement statement1 =
                        connection.prepareStatement(
                                sql1,
                                Statement.RETURN_GENERATED_KEYS
                        );

                statement1.setString(1, username);
                statement1.setString(2, name);
                statement1.setString(3, surname);
                statement1.setString(4, department);

                statement1.executeUpdate();

                ResultSet keys =
                        statement1.getGeneratedKeys();

                keys.next();

                int userId = keys.getInt(1);

                String sql2 =
                        "INSERT INTO students " +
                        "(user_id, registration_number) " +
                        "VALUES (?, ?)";

                PreparedStatement statement2 =
                        connection.prepareStatement(sql2);

                statement2.setInt(1, userId);
                statement2.setInt(2, registrationNumber);

                statement2.executeUpdate();

                connection.commit();

                showMessage(
                        out,
                        "Student Added",
                        "Student added successfully!",
                        true
                );

            } catch (Exception e) {

                connection.rollback();

                showMessage(
                        out,
                        "Student Not Added",
                        "Username or registration number may already exist.",
                        false
                );
            }

            connection.close();

        } catch (Exception e) {

            showMessage(
                    out,
                    "Error",
                    "Student could not be added.",
                    false
            );
        }
    }

    private void showMessage(PrintWriter out,
                             String title,
                             String message,
                             boolean success) {

        String messageClass;

        if (success) {
            messageClass = "success-message";
        } else {
            messageClass = "error-message";
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>" + title + "</title>");
        out.println("<link rel='stylesheet' href='style.css'>");
        out.println("</head>");

        out.println("<body>");

        out.println("<div class='message-box'>");

        out.println(
                "<h1 class='" +
                messageClass +
                "'>" +
                title +
                "</h1>"
        );

        out.println("<p>" + message + "</p>");

        out.println(
                "<a href='addStudent.html'>" +
                "&larr; Add Another Student</a>"
        );

        out.println(
                "<a href='index.html'>" +
                "&larr; Back to Home</a>"
        );

        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}