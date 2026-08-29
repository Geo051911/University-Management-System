<%

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

String role = (String) session.getAttribute("role");

if (role == null || !role.equals("student")) {
    response.sendRedirect("index.html");
    return;
}

%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Menu</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="container">

    <h1>Student Menu</h1>

    <p>Welcome, <%= session.getAttribute("username") %></p>

    <h2>Student Functions</h2>

    <a href="student-grades-course">View Grades by Course</a>

    <a href="student-grades-semester">View Grades by Semester</a>

    <a href="student-grades">View All Grades</a>

    <a href="logout" class="logout-button">Logout</a>

</div>

</body>
</html>