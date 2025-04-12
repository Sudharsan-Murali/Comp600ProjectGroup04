package com.group04.GUI.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.group04.GUI.User.Components.UIUtils;
import com.group04.GUI.User.Components.BaseScreen;
import com.group04.GUI.User.Components.ButtonFactory;

public class JobSearchScreen extends BaseScreen {

    private String userEmail;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private String searchQuery;


    public JobSearchScreen(String email) {
        super("Job Search");
        this.userEmail = email;
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // North container (Title + Search)
        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));
        northContainer.add(UIUtils.createTitleLabel("Job Search", "src/Assets/Profile-Pic-Icon.png"));
        northContainer.add(createSearchPanel());

        // Main content
        mainContentPanel.add(northContainer, BorderLayout.NORTH);
        mainContentPanel.add(createJobContentSplitPane(), BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel searchLabel = new JLabel("Search Jobs:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = ButtonFactory.createSideButton("Search");
        JButton clearButton = ButtonFactory.createSideButton("Clear");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // Button actions
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
        clearButton.addActionListener(e -> clearSearch(searchField));

        return searchPanel;
    }

    private void performSearch(String searchQuery) {
        this.searchQuery = searchQuery;
        try {
            // Adjust your DB credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "your_user", "your_pass");
    
            String sql = "SELECT RA.Job_Title, C.Company_name, RA.Job_location, JT.Job_Type, RA.Date_Of_Application " +
                         "FROM Recruiters_Applications RA " +
                         //"JOIN Company C ON RA.Company_ID = C.Company_ID " + // Adjust if different foreign key
                         "JOIN Job_Type JT ON RA.Job_Type = JT.JobType_ID " +
                         "WHERE RA.Job_Title LIKE ?";
    
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchQuery + "%");
    
            ResultSet rs = stmt.executeQuery();
    
            tableModel.setRowCount(0); // Clear old data
    
            while (rs.next()) {
                String title = rs.getString("Job_Title");
                String company = rs.getString("Company_name");
                String location = rs.getString("Job_location");
                String remote = rs.getString("Job_Type");
                String date = rs.getString("Date_Of_Application");
    
                tableModel.addRow(new Object[]{title, company, location, remote, date});
            }
    
            rs.close();
            stmt.close();
            conn.close();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving jobs: " + ex.getMessage());
        }
    }

    private void clearSearch(JTextField searchField) {
        searchField.setText("");
    }

    private JSplitPane createJobContentSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Initialize the left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // Set layout for leftPanel
        leftPanel.setPreferredSize(new Dimension(350, 500));
        // leftPanel.add(new JLabel("Results will appear here..."));
        String[] columnNames = {"Title", "Company", "Location", "Remote", "Date Posted"};
        tableModel = new DefaultTableModel(columnNames, 0);
        jobTable = new JTable(tableModel);
        leftPanel.add(new JScrollPane(jobTable));

        


        // Right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        JTextArea jobDetailsArea = new JTextArea("Select a job to view details");
        rightPanel.add(new JScrollPane(jobDetailsArea), BorderLayout.CENTER);

        // Set the left and right components of the split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        return splitPane;
    }

    @Override
    protected void handleNavigation(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        dispose();

        switch (command) {
            case "Profile":
                SwingUtilities.invokeLater(() -> new UserProfileScreen(userEmail));
                break;
            case "Search Jobs":
                SwingUtilities.invokeLater(() -> new JobSearchScreen(userEmail));
                break;
            case "Applications":
                SwingUtilities.invokeLater(() -> new UserAppscreen());
                break;
            case "Logout":
                performLogout();
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void performLogout() {
        System.out.println("Logging out...");
        dispose();
    }

 


    // @Override
    // protected void handleNavigation(ActionEvent e) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'handleNavigation'");
    // }
}
