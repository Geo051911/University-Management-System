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
    <title>Assign Professor</title>
    <link rel="stylesheet" href="style.css">
</head>

<body>

<div class="container">

    <h1>Assign Professor</h1>

    <form action="assign-professor" method="post">

        <label>Course Code</label>
        <input type="text"
               name="courseCode"
               required>

        <label>Professor Number</label>
        <input type="number"
               name="professorNumber"
               required>

        <button type="submit">
            Assign Professor
        </button>

    </form>

    <a href="secretaryMenu.jsp">
        Back to Secretary Menu
    </a>

</div>

</body>

</html>