<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

String role = (String) session.getAttribute("role");
String username = (String) session.getAttribute("username");

if (role == null || !"professor".equals(role)) {
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

    <title>Professor Dashboard</title>

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
                    rgba(255,255,255,0.14),
                    transparent 20%
                ),
                linear-gradient(
                    180deg,
                    #4338ca,
                    #7c3aed
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
            margin: 0 10px 12px;

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

            border: none;
            border-radius: 13px;

            background: transparent;

            box-shadow: none;

            color:
                rgba(255,255,255,0.85);

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

        .dashboard-main {
            padding: 38px 42px 46px;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;

            gap: 20px;

            margin-bottom: 36px;
        }

        .page-label {
            margin-bottom: 7px;

            color: #7c3aed;

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
                    #4f46e5,
                    #7c3aed
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

            margin-bottom: 30px;

            padding: 32px;

            border-radius: 23px;

            color: white;

            background:
                linear-gradient(
                    135deg,
                    #4f46e5,
                    #7c3aed
                );

            box-shadow:
                0 18px 45px
                rgba(124,58,237,0.20);
        }

        .welcome-card::before {
            content: "";

            position: absolute;

            width: 240px;
            height: 240px;

            right: -65px;
            top: -100px;

            border-radius: 50%;

            background:
                rgba(255,255,255,0.10);
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
        }

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
                repeat(2, minmax(0,1fr));

            gap: 18px;
        }

        .action-card {
            display: flex;
            flex-direction: column;

            min-height: 225px;

            margin: 0;
            padding: 26px;

            background: white;

            border:
                1px solid #e4e7ec;

            border-radius: 19px;

            color: #101828;

            box-shadow:
                0 8px 25px
                rgba(15,23,42,0.055);
        }

        .action-card:hover {
            color: #101828;

            background: white;

            border-color: #ddd6fe;

            transform: translateY(-5px);

            box-shadow:
                0 18px 40px
                rgba(124,58,237,0.12);
        }

        .card-icon {
            width: 50px;
            height: 50px;

            display: flex;
            align-items: center;
            justify-content: center;

            margin-bottom: 25px;

            border-radius: 14px;

            background: #f5f3ff;

            font-size: 23px;
        }

        .action-card h3 {
            margin: 0 0 8px;

            font-size: 18px;
        }

        .action-card p {
            margin: 0 0 22px;

            color: #667085;

            font-size: 13px;
            line-height: 1.55;
        }

        .card-action {
            margin-top: auto;

            color: #7c3aed;

            font-size: 13px;
            font-weight: 850;
        }

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
            Professor Portal
        </div>


        <a class="nav-item active"
           href="<%= response.encodeURL("professorMenu.jsp") %>">

            <span class="nav-icon">⌂</span>

            Dashboard

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("professor-grades") %>">

            <span class="nav-icon">▤</span>

            View Grades

        </a>


        <a class="nav-item"
           href="<%= response.encodeURL("professor-enter-grades") %>">

            <span class="nav-icon">✎</span>

            Enter Grades

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
                    Professor Dashboard
                </div>

                <h1>
                    Course Management
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
                        Professor Account
                    </span>

                </div>

            </div>

        </header>



        <section class="welcome-card">

            <h2>
                Welcome, Professor <%= username %>
            </h2>

            <p>
                Review grades for your assigned courses
                and enter results for students who have
                not yet received a grade.
            </p>

        </section>



        <div class="section-header">

            <h2>
                Teaching Services
            </h2>

            <p>
                Manage course grading from your dashboard.
            </p>

        </div>



        <section class="action-grid">


            <a class="action-card"
               href="<%= response.encodeURL("professor-grades") %>">

                <div class="card-icon">
                    📊
                </div>

                <h3>
                    View Course Grades
                </h3>

                <p>
                    Select one of your assigned courses
                    and review students whose grades
                    have already been recorded.
                </p>

                <div class="card-action">
                    Review grades →
                </div>

            </a>



            <a class="action-card"
               href="<%= response.encodeURL("professor-enter-grades") %>">

                <div class="card-icon">
                    ✏️
                </div>

                <h3>
                    Enter Student Grades
                </h3>

                <p>
                    View enrolled students who have
                    not yet been graded and securely
                    submit their academic results.
                </p>

                <div class="card-action">
                    Enter grades →
                </div>

            </a>

        </section>



        <footer class="dashboard-footer">

            <span>
                University Management System
            </span>

            <span>
                Professor Portal · Secure Session
            </span>

        </footer>

    </main>

</div>

</body>
</html>