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

if (role == null || !role.equals("professor")) {
    response.sendRedirect("index.html");
    return;
}

%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Professor Menu</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="container">

    <h1>Professor Menu</h1>

    <p>Welcome, <%= session.getAttribute("username") %></p>

    <h2>Professor Functions</h2>

    <a href="professor-grades">View Grades by Course</a>
	<a href="professor-enter-grades">Enter Grades</a>
	<a href="logout" class="logout-button">Logout</a>
</div>

</body>
</html>