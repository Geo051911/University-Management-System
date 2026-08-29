package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mainpackage.DBConnection;

@WebServlet("/db-test")
public class DBTestServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        PrintWriter out = response.getWriter();

        try {

            Connection connection =
                    DBConnection.getConnection();

            out.println("Database connection successful!");

            connection.close();

        } catch (Exception e) {

            out.println("Database connection failed.");
            out.println(e.getMessage());
        }
    }
}