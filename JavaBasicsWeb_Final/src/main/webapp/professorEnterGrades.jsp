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
        response.sendRedirect(response.encodeRedirectURL("professor-enter-grades"));
        return;
    }

    List<Map<String, Object>> students =
            (List<Map<String, Object>>) request.getAttribute("students");

    String selectedCourse =
            (String) request.getAttribute("selectedCourse");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enter Grades</title>
    <link rel="stylesheet" href="style.css?v=9">
</head>
<body>

<div class="container portal-page professor-page">

    <div class="page-header">
        <div class="page-heading">
            <div class="page-kicker">Professor Portal</div>
            <h1>Enter Grades</h1>
            <p>
                Record grades for students who
                have not yet been graded.
            </p>
        </div>

        <div class="page-icon">✏️</div>
    </div>

    <% if (selectedCourse == null || selectedCourse.isBlank()) { %>

        <div class="data-card">
            <form action="<%= response.encodeURL("professor-enter-grades") %>"
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
                    <button type="submit">Continue</button>
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

            <div class="grade-entry-list">
                <% if (students != null) {
                       for (Map<String, Object> student : students) { %>

                    <form class="grade-entry-card"
                          action="<%= response.encodeURL("professor-enter-grades") %>"
                          method="post">

                        <div class="grade-student">
                            <div class="grade-student-avatar"><%= student.get("name").toString().substring(0, 1).toUpperCase() %></div>

                            <div>
                                <strong>
                                    <%= student.get("name") %> <%= student.get("surname") %>
                                </strong>
                                <span>
                                    Registration No. <%= student.get("registrationNumber") %>
                                </span>
                            </div>
                        </div>

                        <div class="grade-field">
                            <label>Grade</label>
                            <input type="number"
                                   name="grade"
                                   min="0"
                                   max="10"
                                   step="0.01"
                                   placeholder="0 - 10"
                                   data-range-message="Grade must be between 0 and 10."
                                   required>
                        </div>

                        <input type="hidden"
                               name="studentId"
                               value="<%= student.get("studentId") %>">

                        <input type="hidden"
                               name="courseCode"
                               value="<%= selectedCourse %>">

                        <button type="submit">Save Grade</button>
                    </form>

                <%     }
                   } %>
            </div>

            <% if (students == null || students.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">✅</div>
                    <strong>All enrolled students have already been graded.</strong>
                </div>
            <% } %>
        </div>

    <% } %>

    <div class="page-actions">
        <% if (selectedCourse != null && !selectedCourse.isBlank()) { %>
            <a href="<%= response.encodeURL("professor-enter-grades") %>">
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
