package view;

import dao.KaryawanDAO;
import dao.UserDAO;
import model.Karyawan;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

public class UserForm extends JFrame {
    private JComboBox<Karyawan> karyawanBox;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JComboBox<String> statusBox;
    private JButton submitButton;

    public UserForm(User u, Consumer<User> onSubmit) {
        setTitle("Tambah User");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Karyawan:"));
        List<Karyawan> karyawanList = KaryawanDAO.getKaryawanWithoutUser();
        karyawanBox = new JComboBox<>(karyawanList.toArray(new Karyawan[0]));
        add(karyawanBox);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"admin", "hr", "karyawan"});
        add(roleBox);

        add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"aktif", "resign"}); // ❗ hanya enum valid
        add(statusBox);

        submitButton = new JButton("Simpan");
        submitButton.addActionListener(e -> {
            User user = new User();
            user.setIdKaryawan(((Karyawan) karyawanBox.getSelectedItem()).getKaryawanId());
            user.setUsername(usernameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            user.setRole((String) roleBox.getSelectedItem());
            user.setStatus((String) statusBox.getSelectedItem());

            if (UserDAO.insert(user)) {
                JOptionPane.showMessageDialog(this, "User berhasil ditambahkan.");
                onSubmit.accept(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan user.");
            }
        });
        add(submitButton);
    }
}
