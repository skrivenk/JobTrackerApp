package com.myjobtracker.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Absolute path for the embedded H2 database.
    // For example, using C:\ProgramData\JobTrackerApp\job_tracker
    private static final String URL = "jdbc:h2:file:C:/ProgramData/JobTrackerApp/job_tracker;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to embedded H2 database at " + URL);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }
}
