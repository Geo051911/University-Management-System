package mainpackage;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        String url = "jdbc:mysql://localhost:3306/javabasics_db";
        String user = "root";

        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                "Database password environment variable DB_PASSWORD is not set."
            );
        }

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
            url,
            user,
            password
        );
    }
}