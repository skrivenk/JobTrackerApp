package com.myjobtracker.app;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainApp {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panel instances
    private DashboardPanel dashboardPanel;
    private AddEditPanel addEditPanel;
    private SearchPanel searchPanel;
    private ReportsPanel reportsPanel;

    public MainApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Job Tracker App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Create the menu bar with File menu.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exportCsvItem = new JMenuItem("Export CSV");
        exportCsvItem.addActionListener(e -> exportToCSV());
        fileMenu.add(exportCsvItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // Create a navigation toolbar with icon buttons.
        JToolBar navToolBar = new JToolBar();
        navToolBar.setFloatable(false);
        navToolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnDashboard = new JButton(new ImageIcon(getClass().getResource("/icons/dashboard.png")));
        btnDashboard.setToolTipText("Dashboard");
        JButton btnAddEdit = new JButton(new ImageIcon(getClass().getResource("/icons/add_edit.png")));
        btnAddEdit.setToolTipText("Add/Edit Application");
        JButton btnSearch = new JButton(new ImageIcon(getClass().getResource("/icons/search.png")));
        btnSearch.setToolTipText("Search/Filter");
        JButton btnReports = new JButton(new ImageIcon(getClass().getResource("/icons/reports.png")));
        btnReports.setToolTipText("Reports/Notifications");
        btnDashboard.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        btnAddEdit.addActionListener(e -> {
            addEditPanel.clearFields();
            cardLayout.show(mainPanel, "AddEdit");
        });
        btnSearch.addActionListener(e -> cardLayout.show(mainPanel, "Search"));
        btnReports.addActionListener(e -> cardLayout.show(mainPanel, "Reports"));
        navToolBar.add(btnDashboard);
        navToolBar.add(btnAddEdit);
        navToolBar.add(btnSearch);
        navToolBar.add(btnReports);

        // Set up CardLayout for the main panel.
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        dashboardPanel = new DashboardPanel();
        addEditPanel = new AddEditPanel();
        searchPanel = new SearchPanel();
        reportsPanel = new ReportsPanel();

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(addEditPanel, "AddEdit");
        mainPanel.add(searchPanel, "Search");
        mainPanel.add(reportsPanel, "Reports");

        // Attach event handlers.

        // Save button in Add/Edit panel.
        addEditPanel.addSaveActionListener(e -> {
            JobApplication app = new JobApplication(
                    addEditPanel.getCompany(),
                    addEditPanel.getJobTitle(),
                    addEditPanel.getApplicationDate(),  // Returns a LocalDate
                    addEditPanel.getStatus(),
                    addEditPanel.getNotes()
            );
            boolean success;
            if (addEditPanel.getCurrentId() != null) {
                app.setId(addEditPanel.getCurrentId());
                success = JobApplicationDAO.updateJobApplication(app);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Job application updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update the job application.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                success = JobApplicationDAO.addJobApplication(app);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Job application saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to save the job application.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            addEditPanel.clearFields();
        });

        // Refresh button in Dashboard panel.
        dashboardPanel.getRefreshButton().addActionListener(e -> {
            List<JobApplication> apps = JobApplicationDAO.getJobApplications();
            Object[][] data = new Object[apps.size()][6];
            for (int i = 0; i < apps.size(); i++) {
                JobApplication app = apps.get(i);
                data[i][0] = app.getId();
                data[i][1] = app.getCompany();
                data[i][2] = app.getJobTitle();
                data[i][3] = (app.getApplicationDate() != null) ? app.getApplicationDate().format(DashboardPanel.DISPLAY_FORMATTER) : "";
                data[i][4] = app.getStatus();
                String notes = app.getNotes();
                if (notes != null && notes.length() > 30) {
                    notes = notes.substring(0, 30) + "...";
                }
                data[i][5] = notes;
            }
            dashboardPanel.updateTable(data);
        });

        // Search button in Search panel.
        searchPanel.getSearchButton().addActionListener(e -> {
            String query = searchPanel.getSearchQuery();
            List<JobApplication> apps = JobApplicationDAO.searchJobApplications(query);
            Object[][] data = new Object[apps.size()][6];
            for (int i = 0; i < apps.size(); i++) {
                JobApplication app = apps.get(i);
                data[i][0] = app.getId();
                data[i][1] = app.getCompany();
                data[i][2] = app.getJobTitle();
                data[i][3] = (app.getApplicationDate() != null) ? app.getApplicationDate().format(DashboardPanel.DISPLAY_FORMATTER) : "";
                data[i][4] = app.getStatus();
                String notes = app.getNotes();
                if (notes != null && notes.length() > 30) {
                    notes = notes.substring(0, 30) + "...";
                }
                data[i][5] = notes;
            }
            searchPanel.updateSearchResults(data);
        });

        // Edit button in Search panel.
        searchPanel.getEditButton().addActionListener(e -> {
            int id = searchPanel.getSelectedRowId();
            if (id == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a job application to edit from the search results.");
                return;
            }
            JobApplication app = JobApplicationDAO.getJobApplicationById(id);
            if (app == null) {
                JOptionPane.showMessageDialog(frame, "Could not retrieve the selected job application.");
                return;
            }
            addEditPanel.setJobApplication(app);
            cardLayout.show(mainPanel, "AddEdit");
        });

        // Clear button in Search panel.
        searchPanel.getClearButton().addActionListener(e -> {
            searchPanel.clearSearchResults();
        });

        // Edit button in Dashboard panel.
        dashboardPanel.getEditButton().addActionListener(e -> {
            int selectedRow = dashboardPanel.getTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a job application to edit.");
                return;
            }
            int id = (int) dashboardPanel.getTable().getValueAt(selectedRow, 0);
            JobApplication app = JobApplicationDAO.getJobApplicationById(id);
            if (app == null) {
                JOptionPane.showMessageDialog(frame, "Could not retrieve the selected job application.");
                return;
            }
            addEditPanel.setJobApplication(app);
            cardLayout.show(mainPanel, "AddEdit");
        });

        // Delete button in Dashboard panel.
        dashboardPanel.getDeleteButton().addActionListener(e -> {
            int selectedRow = dashboardPanel.getTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a job application to delete.");
                return;
            }
            int id = (int) dashboardPanel.getTable().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this application?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = JobApplicationDAO.deleteJobApplication(id);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Job application deleted successfully!");
                    dashboardPanel.getRefreshButton().doClick();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete the job application.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Automatically refresh the dashboard on startup.
        dashboardPanel.getRefreshButton().doClick();

        // Layout the frame: add the navigation toolbar at the top and the main panel in the center.
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(navToolBar, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    // Method to export job applications to a CSV file with a file chooser.
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose destination to export CSV");
        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
                writer.write("ID,Company,Job Title,Application Date,Status,Notes");
                writer.newLine();
                List<JobApplication> apps = JobApplicationDAO.getJobApplications();
                for (JobApplication app : apps) {
                    writer.write(app.getId() + ","
                        + escapeCsv(app.getCompany()) + ","
                        + escapeCsv(app.getJobTitle()) + ","
                        + ((app.getApplicationDate() != null) ? app.getApplicationDate().format(DashboardPanel.DISPLAY_FORMATTER) : "") + ","
                        + escapeCsv(app.getStatus()) + ","
                        + escapeCsv(app.getNotes()));
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(frame, "Data exported successfully to " + filePath);
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error exporting data: " + ex.getMessage());
            }
        }
    }
    
    // Helper method to escape CSV fields containing commas.
    private String escapeCsv(String field) {
        if (field == null) return "";
        if (field.contains(",")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp());
    }
}
