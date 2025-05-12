package com.myjobtracker.app;

import java.time.LocalDate;

public class JobApplication {
    private int id;
    private String company;
    private String jobTitle;
    private LocalDate applicationDate; // Using LocalDate for date values
    private String status;
    private String notes;

    public JobApplication() {
    }

    public JobApplication(String company, String jobTitle, LocalDate applicationDate, String status, String notes) {
        this.company = company;
        this.jobTitle = jobTitle;
        this.applicationDate = applicationDate;
        this.status = status;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public LocalDate getApplicationDate() {
        return applicationDate;
    }
    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
