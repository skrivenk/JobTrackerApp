package com.myjobtracker.app;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AddEditPanel extends JPanel {
    private JTextField txtCompany;
    private JTextField txtJobTitle;
    private JDateChooser dateChooser; // Using JDateChooser for date selection
    private JComboBox<String> comboStatus;
    private JTextArea txtNotes;
    private JButton btnSave;
    
    // Holds the ID of the job application being edited (null for new entries)
    private Integer currentId = null;
    
    // Formatter for dates in MM-DD-YYYY format.
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    
    public AddEditPanel() {
        setLayout(new BorderLayout(10, 10));
        
        // Create a panel for the one-line fields using GridBagLayout.
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        
        // Row 0: Company Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Company Name:"), gbc);
        txtCompany = new JTextField();
        txtCompany.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        topPanel.add(txtCompany, gbc);
        
        // Row 1: Job Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Job Title:"), gbc);
        txtJobTitle = new JTextField();
        txtJobTitle.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        topPanel.add(txtJobTitle, gbc);
        
        // Row 2: Application Date using JDateChooser
        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(new JLabel("Application Date:"), gbc);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("MM-dd-yyyy");
        dateChooser.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        topPanel.add(dateChooser, gbc);
        
        // Row 3: Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        topPanel.add(new JLabel("Status:"), gbc);
        comboStatus = new JComboBox<>(new String[] {"Applied", "Interview", "Offer", "Rejected"});
        gbc.gridx = 1;
        topPanel.add(comboStatus, gbc);
        
        // Create a panel for the Notes field.
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notesPanel.add(new JLabel("Notes:"), BorderLayout.NORTH);
        txtNotes = new JTextArea(3, 20); // Multi-line for notes
        JScrollPane scrollPane = new JScrollPane(txtNotes);
        notesPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Combine the top fields and notes into a form panel.
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.add(topPanel, BorderLayout.NORTH);
        formPanel.add(notesPanel, BorderLayout.CENTER);
        
        // Create a panel for the Save button so it does not stretch full width.
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Save Application");
        btnPanel.add(btnSave);
        
        // Add the form panel in the center and the button panel at the bottom.
        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    // Getter methods for form values.
    public String getCompany() {
        return txtCompany.getText().trim();
    }
    
    public String getJobTitle() {
        return txtJobTitle.getText().trim();
    }
    
    // Returns a LocalDate parsed from the JDateChooser.
    public LocalDate getApplicationDate() {
        if (dateChooser.getDate() != null) {
            return dateChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return null;
    }
    
    public String getStatus() {
        return (String) comboStatus.getSelectedItem();
    }
    
    public String getNotes() {
        return txtNotes.getText().trim();
    }
    
    // Allow external classes (like MainApp) to attach an ActionListener to the Save button.
    public void addSaveActionListener(java.awt.event.ActionListener listener) {
        btnSave.addActionListener(listener);
    }
    
    // Returns the current job application ID (if editing an existing record).
    public Integer getCurrentId() {
        return currentId;
    }
    
    // Populates the form fields with data from the provided JobApplication.
    public void setJobApplication(JobApplication app) {
        if (app != null) {
            currentId = app.getId();
            txtCompany.setText(app.getCompany());
            txtJobTitle.setText(app.getJobTitle());
            if (app.getApplicationDate() != null) {
                // Convert LocalDate to java.util.Date
                java.util.Date date = java.util.Date.from(app.getApplicationDate()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                dateChooser.setDate(date);
            } else {
                dateChooser.setDate(null);
            }
            comboStatus.setSelectedItem(app.getStatus());
            txtNotes.setText(app.getNotes());
        }
    }
    
    // Clears all form fields and resets the current ID.
    public void clearFields() {
        currentId = null;
        txtCompany.setText("");
        txtJobTitle.setText("");
        dateChooser.setDate(null);
        comboStatus.setSelectedIndex(0);
        txtNotes.setText("");
    }
}
