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
import mainpackage.DBConnection;

@WebServlet("/add-professor")
public class AddProfessorServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String department = request.getParameter("department");

        int professorNumber =
                Integer.parseInt(request.getParameter("professorNumber"));

        try {

            Connection connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            try {

                String sql1 =
                        "INSERT INTO users (Username, name, surname, department) " +
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

                ResultSet keys = statement1.getGeneratedKeys();
                keys.next();

                int userId = keys.getInt(1);

                String sql2 =
                        "INSERT INTO professors (user_id, professor_number) " +
                        "VALUES (?, ?)";

                PreparedStatement statement2 =
                        connection.prepareStatement(sql2);

                statement2.setInt(1, userId);
                statement2.setInt(2, professorNumber);

                statement2.executeUpdate();

                connection.commit();

                out.println("Professor added successfully!");

            } catch (Exception e) {

                connection.rollback();

                out.println(
                        "Professor could not be added. " +
                        "Username or professor number may already exist."
                );
            }

            connection.close();

        } catch (Exception e) {

            out.println("Database error: " + e.getMessage());
        }
    }
}