package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mainpackage.DBConnection;

@WebServlet("/professors")
public class ProfessorsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {

            Connection connection = DBConnection.getConnection();

            Statement statement = connection.createStatement();

            ResultSet results = statement.executeQuery(
                    "SELECT users.name, users.surname, " +
                    "professors.professor_number " +
                    "FROM users " +
                    "JOIN professors ON users.user_id = professors.user_id " +
                    "ORDER BY professors.professor_number"
            );

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Professors</title>");
            out.println("<link rel='stylesheet' href='style.css'>");

            out.println("<style>");

            out.println(".data-table {");
            out.println("width: 100%;");
            out.println("border-collapse: collapse;");
            out.println("table-layout: fixed;");
            out.println("margin-top: 25px;");
            out.println("}");

            out.println(".data-table th {");
            out.println("background: #2563eb;");
            out.println("color: white;");
            out.println("padding: 16px 25px;");
            out.println("text-align: left;");
            out.println("}");

            out.println(".data-table td {");
            out.println("padding: 14px 25px;");
            out.println("border-bottom: 1px solid #e2e8f0;");
            out.println("}");

            out.println(".data-table th:nth-child(1),");
            out.println(".data-table td:nth-child(1) {");
            out.println("width: 30%;");
            out.println("}");

            out.println(".data-table th:nth-child(2),");
            out.println(".data-table td:nth-child(2) {");
            out.println("width: 35%;");
            out.println("}");

            out.println(".data-table th:nth-child(3),");
            out.println(".data-table td:nth-child(3) {");
            out.println("width: 35%;");
            out.println("}");

            out.println(".data-table tr:hover td {");
            out.println("background: #f1f5f9;");
            out.println("}");

            out.println("</style>");
            out.println("</head>");

            out.println("<body>");

            out.println("<div class='container'>");

            out.println("<h1>Professors</h1>");

            out.println("<table class='data-table'>");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Name</th>");
            out.println("<th>Surname</th>");
            out.println("<th>Professor Number</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");

            while (results.next()) {

                out.println("<tr>");

                out.println(
                        "<td>" +
                        results.getString("name") +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        results.getString("surname") +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        results.getInt("professor_number") +
                        "</td>"
                );

                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");

            out.println(
                    "<a href='index.html'>&larr; Back to Home</a>"
            );

            out.println("</div>");

            out.println("</body>");
            out.println("</html>");

            connection.close();

        } catch (Exception e) {

            out.println("Error: " + e.getMessage());
        }
    }
}