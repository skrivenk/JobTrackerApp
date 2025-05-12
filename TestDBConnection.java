package com.myjobtracker.app;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            System.out.println("Database connection is successful!");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
