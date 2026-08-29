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

@WebServlet("/add-student-course")
public class AddStudentToCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int registrationNumber =
                Integer.parseInt(request.getParameter("registrationNumber"));

        String courseCode =
                request.getParameter("courseCode");

        try {

            Connection connection = DBConnection.getConnection();

            String sql =
                    "INSERT INTO course_students (student_id, course_id) " +
                    "SELECT students.user_id, courses.course_id " +
                    "FROM students, courses " +
                    "WHERE students.registration_number = ? " +
                    "AND courses.course_code = ?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, registrationNumber);
            statement.setString(2, courseCode);

            int rows = statement.executeUpdate();

            if (rows > 0) {

                out.println("Student added to course successfully!");

            } else {

                out.println("Student or course not found.");
            }

            connection.close();

        } catch (Exception e) {

            out.println(
                    "Student could not be added to this course. " +
                    "The student may already be enrolled."
            );
        }
    }
}