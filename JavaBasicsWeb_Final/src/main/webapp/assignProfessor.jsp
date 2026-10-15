<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession currentSession =
            request.getSession(false);

    if (currentSession == null ||
            !"secretary".equals(
                    currentSession.getAttribute("role"))) {

        response.sendRedirect("index.html");
        return;
    }


    response.setHeader(
            "Cache-Control",
            "no-cache, no-store, must-revalidate"
    );

    response.setHeader(
            "Pragma",
            "no-cache"
    );

    response.setDateHeader(
            "Expires",
            0
    );
%>


<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Assign Professor</title>

    <link rel="stylesheet"
          href="style.css?v=9">

</head>


<body>

<div class="container portal-page secretary-page">

    <div class="page-header">

        <div class="page-heading">

            <div class="page-kicker">
                Administration
            </div>

            <h1>Assign Professor</h1>

            <p>
                Assign a responsible professor
                to an existing university course.
            </p>

        </div>

        <div class="page-icon">
            🧑‍🏫
        </div>

    </div>


    <div class="admin-form-card">

        <div class="admin-form-intro">

            <div class="admin-form-icon">
                🔗
            </div>

            <div>

                <strong>
                    Course Assignment
                </strong>

                <span>
                    Connect a course with
                    a registered professor.
                </span>

            </div>

        </div>


        <form action="<%= response.encodeURL("assign-professor") %>"
              method="post">

            <div class="form-grid">


                <div class="form-field">

                    <label for="courseCode">
                        Course Code
                    </label>

                    <input type="text"
                           id="courseCode"
                           name="courseCode"
                           maxlength="20"
                           placeholder="e.g. CS114"
                           required>

                </div>


                <div class="form-field">

                    <label for="profNumber">
                        Professor Number
                    </label>

                    <input type="number"
                           id="profNumber"
                           name="profNumber"
                           min="1"
                           step="1"
                           placeholder="e.g. 5001"
                           data-range-message="Professor number must be a positive whole number."
                           required>

                </div>


            </div>


            <button class="admin-submit"
                    type="submit">

                Assign Professor

            </button>

        </form>

    </div>


    <div class="page-actions">

        <a class="secondary-action"
           href="<%= response.encodeURL("secretaryMenu.jsp") %>">

            ← Back to Administration Dashboard

        </a>

    </div>

</div>


<script src="ui.js?v=9"></script>

</body>

</html>