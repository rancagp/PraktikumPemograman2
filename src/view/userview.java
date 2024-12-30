package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class userview extends JFrame {
    private JTextField txtName = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JButton btnAdd = new JButton("Add User");
    private JButton btnRefresh = new JButton("Refresh");
    private JButton btnExport = new JButton("Export PDF");
    private JList<String> userList = new JList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public userview() {
        setTitle("User Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnExport);

        // User list panel
        JPanel listPanel = new JPanel(new BorderLayout());
        userList.setModel(listModel);
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("User List"));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Main layout
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(listPanel, BorderLayout.SOUTH);

        // UI visibility
        setVisible(true);
    }

    // Getters for input fields
    public String getNameInput() {
        return txtName.getText();
    }

    public String getEmailInput() {
        return txtEmail.getText();
    }

    // Update user list
    public void setUserList(String[] users) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (String user : users) {
                listModel.addElement(user);
            }
        });
    }

    // Add listeners for buttons
    public void addAddUserListener(ActionListener listener) {
        btnAdd.addActionListener(listener);
    }

    public void addRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }

    public void addExportListener(ActionListener listener) {
        btnExport.addActionListener(listener);
    }
}
