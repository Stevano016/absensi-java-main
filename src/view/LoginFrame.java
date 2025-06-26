package view;

import dao.UserDAO;
import java.awt.*;
import javax.swing.*;
import model.User;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login Sistem Absensi");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 5));
        JTextField usernameField = new JTextField();
        
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        add(panel, BorderLayout.CENTER);
        add(loginBtn, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User user = UserDAO.login(username, password);

            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + user.getRole());
                dispose();

                switch (user.getRole()) {
                    case "admin" -> new AdminDashboard(user).setVisible(true);
                    case "hr" -> new HRDashboard(user).setVisible(true);
                    case "karyawan" -> new KaryawanDashboard(user).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal. Cek username atau password.");
            }
        });
    }
}
