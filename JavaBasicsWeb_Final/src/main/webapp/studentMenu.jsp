
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

String role = (String) session.getAttribute("role");
String username = (String) session.getAttribute("username");

if (role == null || !"student".equals(role)) {
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

    <title>Student Dashboard</title>

    <link rel="stylesheet"
          href="style.css">

    <style>

        body {
            padding: 28px;
        }

        .dashboard-shell {
            width: min(1280px, 100%);
            margin: 0 auto;

            display: grid;
            grid-template-columns: 260px 1fr;

            min-height: calc(100vh - 56px);

            background: #f8fafc;

            border-radius: 28px;
            overflow: hidden;

            box-shadow:
                0 30px 90px
                rgba(0, 0, 0, 0.30);
        }


        /* SIDEBAR */

        .sidebar {
            display: flex;
            flex-direction: column;

            padding: 30px 22px;

            color: white;

            background:
                radial-gradient(
                    circle at 20% 10%,
                    rgba(255,255,255,0.15),
                    transparent 20%
                ),
                linear-gradient(
                    180deg,
                    #1d4ed8,
                    #4338ca
                );
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 12px;

            margin-bottom: 50px;

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

            min-height: 52px;

            margin: 5px 0;
            padding: 13px 14px;

            color:
                rgba(255,255,255,0.85);

            background: transparent;

            border: none;
            border-radius: 13px;

            box-shadow: none;

            font-size: 14px;
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
            width: 30px;
            text-align: center;

            font-size: 18px;
        }

        .sidebar-bottom {
            margin-top: auto;
        }

        .logout-link:hover {
            background:
                rgba(239,68,68,0.22);
        }


        /* MAIN */

        .dashboard-main {
            padding: 38px 42px 46px;
            overflow: hidden;
        }

        .topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;

            gap: 20px;

            margin-bottom: 36px;
        }

        .page-label {
            margin-bottom: 7px;

            color: #2563eb;

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
                1px solid
                #e4e7ec;

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

            background:
                linear-gradient(
                    135deg,
                    #2563eb,
                    #4f46e5
                );

            color: white;

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


        /* HERO */

        .welcome-card {
            position: relative;

            overflow: hidden;

            margin-bottom: 30px;

            padding: 32px;

            border-radius: 23px;

            color: white;

            background:
                linear-gradient(
                    135deg,
                    #2563eb,
                    #4f46e5
                );

            box-shadow:
                0 18px 45px
                rgba(37,99,235,0.22);
        }

        .welcome-card::before {
            content: "";

            position: absolute;

            width: 230px;
            height: 230px;

            right: -60px;
            top: -90px;

            border-radius: 50%;

            background:
                rgba(255,255,255,0.10);
        }

        .welcome-card::after {
            content: "";

            position: absolute;

            width: 150px;
            height: 150px;

            right: 100px;
            bottom: -100px;

            border-radius: 50%;

            background:
                rgba(255,255,255,0.08);
        }

        .welcome-card h2 {
            position: relative;
            z-index: 2;

            margin: 0 0 10px;
            padding: 0;

            border: none;

            color: white;

            font-size: 27px;
        }

        .welcome-card p {
            position: relative;
            z-index: 2;

            max-width: 650px;

            margin: 0;

            color:
                rgba(255,255,255,0.82);

            line-height: 1.6;
        }


        /* ACTION CARDS */

        .section-header {
            margin:
                30px 0 18px;
        }

        .section-header h2 {
            margin: 0 0 5px;

            padding: 0;

            border: none;

            color: #101828;

            font-size: 21px;
        }

        .section-header p {
            margin: 0;

            color: #98a2b3;

            font-size: 14px;
        }

        .action-grid {
            display: grid;

            grid-template-columns:
                repeat(3, minmax(0, 1fr));

            gap: 18px;
        }

        .action-card {
            position: relative;

            display: flex;
            flex-direction: column;

            min-height: 210px;

            margin: 0;
            padding: 23px;

            overflow: hidden;

            background: white;

            border:
                1px solid
                #e4e7ec;

            border-radius: 19px;

            color: #101828;

            box-shadow:
                0 8px 25px
                rgba(15,23,42,0.055);
        }

        .action-card:hover {
            color: #101828;

            background: white;

            border-color:
                #bfdbfe;

            transform:
                translateY(-5px);

            box-shadow:
                0 18px 40px
                rgba(37,99,235,0.12);
        }

        .card-icon {
            width: 48px;
            height: 48px;

            display: flex;
            align-items: center;
            justify-content: center;

            margin-bottom: 25px;

            border-radius: 14px;

            background: #eff6ff;

            font-size: 23px;
        }

        .action-card h3 {
            margin: 0 0 8px;

            font-size: 17px;
        }

        .action-card p {
            margin: 0 0 22px;

            color: #667085;

            font-size: 13px;
            line-height: 1.55;
        }

        .card-action {
            margin-top: auto;

            color: #2563eb;

            font-size: 13px;
            font-weight: 850;
        }


        /* FOOTER */

        .dashboard-footer {
            display: flex;
            justify-content: space-between;

            margin-top: 35px;
            padding-top: 20px;

            border-top:
                1px solid #eaecf0;

            color: #98a2b3;

            font-size: 12px;
        }


        @media (max-width: 900px) {

            .dashboard-shell {
                grid-template-columns: 1fr;
            }

            .sidebar {
                display: none;
            }

            .action-grid {
                grid-template-columns: 1fr;
            }

            .dashboard-main {
                padding: 28px 20px;
            }

            .topbar {
                align-items: flex-start;
                flex-direction: column;
            }
        }

    </style>

</head>

<body>

<div class="dashboard-shell">


    <!-- SIDEBAR -->

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
            Student Portal
        </div>


        <a class="nav-item active"
           href="<%= response.encodeURL("studentMenu.jsp") %>">

            <span class="nav-icon">⌂</span>

            Dashboard

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("student-grades") %>">

            <span class="nav-icon">▤</span>

            My Grades

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("student-grades-course") %>">

            <span class="nav-icon">◫</span>

            By Course

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("student-grades-semester") %>">

            <span class="nav-icon">◷</span>

            By Semester

        </a>


        <div class="sidebar-bottom">

            <a class="nav-item logout-link"
               href="<%= response.encodeURL("logout") %>">

                <span class="nav-icon">↪</span>

                Logout

            </a>

        </div>

    </aside>



    <!-- MAIN -->

    <main class="dashboard-main">


        <header class="topbar">

            <div>

                <div class="page-label">
                    Student Dashboard
                </div>

                <h1>
                    Academic Overview
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
                        Student Account
                    </span>

                </div>

            </div>

        </header>



        <section class="welcome-card">

            <h2>
                Welcome back, <%= username %>
            </h2>

            <p>
                Your academic information is available
                from one place. Review your complete
                grade history or filter results by
                course and semester.
            </p>

        </section>



        <div class="section-header">

            <h2>
                Academic Services
            </h2>

            <p>
                Select an option to view your results.
            </p>

        </div>



        <section class="action-grid">


            <a class="action-card"
               href="<%= response.encodeURL("student-grades") %>">

                <div class="card-icon">
                    📊
                </div>

                <h3>
                    My Grades
                </h3>

                <p>
                    View the complete list of your
                    examined courses and grades.
                </p>

                <div class="card-action">
                    View all grades →
                </div>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("student-grades-course") %>">

                <div class="card-icon">
                    📚
                </div>

                <h3>
                    Grades by Course
                </h3>

                <p>
                    Select a specific course and
                    review your recorded result.
                </p>

                <div class="card-action">
                    Select course →
                </div>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("student-grades-semester") %>">

                <div class="card-icon">
                    🗓️
                </div>

                <h3>
                    Grades by Semester
                </h3>

                <p>
                    Filter your academic results
                    according to university semester.
                </p>

                <div class="card-action">
                    Select semester →
                </div>

            </a>

        </section>



        <footer class="dashboard-footer">

            <span>
                University Management System
            </span>

            <span>
                Student Portal · Secure Session
            </span>

        </footer>

    </main>

</div>

</body>
</html>