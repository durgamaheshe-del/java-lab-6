import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseDelete {
    // Database connection credentials
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement deleteStatement = null;

        try {
            // Step 1: Register and load the JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish the connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");

            // Step 3: Ensure table and test data exist
            statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS students ("
                    + "id INT PRIMARY KEY, "
                    + "name VARCHAR(50), "
                    + "branch VARCHAR(20)"
                    + ")";
            statement.execute(createTableSQL);

            // Insert sample row to ensure there is a record to delete
            String seedSQL = "INSERT IGNORE INTO students (id, name, branch) VALUES (101, 'Shaik Ameer', 'CSE')";
            statement.executeUpdate(seedSQL);

            // Step 4: Parameterized DELETE statement
            String deleteSQL = "DELETE FROM students WHERE id = ?";
            deleteStatement = connection.prepareStatement(deleteSQL);

            // Specify which record to delete
            int studentIdToDelete = 101;
            deleteStatement.setInt(1, studentIdToDelete);

            // Step 5: Execute deletion
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Record with ID " + studentIdToDelete + " was successfully deleted.");
            } else {
                System.out.println("No record found with ID " + studentIdToDelete + ".");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Ensure MySQL Connector JAR is added.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Step 6: Close JDBC resources to prevent memory leaks
            try {
                if (deleteStatement != null) deleteStatement.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                System.out.println("Database connection and resources closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
