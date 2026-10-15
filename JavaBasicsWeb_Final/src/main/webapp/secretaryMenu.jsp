<%@ page contentType="text/html;charset=UTF-8" %>

<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

String role = (String) session.getAttribute("role");
String username = (String) session.getAttribute("username");

if (role == null || !"secretary".equals(role)) {
    response.sendRedirect("index.html");
    return;
}
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Administration Dashboard</title>

    <link rel="stylesheet"
          href="style.css">

    <style>

        body {
            padding: 28px;
        }

        .dashboard-shell {
            width: min(1380px, 100%);
            margin: 0 auto;

            display: grid;
            grid-template-columns: 270px 1fr;

            min-height: calc(100vh - 56px);

            background: #f8fafc;

            border-radius: 28px;
            overflow: hidden;

            box-shadow:
                0 30px 90px
                rgba(0,0,0,0.30);
        }

        .sidebar {
            display: flex;
            flex-direction: column;

            padding: 30px 22px;

            color: white;

            background:
                radial-gradient(
                    circle at 20% 10%,
                    rgba(255,255,255,0.12),
                    transparent 20%
                ),
                linear-gradient(
                    180deg,
                    #0f766e,
                    #0891b2
                );
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 12px;

            margin-bottom: 45px;

            font-weight: 850;
        }

        .brand-logo {
            width: 42px;
            height: 42px;

            display: flex;
            align-items: center;
            justify-content: center;

            border-radius: 13px;

            background:
                rgba(255,255,255,0.16);

            border:
                1px solid
                rgba(255,255,255,0.22);

            font-weight: 900;
        }

        .sidebar-label {
            margin:
                0 10px 12px;

            color:
                rgba(255,255,255,0.55);

            font-size: 11px;
            font-weight: 850;

            letter-spacing: 1.3px;

            text-transform: uppercase;
        }

        .nav-item {
            display: flex;
            align-items: center;
            gap: 12px;

            min-height: 46px;

            margin: 3px 0;
            padding: 10px 13px;

            border: none;
            border-radius: 12px;

            background: transparent;

            color:
                rgba(255,255,255,0.84);

            box-shadow: none;

            font-size: 13px;
            font-weight: 700;
        }

        .nav-item:hover,
        .nav-item.active {
            color: white;

            background:
                rgba(255,255,255,0.14);

            transform: none;
            box-shadow: none;
        }

        .nav-icon {
            width: 28px;

            text-align: center;

            font-size: 16px;
        }

        .sidebar-bottom {
            margin-top: auto;
        }

        .logout-link:hover {
            background:
                rgba(239,68,68,0.22);
        }


        .dashboard-main {
            padding: 38px 42px 46px;
        }

        .topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;

            gap: 20px;

            margin-bottom: 30px;
        }

        .page-label {
            margin-bottom: 7px;

            color: #0f766e;

            font-size: 12px;
            font-weight: 850;

            letter-spacing: 1.2px;

            text-transform: uppercase;
        }

        .topbar h1 {
            margin: 0;

            text-align: left;

            font-size: 34px;
            letter-spacing: -1px;
        }

        .topbar h1::after {
            display: none;
        }

        .user-box {
            display: flex;
            align-items: center;
            gap: 12px;

            padding: 9px 13px;

            border:
                1px solid #e4e7ec;

            border-radius: 15px;

            background: white;

            box-shadow:
                0 5px 18px
                rgba(15,23,42,0.05);
        }

        .avatar {
            width: 40px;
            height: 40px;

            display: flex;
            align-items: center;
            justify-content: center;

            border-radius: 12px;

            color: white;

            background:
                linear-gradient(
                    135deg,
                    #0f766e,
                    #0891b2
                );

            font-weight: 900;
        }

        .user-info strong {
            display: block;

            color: #101828;

            font-size: 14px;
        }

        .user-info span {
            color: #98a2b3;

            font-size: 12px;
        }


        .welcome-card {
            position: relative;

            overflow: hidden;

            padding: 30px 32px;

            border-radius: 22px;

            color: white;

            background:
                linear-gradient(
                    135deg,
                    #0f766e,
                    #0891b2
                );

            box-shadow:
                0 18px 45px
                rgba(8,145,178,0.18);
        }

        .welcome-card::before {
            content: "";

            position: absolute;

            width: 260px;
            height: 260px;

            right: -70px;
            top: -120px;

            border-radius: 50%;

            background:
                rgba(255,255,255,0.11);
        }

        .welcome-card h2 {
            position: relative;
            z-index: 2;

            margin: 0 0 8px;
            padding: 0;

            border: none;

            color: white;

            font-size: 26px;
        }

        .welcome-card p {
            position: relative;
            z-index: 2;

            margin: 0;

            max-width: 700px;

            color:
                rgba(255,255,255,0.82);
        }


        .section-header {
            margin:
                30px 0 16px;
        }

        .section-header h2 {
            margin: 0 0 5px;

            padding: 0;
            border: none;

            color: #101828;

            font-size: 20px;
        }

        .section-header p {
            margin: 0;

            color: #98a2b3;

            font-size: 13px;
        }


        .action-grid {
            display: grid;

            grid-template-columns:
                repeat(3, minmax(0,1fr));

            gap: 15px;
        }

        .action-card {
            display: flex;
            flex-direction: column;

            min-height: 175px;

            margin: 0;
            padding: 20px;

            border:
                1px solid #e4e7ec;

            border-radius: 17px;

            background: white;

            color: #101828;

            box-shadow:
                0 7px 22px
                rgba(15,23,42,0.05);
        }

        .action-card:hover {
            color: #101828;

            background: white;

            border-color: #99f6e4;

            transform:
                translateY(-4px);

            box-shadow:
                0 16px 35px
                rgba(13,148,136,0.11);
        }

        .card-top {
            display: flex;
            align-items: center;
            justify-content: space-between;

            margin-bottom: 20px;
        }

        .card-icon {
            width: 43px;
            height: 43px;

            display: flex;
            align-items: center;
            justify-content: center;

            border-radius: 13px;

            background: #f0fdfa;

            font-size: 20px;
        }

        .arrow {
            color: #98a2b3;

            font-size: 17px;
        }

        .action-card h3 {
            margin: 0 0 7px;

            font-size: 15px;
        }

        .action-card p {
            margin: 0;

            color: #667085;

            font-size: 12px;
            line-height: 1.5;
        }


        .dashboard-footer {
            display: flex;
            justify-content: space-between;

            margin-top: 32px;
            padding-top: 20px;

            border-top:
                1px solid #eaecf0;

            color: #98a2b3;

            font-size: 12px;
        }


        @media (max-width: 1050px) {

            .action-grid {
                grid-template-columns:
                    repeat(2,1fr);
            }
        }


        @media (max-width: 800px) {

            .dashboard-shell {
                grid-template-columns: 1fr;
            }

            .sidebar {
                display: none;
            }

            .dashboard-main {
                padding: 28px 20px;
            }

            .topbar {
                align-items: flex-start;
                flex-direction: column;
            }

            .action-grid {
                grid-template-columns: 1fr;
            }
        }

    </style>

