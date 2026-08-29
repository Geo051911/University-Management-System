package mainpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordSetup {

    public static void main(String[] args) {

        try {

            Connection connection =
                    DBConnection.getConnection();

            String selectSql =
                    "SELECT user_id, Username FROM users";

            PreparedStatement selectStatement =
                    connection.prepareStatement(selectSql);

            ResultSet results =
                    selectStatement.executeQuery();

            String updateSql =
                    "UPDATE users " +
                    "SET password_hash = ?, password_salt = ? " +
                    "WHERE user_id = ?";

            PreparedStatement updateStatement =
                    connection.prepareStatement(updateSql);

            while (results.next()) {

                int userId =
                        results.getInt("user_id");

                String username =
                        results.getString("Username");

                String password =
                        username + "123!";

                String salt =
                        PasswordUtil.generateSalt();

                String hash =
                        PasswordUtil.hashPassword(
                                password,
                                salt
                        );

                updateStatement.setString(1, hash);
                updateStatement.setString(2, salt);
                updateStatement.setInt(3, userId);

                updateStatement.executeUpdate();

                System.out.println(
                        "Password created for: " + username
                );
            }

            connection.close();

            System.out.println(
                    "All passwords created successfully!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }
}