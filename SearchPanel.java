package com.myjobtracker.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class SearchPanel extends JPanel {
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnEdit;
    private JButton btnClear;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    
    // Use the same date formatter.
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public SearchPanel() {
        setLayout(new BorderLayout());
        
        // Top panel for search input and control buttons.
        JPanel searchInputPanel = new JPanel(new FlowLayout());
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        btnEdit = new JButton("Edit");
        btnClear = new JButton("Clear");
        searchInputPanel.add(new JLabel("Search:"));
        searchInputPanel.add(txtSearch);
        searchInputPanel.add(btnSearch);
        searchInputPanel.add(btnEdit);
        searchInputPanel.add(btnClear);
        
        // Table for displaying search results, with an extra "Notes" column.
        String[] columnNames = {"ID", "Company", "Job Title", "Application Date", "Status", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);
        
        add(searchInputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
    }
    
    // Returns the search query string.
    public String getSearchQuery() {
        return txtSearch.getText().trim();
    }
    
    // Exposes the search button.
    public JButton getSearchButton() {
        return btnSearch;
    }
    
    // Exposes the edit button.
    public JButton getEditButton() {
        return btnEdit;
    }
    
    // Exposes the clear button.
    public JButton getClearButton() {
        return btnClear;
    }
    
    // Updates the search results table.
    public void updateSearchResults(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    // Returns the ID of the selected row in the search results, or -1 if none is selected.
    public int getSelectedRowId() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) resultsTable.getValueAt(selectedRow, 0);
        }
        return -1;
    }
    
    // Clears the search query and the table data.
    public void clearSearchResults() {
        txtSearch.setText("");
        tableModel.setRowCount(0);
    }
    
    // Expose the results table if needed.
    public JTable getTable() {
        return resultsTable;
    }
}
