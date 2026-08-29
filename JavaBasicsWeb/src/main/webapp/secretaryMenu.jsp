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

if (role == null || !role.equals("secretary")) {
    response.sendRedirect("index.html");
    return;
}

%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Secretary Menu</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="container">

    <h1>Secretary Menu</h1>

    <p>Welcome, <%= session.getAttribute("username") %></p>

    <h2>Secretary Functions</h2>

    <a href="courses">View All Courses</a>

    <a href="courses-professors">View Courses & Professors</a>
	
	<a href="addCourse.html">Add Course</a>

    <a href="assignProfessor.html">Assign Professor</a>

    <a href="logout" class="logout-button">Logout</a>

</div>

</body>
</html>