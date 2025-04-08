package com.group04.DAO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

 public class SqlRunner {
    private static final String URL = "jdbc:mysql://localhost:3306"; // no DB in URL to allow `CREATE DATABASE`
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void runSQLFile(String filePath) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] statements = sql.split(";"); // Split queries on semicolon

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                for (String statement : statements) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        stmt.execute(statement);
                        System.out.println("✅ Executed: " + statement);
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ Database error:");
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("❌ Could not read SQL file:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runSQLFile("jobportal\\Resources\\createdb.sql"); // Path to your .sql file
        runSQLFile("jobportal\\Resources\\User_roles.sql");
        runSQLFile("jobportal\\Resources\\company.sql");
        runSQLFile("jobportal\\Resources\\Users.sql");
        runSQLFile("jobportal\\Resources\\Application_Status.sql");
        runSQLFile("jobportal\\Resources\\Applications_User.sql");
        runSQLFile("jobportal\\Resources\\Recruiters_Applications.sql");
        //runSQLFile("jobportal\\Resources\\deletedb.sql");
    }

}
    







//  public class JdbcConnector {
//     // Database credentials
//     private static final String URL = "jdbc:mysql://localhost:3306/job_portal"; // Replace with your DB name
//     private static final String USER = "root"; // Your MySQL username
//     private static final String PASSWORD = "root"; // Your MySQL password

//     public static Connection getConnection() {
//         Connection conn = null;
//         try {
//             // Load MySQL JDBC Driver
//             Class.forName("com.mysql.cj.jdbc.Driver");

//             // Establish the connection
//             conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             System.out.println("Connected to MySQL Database!");
//         } catch (ClassNotFoundException e) {
//             System.out.println("JDBC Driver not found!");
//             e.printStackTrace();
//         } catch (SQLException e) {
//             System.out.println("Connection failed! Check database credentials.");
//             e.printStackTrace();
//         }
//         return conn;
//     }

//     public static void fetchData() {
//         Connection conn = getConnection();
//         if (conn == null) {
//             System.out.println("❌ Database connection failed.");
//             return;
//         }

//         String query = "SELECT * FROM users"; // SQL Query

//         try (Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery(query)) {

//             System.out.println("📄 User Data:");
//             System.out.println("----------------------------");

//             while (rs.next()) {
//                 int id = rs.getInt("id");
//                 String name = rs.getString("name");
//                 //String email = rs.getString("email");
//                 System.out.println("ID: " + id + " | Name: " + name );
//             }

//             System.out.println("----------------------------");
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         fetchData(); // Test the connection
//     }
// }
