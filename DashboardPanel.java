package com.myjobtracker.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DashboardPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JButton btnEdit;
    private JButton btnDelete;
    
    // Formatter for displaying dates in MM-DD-YYYY format.
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public DashboardPanel() {
        setLayout(new BorderLayout());
        
        // Updated column headers to include "Notes".
        String[] columnNames = {"ID", "Company", "Job Title", "Application Date", "Status", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable.
            }
        };
        table = new JTable(tableModel);
        
        // Enable sorting with a TableRowSorter.
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        // Set a custom comparator for the "Application Date" column (index 3).
        sorter.setComparator(3, (String s1, String s2) -> {
            try {
                LocalDate d1 = LocalDate.parse(s1, DISPLAY_FORMATTER);
                LocalDate d2 = LocalDate.parse(s2, DISPLAY_FORMATTER);
                return d1.compareTo(d2);
            } catch (DateTimeParseException e) {
                return s1.compareTo(s2);
            }
        });
        table.setRowSorter(sorter);
        
        btnRefresh = new JButton("Refresh Data");
        btnEdit = new JButton("Edit Selected");
        btnDelete = new JButton("Delete Selected");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void updateTable(Object[][] data) {
        tableModel.setRowCount(0);  // Clear existing rows.
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    public JButton getRefreshButton() {
        return btnRefresh;
    }
    
    public JButton getEditButton() {
        return btnEdit;
    }
    
    public JButton getDeleteButton() {
        return btnDelete;
    }
    
    public JTable getTable() {
        return table;
    }
}
