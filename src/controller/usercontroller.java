package controller;

import model.*;
import view.userPdf;
import view.userview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class usercontroller {
    private userview view;
    private usermapper mapper;
    private userPdf pdf;

    public usercontroller(userview view, usermapper mapper, userPdf pdf) {
        this.view = view;
        this.mapper = mapper;
        this.pdf = pdf;

        this.view.addAddUserListener(new AddUserListener());
        this.view.addRefreshListener(new RefreshListener());
        this.view.addExportListener(new ExportListener());
    }

    class AddUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            executor.submit(() -> {
                try {
                    String name = view.getNameInput();
                    String email = view.getEmailInput();

                    if (!name.isEmpty() && !email.isEmpty() && email.contains("@")) {
                        user user = new user();
                        user.setName(name);
                        user.setEmail(email);
                        mapper.insertUser(user);
                        JOptionPane.showMessageDialog(view, "User added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(view, "Please fill in all fields correctly.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error adding user: " + ex.getMessage());
                }
            });
            executor.shutdown();
        }
    }

    class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    List<user> users = mapper.getAllUsers();
                    String[] userArray = users.stream()
                            .map(u -> u.getName() + " (" + u.getEmail() + ")")
                            .toArray(String[]::new);
                    SwingUtilities.invokeLater(() -> view.setUserList(userArray));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error refreshing user list: " + ex.getMessage());
                }
            }).start();
        }
    }

    class ExportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    List<user> users = mapper.getAllUsers();
                    pdf.exportPdf(users);
                    JOptionPane.showMessageDialog(view, "PDF exported successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error exporting PDF: " + ex.getMessage());
                }
            }).start();
        }
    }
}
