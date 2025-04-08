package com.group04;

import java.sql.Connection;

import com.group04.DAO.DatabaseConnection;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connected to MySQL Database!");
            } else {
                System.out.println("❌ Connection failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
