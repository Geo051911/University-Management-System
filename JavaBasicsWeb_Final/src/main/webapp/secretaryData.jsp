<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (session == null || !"secretary".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.html");
        return;
    }

    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String view = (String) request.getAttribute("view");
    List<Map<String, Object>> rows =
            (List<Map<String, Object>>) request.getAttribute("rows");

    if (view == null || rows == null) {
        response.sendRedirect(response.encodeRedirectURL("secretaryMenu.jsp"));
        return;
    }

    String title = "Administration";
    String description = "";
    String icon = "🏛️";

    if ("students".equals(view)) {
        title = "Registered Students";
        description = "View all students currently registered in the university system.";
        icon = "👥";
    } else if ("professors".equals(view)) {
        title = "University Professors";
        description = "View all registered professors and their professor numbers.";
        icon = "🧑‍🏫";
    } else if ("courses".equals(view)) {
        title = "University Courses";
        description = "Browse all courses and their corresponding semesters.";
        icon = "📚";
    } else if ("coursesProfessors".equals(view)) {
        title = "Courses & Professors";
        description = "Review the responsible professor assigned to each university course.";
        icon = "🏫";
    } else if ("enrollments".equals(view)) {
        title = "Student Enrollments";
        description = "Review student-to-course enrollment records.";
        icon = "🔗";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= title %></title>
    <link rel="stylesheet" href="style.css?v=9">
</head>
<body>

<div class="container portal-page secretary-page">

    <div class="page-header">
        <div class="page-heading">
            <div class="page-kicker">Administration</div>
            <h1><%= title %></h1>
            <p><%= description %></p>
        </div>

        <div class="page-icon"><%= icon %></div>
    </div>

    <div class="data-card">

        <% if ("students".equals(view)) { %>

            <table>
                <tr>
                    <th>Registration Number</th>
                    <th>Name</th>
                    <th>Surname</th>
                </tr>

                <% for (Map<String, Object> row : rows) { %>
                    <tr>
                        <td>
                            <span class="record-number">
                                <%= row.get("registrationNumber") %>
                            </span>
                        </td>
                        <td><%= row.get("name") %></td>
                        <td><%= row.get("surname") %></td>
                    </tr>
                <% } %>
            </table>

            <% if (rows.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <strong>No students found.</strong>
                </div>
            <% } %>

        <% } else if ("professors".equals(view)) { %>

            <table>
                <tr>
                    <th>Professor Number</th>
                    <th>Name</th>
                    <th>Surname</th>
                </tr>

                <% for (Map<String, Object> row : rows) { %>
                    <tr>
                        <td>
                            <span class="record-number">
                                <%= row.get("professorNumber") %>
                            </span>
                        </td>
                        <td><%= row.get("name") %></td>
                        <td><%= row.get("surname") %></td>
                    </tr>
                <% } %>
            </table>

            <% if (rows.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <strong>No professors found.</strong>
                </div>
            <% } %>

        <% } else if ("courses".equals(view)) { %>

            <table>
                <tr>
                    <th>Course Code</th>
                    <th>Course Name</th>
                    <th>Semester</th>
                </tr>

                <% for (Map<String, Object> row : rows) { %>
                    <tr>
                        <td>
                            <span class="course-code-badge">
                                <%= row.get("courseCode") %>
                            </span>
                        </td>
                        <td><%= row.get("courseName") %></td>
                        <td>
                            <span class="semester-badge">
                                Semester <%= row.get("semester") %>
                            </span>
                        </td>
                    </tr>
                <% } %>
            </table>

            <% if (rows.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <strong>No courses found.</strong>
                </div>
            <% } %>

        <% } else if ("coursesProfessors".equals(view)) { %>

            <table>
                <tr>
                    <th>Course Code</th>
                    <th>Course</th>
                    <th>Semester</th>
                    <th>Responsible Professor</th>
                </tr>

                <% for (Map<String, Object> row : rows) { %>
                    <tr>
                        <td>
                            <span class="course-code-badge">
                                <%= row.get("courseCode") %>
                            </span>
                        </td>
                        <td><%= row.get("courseName") %></td>
                        <td>
                            <span class="semester-badge">
                                Semester <%= row.get("semester") %>
                            </span>
                        </td>
                        <td>
                            <% if (row.get("professorName") != null) { %>
                                <span class="assigned-badge">
                                    ✓ <%= row.get("professorName") %>
                                    <%= row.get("professorSurname") %>
                                </span>
                            <% } else { %>
                                <span class="unassigned-badge">
                                    ● Not Assigned
                                </span>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </table>

            <% if (rows.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <strong>No courses found.</strong>
                </div>
            <% } %>

        <% } else { %>

            <table>
                <tr>
                    <th>Registration Number</th>
                    <th>Student</th>
                    <th>Course Code</th>
                    <th>Course</th>
                </tr>

                <% for (Map<String, Object> row : rows) { %>
                    <tr>
                        <td>
                            <span class="record-number">
                                <%= row.get("registrationNumber") %>
                            </span>
                        </td>
                        <td>
                            <%= row.get("name") %> <%= row.get("surname") %>
                        </td>
                        <td>
                            <span class="course-code-badge">
                                <%= row.get("courseCode") %>
                            </span>
                        </td>
                        <td><%= row.get("courseName") %></td>
                    </tr>
                <% } %>
            </table>

            <% if (rows.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <strong>No enrollments found.</strong>
                </div>
            <% } %>

        <% } %>

    </div>

    <div class="page-actions">
        <% if ("coursesProfessors".equals(view)) { %>
            <a href="<%= response.encodeURL("assignProfessor.jsp") %>">
                Assign Professor to Course →
            </a>
        <% } %>

        <% if ("enrollments".equals(view)) { %>
            <a href="<%= response.encodeURL("addStudentToCourse.html") %>">
                Add Student to Course →
            </a>
        <% } %>

        <a class="secondary-action"
           href="<%= response.encodeURL("secretaryMenu.jsp") %>">
            ← Back to Administration Dashboard
        </a>
    </div>

</div>

</body>
</html>
