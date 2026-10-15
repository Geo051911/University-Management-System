package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mainpackage.DBConnection;

@WebServlet(urlPatterns = {
        "/student-grades",
        "/student-grades-course",
        "/student-grades-semester"
})
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"student".equals(session.getAttribute("role"))) {

            response.sendRedirect("index.html");
            return;
        }

        setNoCache(response);

        int userId = (Integer) session.getAttribute("userId");

        String sql =
                "SELECT courses.course_code, " +
                "courses.course_name, " +
                "courses.semester, " +
                "grades.grade " +
                "FROM grades " +
                "JOIN courses " +
                "ON grades.course_id = courses.course_id " +
                "WHERE grades.student_id = ? " +
                "ORDER BY courses.semester, courses.course_code";

        List<Map<String, Object>> grades = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Map<String, Object> row = new HashMap<>();

                    row.put("courseCode", result.getString("course_code"));
                    row.put("courseName", result.getString("course_name"));
                    row.put("semester", result.getInt("semester"));
                    row.put("grade", result.getBigDecimal("grade"));

                    grades.add(row);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
            request.setAttribute("error", "Error loading grades.");
        }

        String path = request.getServletPath();

        if ("/student-grades-course".equals(path)) {
            request.setAttribute("view", "course");
        } else if ("/student-grades-semester".equals(path)) {
            request.setAttribute("view", "semester");
        } else {
            request.setAttribute("view", "all");
        }

        request.setAttribute("grades", grades);
        request.getRequestDispatcher("/studentGrades.jsp")
                .forward(request, response);
    }

    private void setNoCache(HttpServletResponse response) {

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate"
        );

        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
