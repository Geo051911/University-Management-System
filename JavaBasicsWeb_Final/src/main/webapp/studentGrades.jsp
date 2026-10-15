<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.TreeSet" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (session == null || !"student".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.html");
        return;
    }

    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    List<Map<String, Object>> grades =
            (List<Map<String, Object>>) request.getAttribute("grades");

    if (grades == null) {
        response.sendRedirect(response.encodeRedirectURL("student-grades"));
        return;
    }

    String view = (String) request.getAttribute("view");
    String error = (String) request.getAttribute("error");

    String title = "My Grades";
    String description = "Review your complete academic grade history.";
    String icon = "📊";

    if ("course".equals(view)) {
        title = "Grades by Course";
        description = "Select a course to view your recorded grade.";
        icon = "📚";
    } else if ("semester".equals(view)) {
        title = "Grades by Semester";
        description = "Review your results for a specific semester.";
        icon = "🗓️";
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

<div class="container portal-page">

    <div class="page-header">
        <div class="page-heading">
            <div class="page-kicker">Student Portal</div>
            <h1><%= title %></h1>
            <p><%= description %></p>
        </div>

        <div class="page-icon"><%= icon %></div>
    </div>

    <% if (error != null) { %>
        <p class="error-message"><%= error %></p>
    <% } %>

    <% if ("all".equals(view)) { %>

        <div class="data-card">
            <table>
                <tr>
                    <th>Course Code</th>
                    <th>Course</th>
                    <th>Semester</th>
                    <th>Grade</th>
                </tr>

                <% for (Map<String, Object> row : grades) { %>
                    <tr>
                        <td><strong><%= row.get("courseCode") %></strong></td>
                        <td><%= row.get("courseName") %></td>
                        <td>Semester <%= row.get("semester") %></td>
                        <td>
                            <span class="grade-value"><%= row.get("grade") %></span>
                        </td>
                    </tr>
                <% } %>
            </table>

            <% if (grades.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <strong>No grades available yet.</strong>
                </div>
            <% } %>
        </div>

    <% } else if ("course".equals(view)) { %>

        <%
            String selectedCourse = request.getParameter("courseCode");
        %>

        <% if (selectedCourse == null || selectedCourse.isBlank()) { %>

            <div class="data-card">
                <form action="<%= response.encodeURL("student-grades-course") %>"
                      method="get">

                    <label for="courseCode">Select Course</label>

                    <select id="courseCode" name="courseCode" required>
                        <%
                            TreeMap<String, String> courses = new TreeMap<>();

                            for (Map<String, Object> row : grades) {
                                courses.put(
                                        row.get("courseCode").toString(),
                                        row.get("courseName").toString()
                                );
                            }

                            for (Map.Entry<String, String> course : courses.entrySet()) {
                        %>
                            <option value="<%= course.getKey() %>">
                                <%= course.getKey() %> - <%= course.getValue() %>
                            </option>
                        <% } %>
                    </select>

                    <button type="submit">View Grade</button>
                </form>
            </div>

        <% } else { %>

            <div class="data-card">
                <table>
                    <tr>
                        <th>Course Code</th>
                        <th>Course</th>
                        <th>Semester</th>
                        <th>Grade</th>
                    </tr>

                    <%
                        boolean found = false;

                        for (Map<String, Object> row : grades) {
                            if (selectedCourse.equals(row.get("courseCode"))) {
                                found = true;
                    %>
                        <tr>
                            <td><strong><%= row.get("courseCode") %></strong></td>
                            <td><%= row.get("courseName") %></td>
                            <td>Semester <%= row.get("semester") %></td>
                            <td>
                                <span class="grade-value"><%= row.get("grade") %></span>
                            </td>
                        </tr>
                    <%
                            }
                        }
                    %>
                </table>

                <% if (!found) { %>
                    <div class="empty-state">
                        <div class="empty-state-icon">📭</div>
                        No grade found for this course.
                    </div>
                <% } %>
            </div>

        <% } %>

        <div class="page-actions">
            <% if (selectedCourse != null && !selectedCourse.isBlank()) { %>
                <a href="<%= response.encodeURL("student-grades-course") %>">
                    ↻ Choose Another Course
                </a>
            <% } %>

            <a class="secondary-action"
               href="<%= response.encodeURL("studentMenu.jsp") %>">
                ← Back to Student Dashboard
            </a>
        </div>

    <% } else { %>

        <%
            String semesterParameter = request.getParameter("semester");
        %>

        <% if (semesterParameter == null || semesterParameter.isBlank()) { %>

            <div class="data-card">
                <form action="<%= response.encodeURL("student-grades-semester") %>"
                      method="get">

                    <label for="semester">Select Semester</label>

                    <select id="semester" name="semester" required>
                        <%
                            TreeSet<Integer> semesters = new TreeSet<>();

                            for (Map<String, Object> row : grades) {
                                semesters.add((Integer) row.get("semester"));
                            }

                            for (Integer semester : semesters) {
                        %>
                            <option value="<%= semester %>">
                                Semester <%= semester %>
                            </option>
                        <% } %>
                    </select>

                    <button type="submit">View Grades</button>
                </form>
            </div>

        <% } else { %>

            <div class="data-card">
                <%
                    Integer selectedSemester = null;

                    try {
                        selectedSemester = Integer.parseInt(semesterParameter);
                    } catch (NumberFormatException ignored) {
                    }
                %>

                <% if (selectedSemester == null) { %>
                    <p class="error-message">Invalid semester.</p>
                <% } else { %>
                    <h2>Semester <%= selectedSemester %></h2>

                    <table>
                        <tr>
                            <th>Course Code</th>
                            <th>Course</th>
                            <th>Grade</th>
                        </tr>

                        <%
                            boolean found = false;

                            for (Map<String, Object> row : grades) {
                                if (selectedSemester.equals(row.get("semester"))) {
                                    found = true;
                        %>
                            <tr>
                                <td><strong><%= row.get("courseCode") %></strong></td>
                                <td><%= row.get("courseName") %></td>
                                <td>
                                    <span class="grade-value"><%= row.get("grade") %></span>
                                </td>
                            </tr>
                        <%
                                }
                            }
                        %>
                    </table>

                    <% if (!found) { %>
                        <div class="empty-state">
                            <div class="empty-state-icon">📭</div>
                            No grades found for this semester.
                        </div>
                    <% } %>
                <% } %>
            </div>

        <% } %>

        <div class="page-actions">
            <% if (semesterParameter != null && !semesterParameter.isBlank()) { %>
                <a href="<%= response.encodeURL("student-grades-semester") %>">
                    ↻ Choose Another Semester
                </a>
            <% } %>

            <a class="secondary-action"
               href="<%= response.encodeURL("studentMenu.jsp") %>">
                ← Back to Student Dashboard
            </a>
        </div>

    <% } %>

    <% if ("all".equals(view)) { %>
        <div class="page-actions">
            <a class="secondary-action"
               href="<%= response.encodeURL("studentMenu.jsp") %>">
                ← Back to Student Dashboard
            </a>
        </div>
    <% } %>

</div>

</body>
</html>