</head>

<body>

<div class="dashboard-shell">


    <aside class="sidebar">

        <div class="brand">

            <div class="brand-logo">
                U
            </div>

            <span>
                University<br>
                Management
            </span>

        </div>


        <div class="sidebar-label">
            Administration
        </div>


        <a class="nav-item active"
           href="<%= response.encodeURL("secretaryMenu.jsp") %>">

            <span class="nav-icon">⌂</span>

            Dashboard

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("students") %>">

            <span class="nav-icon">♟</span>

            Students

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("professors") %>">

            <span class="nav-icon">♜</span>

            Professors

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("courses") %>">

            <span class="nav-icon">▤</span>

            Courses

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("enrollments") %>">

            <span class="nav-icon">⇄</span>

            Enrollments

        </a>


        <div class="sidebar-bottom">

            <a class="nav-item logout-link"
               href="<%= response.encodeURL("logout") %>">

                <span class="nav-icon">↪</span>

                Logout

            </a>

        </div>

    </aside>



    <main class="dashboard-main">


        <header class="topbar">

            <div>

                <div class="page-label">
                    Administration Dashboard
                </div>

                <h1>
                    University Management
                </h1>

            </div>


            <div class="user-box">

                <div class="avatar">
                    <%= username.substring(0,1).toUpperCase() %>
                </div>

                <div class="user-info">

                    <strong>
                        <%= username %>
                    </strong>

                    <span>
                        Secretary Account
                    </span>

                </div>

            </div>

        </header>



        <section class="welcome-card">

            <h2>
                Welcome, <%= username %>
            </h2>

            <p>
                Manage academic records, users,
                courses, enrollments and professor
                assignments from one centralized dashboard.
            </p>

        </section>



        <!-- CREATE -->

        <div class="section-header">

            <h2>
                Create Records
            </h2>

            <p>
                Add new users and academic data.
            </p>

        </div>


        <section class="action-grid">


            <a class="action-card"
               href="<%= response.encodeURL("addStudent.html") %>">

                <div class="card-top">

                    <div class="card-icon">
                        🎓
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    Add Student
                </h3>

                <p>
                    Register a new student
                    in the university system.
                </p>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("addProfessor.html") %>">

                <div class="card-top">

                    <div class="card-icon">
                        👨‍🏫
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    Add Professor
                </h3>

                <p>
                    Create a new professor
                    account and academic profile.
                </p>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("addCourse.html") %>">

                <div class="card-top">

                    <div class="card-icon">
                        📘
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    Add Course
                </h3>

                <p>
                    Create a new university
                    course and semester record.
                </p>

            </a>

        </section>



        <!-- ACADEMIC MANAGEMENT -->

        <div class="section-header">

            <h2>
                Academic Management
            </h2>

            <p>
                Manage enrollments and course assignments.
            </p>

        </div>


        <section class="action-grid">


            <a class="action-card"
               href="<%= response.encodeURL("addStudentToCourse.html") %>">

                <div class="card-top">

                    <div class="card-icon">
                        🔗
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    Add Student to Course
                </h3>

                <p>
                    Enroll a registered student
                    into an existing course.
                </p>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("assignProfessor.jsp") %>">

                <div class="card-top">

                    <div class="card-icon">
                        🧑‍🏫
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    Assign Professor
                </h3>

                <p>
                    Assign a responsible professor
                    to a university course.
                </p>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("enrollments") %>">

                <div class="card-top">

                    <div class="card-icon">
                        📋
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    View Enrollments
                </h3>

                <p>
                    Review student-to-course
                    enrollment records.
                </p>

            </a>

        </section>



        <!-- RECORDS -->

        <div class="section-header">

            <h2>
                University Records
            </h2>

            <p>
                Browse academic information stored in the database.
            </p>

        </div>


        <section class="action-grid">


            <a class="action-card"
               href="<%= response.encodeURL("students") %>">

                <div class="card-top">

                    <div class="card-icon">
                        👥
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    View All Students
                </h3>

                <p>
                    Browse registered students
                    and registration numbers.
                </p>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("professors") %>">

                <div class="card-top">

                    <div class="card-icon">
                        🧑‍💼
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    View All Professors
                </h3>

                <p>
                    Browse professor profiles
                    and professor numbers.
                </p>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("courses") %>">

                <div class="card-top">

                    <div class="card-icon">
                        📚
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    View All Courses
                </h3>

                <p>
                    Review course codes,
                    titles and semesters.
                </p>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("courses-professors") %>">

                <div class="card-top">

                    <div class="card-icon">
                        🏫
                    </div>

                    <span class="arrow">
                        →
                    </span>

                </div>

                <h3>
                    Courses & Professors
                </h3>

                <p>
                    Review responsible professors
                    assigned to each course.
                </p>

            </a>

        </section>



        <footer class="dashboard-footer">

            <span>
                University Management System
            </span>

            <span>
                Administration · Secure Session
            </span>

        </footer>

    </main>

</div>

</body>
</html>