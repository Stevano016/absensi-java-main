package view;

import model.Departemen;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class DepartemenForm extends JDialog {
    public DepartemenForm(Departemen existing, Consumer<Departemen> onSubmit) {
        setTitle(existing == null ? "Tambah Departemen" : "Edit Departemen");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 5, 5));
        setLocationRelativeTo(null);

        JTextField namaField = new JTextField();
        JTextField lokasiField = new JTextField();

        if (existing != null) {
            namaField.setText(existing.getNamaDepartemen());
            lokasiField.setText(existing.getLokasi());
        }

        add(new JLabel("Nama Departemen:"));
        add(namaField);
        add(new JLabel("Lokasi:"));
        add(lokasiField);

        JButton submit = new JButton("Simpan");
        add(new JLabel());
        add(submit);

        submit.addActionListener(e -> {
            Departemen d = existing != null ? existing : new Departemen();
            d.setNamaDepartemen(namaField.getText());
            d.setLokasi(lokasiField.getText());
            onSubmit.accept(d);
            dispose();
        });
    }
}
