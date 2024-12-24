package controller;

import model.*;
import view.userview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class usercontroller {
    private userview view;
    private usermapper mapper;

    public usercontroller(userview view, usermapper mapper) {
        this.view = view;
        this.mapper = mapper;

        this.view.addAddUserListener(new AddUserListener());
        this.view.addRefreshListener(new RefreshListener());
    }

    class AddUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = view.getNameInput();
            String email = view.getEmailInput();
            if (!name.isEmpty() && !email.isEmpty()) {
                user user = new user();
                user.setName(name);
                user.setEmail(email);
                mapper.insertUser(user);
                JOptionPane.showMessageDialog(view, "User added successfully!");
            } else {
                JOptionPane.showMessageDialog(view, "Please fill in all fields.");
            }
        }
    }

    class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<user> users = mapper.getAllUsers();
            String[] userArray = users.stream()
                    .map(u -> u.getName() + " (" + u.getEmail() + ")")
                    .toArray(String[]::new);
            view.setUserList(userArray);
        }
    }
}