package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/student-grades-semester")
public class StudentGradesBySemesterServlet extends HttpServlet {

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

        int userId =
                (Integer) session.getAttribute("userId");

        String semesterParameter =
                request.getParameter("semester");

        response.setContentType("text/html");

        PrintWriter out =
                response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Grades by Semester</title>");
        out.println("<link rel='stylesheet' href='style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println("<div class='container'>");

        out.println("<h1>Grades by Semester</h1>");

        if (semesterParameter == null ||
                semesterParameter.isBlank()) {

            String semesterSql =
                    "SELECT DISTINCT courses.semester " +
                    "FROM grades " +
                    "JOIN courses " +
                    "ON grades.course_id = courses.course_id " +
                    "WHERE grades.student_id = ? " +
                    "AND courses.semester IS NOT NULL " +
                    "ORDER BY courses.semester";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(semesterSql)
            ) {

                statement.setInt(1, userId);

                ResultSet result =
                        statement.executeQuery();

                out.println(
                        "<form action='student-grades-semester' method='get'>"
                );

                out.println("<label>Select Semester</label>");

                out.println(
                        "<select name='semester' required>"
                );

                while (result.next()) {

                    int semester =
                            result.getInt("semester");

                    out.println(
                            "<option value='" + semester + "'>" +
                            "Semester " + semester +
                            "</option>"
                    );
                }

                out.println("</select>");

                out.println(
                        "<button type='submit'>" +
                        "View Grades" +
                        "</button>"
                );

                out.println("</form>");

            } catch (Exception e) {

                e.printStackTrace();

                out.println(
                        "<p class='error'>" +
                        "Error loading semesters." +
                        "</p>"
                );
            }

        } else {

            try {

                int semester =
                        Integer.parseInt(semesterParameter);

                String gradesSql =
                        "SELECT courses.course_code, " +
                        "courses.course_name, " +
                        "courses.semester, " +
                        "grades.grade " +
                        "FROM grades " +
                        "JOIN courses " +
                        "ON grades.course_id = courses.course_id " +
                        "WHERE grades.student_id = ? " +
                        "AND courses.semester = ? " +
                        "ORDER BY courses.course_code";

                try (
                        Connection connection =
                                DBConnection.getConnection();

                        PreparedStatement statement =
                                connection.prepareStatement(gradesSql)
                ) {

                    statement.setInt(1, userId);
                    statement.setInt(2, semester);

                    ResultSet result =
                            statement.executeQuery();

                    out.println(
                            "<h2>Semester " +
                            semester +
                            "</h2>"
                    );

                    out.println("<table>");

                    out.println(
                            "<tr>" +
                            "<th>Course Code</th>" +
                            "<th>Course</th>" +
                            "<th>Grade</th>" +
                            "</tr>"
                    );

                    boolean found = false;

                    while (result.next()) {

                        found = true;

                        out.println("<tr>");

                        out.println(
                                "<td>" +
                                result.getString("course_code") +
                                "</td>"
                        );

                        out.println(
                                "<td>" +
                                result.getString("course_name") +
                                "</td>"
                        );

                        out.println(
                                "<td>" +
                                result.getBigDecimal("grade") +
                                "</td>"
                        );

                        out.println("</tr>");
                    }

                    out.println("</table>");

                    if (!found) {

                        out.println(
                                "<p>No grades found.</p>"
                        );
                    }
                }

                out.println(
                        "<a href='student-grades-semester'>" +
                        "Choose Another Semester" +
                        "</a>"
                );

            } catch (NumberFormatException e) {

                out.println(
                        "<p class='error'>" +
                        "Invalid semester." +
                        "</p>"
                );
            } catch (Exception e) {

                e.printStackTrace();

                out.println(
                        "<p class='error'>" +
                        "Error loading grades." +
                        "</p>"
                );
            }
        }

        out.println(
                "<a href='studentMenu.jsp'>" +
                "Back to Student Menu" +
                "</a>"
        );

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}