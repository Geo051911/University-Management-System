package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mainpackage.DBConnection;

@WebServlet("/add-grade")
public class AddGradeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {

            int registrationNumber =
                    Integer.parseInt(request.getParameter("registrationNumber"));

            String courseCode =
                    request.getParameter("courseCode");

            double grade =
                    Double.parseDouble(request.getParameter("grade"));

            Connection connection = DBConnection.getConnection();

            String sql =
                    "INSERT INTO grades (student_id, course_id, grade) " +
                    "SELECT students.user_id, courses.course_id, ? " +
                    "FROM course_students " +
                    "JOIN students ON course_students.student_id = students.user_id " +
                    "JOIN courses ON course_students.course_id = courses.course_id " +
                    "WHERE students.registration_number = ? " +
                    "AND courses.course_code = ? " +
                    "ON DUPLICATE KEY UPDATE grade = ?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setDouble(1, grade);
            statement.setInt(2, registrationNumber);
            statement.setString(3, courseCode);
            statement.setDouble(4, grade);

            int rows = statement.executeUpdate();

            if (rows > 0) {

                out.println("Grade saved successfully!");

            } else {

                out.println("Student is not enrolled in this course.");
            }

            connection.close();

        } catch (Exception e) {

            out.println("Grade could not be saved.");
        }
    }
}