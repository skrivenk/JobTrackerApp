package com.myjobtracker.app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationDAO {

    // CREATE: Add a new job application
    public static boolean addJobApplication(JobApplication app) {
        String sql = "INSERT INTO job_applications (company_name, job_title, application_date, status, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, app.getCompany());
            pstmt.setString(2, app.getJobTitle());
            // Convert LocalDate to SQL Date
            pstmt.setDate(3, java.sql.Date.valueOf(app.getApplicationDate()));
            pstmt.setString(4, app.getStatus());
            pstmt.setString(5, app.getNotes());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting job application: " + e.getMessage());
            return false;
        }
    }

    // READ: Retrieve all job applications
    public static List<JobApplication> getJobApplications() {
        List<JobApplication> apps = new ArrayList<>();
        String sql = "SELECT * FROM job_applications ORDER BY application_date DESC";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                JobApplication app = new JobApplication();
                app.setId(rs.getInt("id"));
                app.setCompany(rs.getString("company_name"));
                app.setJobTitle(rs.getString("job_title"));
                // Convert SQL Date to LocalDate
                app.setApplicationDate(rs.getDate("application_date").toLocalDate());
                app.setStatus(rs.getString("status"));
                app.setNotes(rs.getString("notes"));
                apps.add(app);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving job applications: " + e.getMessage());
        }
        return apps;
    }

    // UPDATE: Update an existing job application
    public static boolean updateJobApplication(JobApplication app) {
        String sql = "UPDATE job_applications SET company_name = ?, job_title = ?, application_date = ?, status = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, app.getCompany());
            pstmt.setString(2, app.getJobTitle());
            pstmt.setDate(3, java.sql.Date.valueOf(app.getApplicationDate()));
            pstmt.setString(4, app.getStatus());
            pstmt.setString(5, app.getNotes());
            pstmt.setInt(6, app.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating job application: " + e.getMessage());
            return false;
        }
    }

    // DELETE: Delete a job application by its ID
    public static boolean deleteJobApplication(int id) {
        String sql = "DELETE FROM job_applications WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting job application: " + e.getMessage());
            return false;
        }
    }

    // SEARCH: Retrieve job applications matching a query in company_name or job_title
    public static List<JobApplication> searchJobApplications(String query) {
        List<JobApplication> apps = new ArrayList<>();
        String sql = "SELECT * FROM job_applications WHERE company_name ILIKE ? OR job_title ILIKE ? ORDER BY application_date DESC";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JobApplication app = new JobApplication();
                    app.setId(rs.getInt("id"));
                    app.setCompany(rs.getString("company_name"));
                    app.setJobTitle(rs.getString("job_title"));
                    app.setApplicationDate(rs.getDate("application_date").toLocalDate());
                    app.setStatus(rs.getString("status"));
                    app.setNotes(rs.getString("notes"));
                    apps.add(app);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching job applications: " + e.getMessage());
        }
        return apps;
    }

    // NEW: Retrieve a single job application by its ID
    public static JobApplication getJobApplicationById(int id) {
        String sql = "SELECT * FROM job_applications WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    JobApplication app = new JobApplication();
                    app.setId(rs.getInt("id"));
                    app.setCompany(rs.getString("company_name"));
                    app.setJobTitle(rs.getString("job_title"));
                    app.setApplicationDate(rs.getDate("application_date").toLocalDate());
                    app.setStatus(rs.getString("status"));
                    app.setNotes(rs.getString("notes"));
                    return app;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving job application by ID: " + e.getMessage());
        }
        return null;
    }
}
