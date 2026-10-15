<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (session == null || !"professor".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.html");
        return;
    }

    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    List<Map<String, Object>> courses =
            (List<Map<String, Object>>) request.getAttribute("courses");

    if (courses == null) {
        response.sendRedirect(response.encodeRedirectURL("professor-grades"));
        return;
    }

    List<Map<String, Object>> grades =
            (List<Map<String, Object>>) request.getAttribute("grades");

    String selectedCourse =
            (String) request.getAttribute("selectedCourse");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Grades</title>
    <link rel="stylesheet" href="style.css?v=9">
</head>
<body>

<div class="container portal-page professor-page">

    <div class="page-header">
        <div class="page-heading">
            <div class="page-kicker">Professor Portal</div>
            <h1>Course Grades</h1>
            <p>
                Review grades already recorded for students
                in your assigned courses.
            </p>
        </div>

        <div class="page-icon">📊</div>
    </div>

    <% if (selectedCourse == null || selectedCourse.isBlank()) { %>

        <div class="data-card">
            <form action="<%= response.encodeURL("professor-grades") %>"
                  method="get">

                <label for="courseCode">Select Course</label>

                <select id="courseCode" name="courseCode" required>
                    <% for (Map<String, Object> course : courses) { %>
                        <option value="<%= course.get("courseCode") %>">
                            <%= course.get("courseCode") %> - <%= course.get("courseName") %>
                        </option>
                    <% } %>
                </select>

                <% if (!courses.isEmpty()) { %>
                    <button type="submit">View Course Grades</button>
                <% } else { %>
                    <div class="empty-state">
                        <div class="empty-state-icon">📭</div>
                        <strong>No courses assigned to you.</strong>
                    </div>
                <% } %>
            </form>
        </div>

    <% } else { %>

        <div class="data-card">
            <div class="course-badge">
                Course · <%= selectedCourse %>
            </div>

            <table>
                <tr>
                    <th>Registration Number</th>
                    <th>Name</th>
                    <th>Surname</th>
                    <th>Grade</th>
                </tr>

                <% if (grades != null) {
                       for (Map<String, Object> row : grades) { %>
                    <tr>
                        <td><%= row.get("registrationNumber") %></td>
                        <td><%= row.get("name") %></td>
                        <td><%= row.get("surname") %></td>
                        <td>
                            <span class="grade-value"><%= row.get("grade") %></span>
                        </td>
                    </tr>
                <%     }
                   } %>
            </table>

            <% if (grades == null || grades.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <strong>No graded students found for this course.</strong>
                </div>
            <% } %>
        </div>

    <% } %>

    <div class="page-actions">
        <% if (selectedCourse != null && !selectedCourse.isBlank()) { %>
            <a href="<%= response.encodeURL("professor-grades") %>">
                ↻ Choose Another Course
            </a>
        <% } %>

        <a class="secondary-action"
           href="<%= response.encodeURL("professorMenu.jsp") %>">
            ← Back to Professor Dashboard
        </a>
    </div>

</div>

<script src="ui.js?v=9"></script>
</body>
</html>
