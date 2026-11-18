import java.sql.*;
import java.util.Scanner;

public class FlightViewer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask for MySQL root password
        System.out.print("Enter MySQL root password: ");
        String password = scanner.nextLine();

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
        String user = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Ask for terminal number (valid: 2, 3, 4, 5)
            System.out.print("Enter terminal number (2, 3, 4, 5): ");
            int terminal = scanner.nextInt();
            if (terminal < 2 || terminal > 5) {
                throw new IllegalArgumentException("Invalid terminal number. Please enter a number between 2 and 5.");
            }

            // Create SQL query
            String query = "SELECT a.scheduled, a.flight_number, r.from_city, a.delay_status, a.terminal FROM Arrivals a INNER JOIN FlightRoutes r ON a.flight_number = r.flight_number WHERE a.terminal = ? ORDER BY a.scheduled ASC";

            // Execute the query using try-with-resources
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, String.valueOf(terminal));

                // Execute the query and process the results
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Print formatted table header
                    System.out.println(String.format("%-20s %-15s %-15s %-15s %-15s",
                            "Scheduled", "Flight Number", "From City", "Delay Status", "Terminal"));

                    // Print each row of results
                    while (rs.next()) {
                        String scheduled = rs.getString("scheduled");
                        String flightNumber = rs.getString("flight_number");
                        String fromCity = rs.getString("from_city");
                        String delayStatus = rs.getString("delay_status");
                        int terminalNum = rs.getInt("terminal");

                        System.out.println(String.format("%-20s %-15s %-15s %-15s %-15d",
                                scheduled, flightNumber, fromCity, delayStatus, terminalNum));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}