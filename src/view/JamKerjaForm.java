package view;

import model.JamKerja;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.function.Consumer;

public class JamKerjaForm extends JDialog {
    public JamKerjaForm(JamKerja existing, Consumer<JamKerja> onSubmit) {
        setTitle(existing == null ? "Tambah Jam Kerja" : "Edit Jam Kerja");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        JTextField shiftField = new JTextField();
        JComboBox<String> hariBox = new JComboBox<>(new String[]{
            "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
        });
        JTextField masukField = new JTextField("08:00:00");
        JTextField pulangField = new JTextField("17:00:00");
        JTextField ketField = new JTextField();

        if (existing != null) {
            shiftField.setText(existing.getNamaShift());
            hariBox.setSelectedItem(existing.getHari());
            masukField.setText(existing.getJamMasuk().toString());
            pulangField.setText(existing.getJamPulang().toString());
            ketField.setText(existing.getKeterangan());
        }

        add(new JLabel("Nama Shift:"));
        add(shiftField);
        add(new JLabel("Hari:"));
        add(hariBox);
        add(new JLabel("Jam Masuk:"));
        add(masukField);
        add(new JLabel("Jam Pulang:"));
        add(pulangField);
        add(new JLabel("Keterangan:"));
        add(ketField);

        JButton simpanBtn = new JButton("Simpan");
        add(new JLabel());
        add(simpanBtn);

        simpanBtn.addActionListener(e -> {
            JamKerja j = existing != null ? existing : new JamKerja();
            j.setNamaShift(shiftField.getText());
            j.setHari((String) hariBox.getSelectedItem());
            j.setJamMasuk(Time.valueOf(masukField.getText()));
            j.setJamPulang(Time.valueOf(pulangField.getText()));
            j.setKeterangan(ketField.getText());
            onSubmit.accept(j);
            dispose();
        });
    }
}
