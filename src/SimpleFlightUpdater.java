import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class demonstrates the use of PreparedStatement to prevent SQL Injection.
 *
 * Previous code used a standard Statement for executing an SQL query, which is vulnerable
 * to SQL Injection because it constructs SQL statements by directly concatenating user input
 * with the SQL string. This allows an attacker to inject malicious SQL code that could lead
 * to unauthorized data access or manipulation.
 *
 * PreparedStatement prevents SQL Injection by using parameterized queries. It treats all inputs
 * as data and not executable SQL code, thereby preventing attackers from injecting malicious code.
 */
public class SimpleFlightUpdater {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
        String user = "root";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter MySQL root password: ");
            char[] passwordChars = reader.readLine().toCharArray();
            String password = new String(passwordChars);

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE Arrivals SET delay_status = ? WHERE flight_number = 'LX354'")) {

                System.out.print("Enter a new delay status for flight 'LX354': ");
                String userInput = reader.readLine();

                // Set the parameter for the PreparedStatement
                stmt.setString(1, userInput);

                int rowsAffected = stmt.executeUpdate();
                System.out.println("Number of affected rows: " + rowsAffected);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}