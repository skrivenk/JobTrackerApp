package com.myjobtracker.app;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

public class ReportsPanel extends JPanel {
    private JButton btnGenerateReport;
    private JTextArea textAreaReport;
    private JPanel chartContainer;  // Panel to hold the chart
    private JComboBox<String> timeFrameComboBox;
    private JComboBox<String> statusFilterComboBox;
    private JTextField companyFilterField;
    
    // Formatter for dates in MM-DD-YYYY format.
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public ReportsPanel() {
        setLayout(new BorderLayout());
        
        // Top panel for filter controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Time frame filter
        filterPanel.add(new JLabel("Time Frame:"));
        timeFrameComboBox = new JComboBox<>(new String[] {"All Time", "Past Week", "Past Month", "Past Year"});
        filterPanel.add(timeFrameComboBox);
        
        // Status filter
        filterPanel.add(new JLabel("Status:"));
        statusFilterComboBox = new JComboBox<>(new String[] {"All", "Applied", "Interview", "Offer", "Rejected"});
        filterPanel.add(statusFilterComboBox);
        
        // Company filter
        filterPanel.add(new JLabel("Company:"));
        companyFilterField = new JTextField(15);
        filterPanel.add(companyFilterField);
        
        // Generate Report button
        btnGenerateReport = new JButton("Generate Report");
        filterPanel.add(btnGenerateReport);
        
        // Create a non-editable text area for the summary report.
        textAreaReport = new JTextArea();
        textAreaReport.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(textAreaReport);
        
        // Panel to hold the chart.
        chartContainer = new JPanel(new BorderLayout());
        
        // Use a JSplitPane to divide the view between the text summary and the chart.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textScrollPane, chartContainer);
        splitPane.setResizeWeight(0.5);
        
        add(filterPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        // Attach event listener to generate the report.
        btnGenerateReport.addActionListener(e -> generateAndDisplayReport());
    }
    
    private void generateAndDisplayReport() {
        // Retrieve all applications from the database.
        List<JobApplication> allApps = JobApplicationDAO.getJobApplications();
        
        // Apply time frame filter.
        List<JobApplication> filteredApps = new ArrayList<>();
        String selectedTimeFrame = (String) timeFrameComboBox.getSelectedItem();
        LocalDate cutoff = null;
        if ("Past Week".equals(selectedTimeFrame)) {
            cutoff = LocalDate.now().minusDays(7);
        } else if ("Past Month".equals(selectedTimeFrame)) {
            cutoff = LocalDate.now().minusDays(30);
        } else if ("Past Year".equals(selectedTimeFrame)) {
            cutoff = LocalDate.now().minusDays(365);
        }
        if (cutoff != null) {
            for (JobApplication app : allApps) {
                LocalDate appDate = app.getApplicationDate();
                if (appDate != null && (appDate.isEqual(cutoff) || appDate.isAfter(cutoff))) {
                    filteredApps.add(app);
                }
            }
        } else {
            filteredApps = allApps;
        }
        
        // Apply status filter.
        String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
        List<JobApplication> statusFilteredApps = new ArrayList<>();
        if (!"All".equalsIgnoreCase(selectedStatus)) {
            for (JobApplication app : filteredApps) {
                String status = app.getStatus();
                if (status != null && status.trim().equalsIgnoreCase(selectedStatus)) {
                    statusFilteredApps.add(app);
                }
            }
        } else {
            statusFilteredApps = filteredApps;
        }
        
        // Apply company filter.
        String companyFilter = companyFilterField.getText().trim().toLowerCase();
        List<JobApplication> finalApps = new ArrayList<>();
        if (!companyFilter.isEmpty()) {
            for (JobApplication app : statusFilteredApps) {
                String company = app.getCompany();
                if (company != null && company.toLowerCase().contains(companyFilter)) {
                    finalApps.add(app);
                }
            }
        } else {
            finalApps = statusFilteredApps;
        }
        
        // Calculate statistics.
        int total = finalApps.size();
        int applied = 0, interview = 0, offer = 0, rejected = 0, unknown = 0;
        for (JobApplication app : finalApps) {
            String status = app.getStatus();
            String normalizedStatus = (status == null) ? "" : status.trim();
            if (normalizedStatus.isEmpty()) {
                unknown++;
            } else if (normalizedStatus.equalsIgnoreCase("Applied")) {
                applied++;
            } else if (normalizedStatus.equalsIgnoreCase("Interview")) {
                interview++;
            } else if (normalizedStatus.equalsIgnoreCase("Offer")) {
                offer++;
            } else if (normalizedStatus.equalsIgnoreCase("Rejected")) {
                rejected++;
            } else {
                unknown++;
            }
        }
        
        StringBuilder report = new StringBuilder();
        report.append("Job Application Report\n");
        report.append("========================\n\n");
        report.append("Time Frame: ").append(selectedTimeFrame).append("\n");
        report.append("Status Filter: ").append(selectedStatus).append("\n");
        report.append("Company Filter: ").append(companyFilter.isEmpty() ? "None" : companyFilter).append("\n\n");
        report.append("Total Applications: ").append(total).append("\n");
        report.append("Applied: ").append(applied).append("\n");
        report.append("Interview: ").append(interview).append("\n");
        report.append("Offer: ").append(offer).append("\n");
        report.append("Rejected: ").append(rejected).append("\n");
        if (unknown > 0) {
            report.append("Unknown: ").append(unknown).append("\n");
        }
        
        // Determine earliest and latest application dates.
        LocalDate earliest = null;
        LocalDate latest = null;
        for (JobApplication app : finalApps) {
            LocalDate date = app.getApplicationDate();
            if (date != null) {
                if (earliest == null || date.isBefore(earliest)) {
                    earliest = date;
                }
                if (latest == null || date.isAfter(latest)) {
                    latest = date;
                }
            }
        }
        if (earliest != null) {
            report.append("Earliest Application Date: ").append(earliest.format(DISPLAY_FORMATTER)).append("\n");
        }
        if (latest != null) {
            report.append("Latest Application Date: ").append(latest.format(DISPLAY_FORMATTER)).append("\n");
        }
        
        textAreaReport.setText(report.toString());
        
        // Create dataset for the chart.
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(applied, "Count", "Applied");
        dataset.addValue(interview, "Count", "Interview");
        dataset.addValue(offer, "Count", "Offer");
        dataset.addValue(rejected, "Count", "Rejected");
        dataset.addValue(unknown, "Count", "Unknown");
        
        // Create a bar chart.
        JFreeChart barChart = ChartFactory.createBarChart(
            "Job Applications by Status",  // Chart title
            "Status",                      // Category axis label
            "Count",                       // Value axis label
            dataset
        );
        
        // Enable anti-aliasing and set rendering hints for better quality.
        barChart.setAntiAlias(true);
        barChart.setTextAntiAlias(true);
        barChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        barChart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        barChart.getRenderingHints().put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        barChart.getRenderingHints().put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        // Use a high-quality font such as Segoe UI.
        barChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
        if (barChart.getLegend() != null) {
            barChart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 16));
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
        plot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 16));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        
        chartContainer.removeAll();
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        chartContainer.validate();
    }
}
