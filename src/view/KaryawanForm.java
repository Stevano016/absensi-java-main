package view;

import dao.DepartemenDAO;
import dao.JamKerjaDAO;
import model.Departemen;
import model.JamKerja;
import model.Karyawan;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.text.SimpleDateFormat;

import org.jdatepicker.impl.*;

public class KaryawanForm extends JDialog {
    public KaryawanForm(Karyawan existing, Consumer<Karyawan> onSubmit) {
        setTitle(existing == null ? "Tambah Karyawan" : "Edit Karyawan");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 5, 5));

        // Input Fields
        JTextField namaField = new JTextField();
        JTextField nikField = new JTextField();
        JTextField jabatanField = new JTextField();
        JDatePickerImpl tanggalPicker = createDatePicker();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"aktif", "resign"});

        // Dropdowns
        List<Departemen> departemenList = DepartemenDAO.getAll();
        List<JamKerja> jamKerjaList = JamKerjaDAO.getAll();
        JComboBox<Departemen> departemenBox = new JComboBox<>(departemenList.toArray(new Departemen[0]));
        JComboBox<JamKerja> jamKerjaBox = new JComboBox<>(jamKerjaList.toArray(new JamKerja[0]));

        // Pre-fill if editing
        if (existing != null) {
            namaField.setText(existing.getNamaLengkap());
            nikField.setText(existing.getNik());
            jabatanField.setText(existing.getJabatan());
            if (existing.getTanggalMasuk() != null) {
                java.util.Date utilDate = new java.util.Date(existing.getTanggalMasuk().getTime());
                ((UtilDateModel) tanggalPicker.getModel()).setValue(utilDate);
            }
            statusBox.setSelectedItem(existing.getStatus());

            // Select matching departemen
            for (Departemen d : departemenList) {
                if (d.getDepartemenId() == existing.getDepartemenId()) {
                    departemenBox.setSelectedItem(d);
                    break;
                }
            }

            // Select matching jam kerja
            for (JamKerja j : jamKerjaList) {
                if (j.getJamKerjaId() == existing.getJamKerjaId()) {
                    jamKerjaBox.setSelectedItem(j);
                    break;
                }
            }
        }

        // Labels & Inputs
        add(new JLabel("Nama Lengkap:"));
        add(namaField);
        add(new JLabel("NIK:"));
        add(nikField);
        add(new JLabel("Jabatan:"));
        add(jabatanField);
        add(new JLabel("Tanggal Masuk:"));
        add(tanggalPicker);
        add(new JLabel("Status:"));
        add(statusBox);
        add(new JLabel("Departemen:"));
        add(departemenBox);
        add(new JLabel("Jam Kerja:"));
        add(jamKerjaBox);

        JButton simpanBtn = new JButton("Simpan");
        add(new JLabel());
        add(simpanBtn);

        simpanBtn.addActionListener(e -> {
            try {
                Karyawan k = existing != null ? existing : new Karyawan();
                k.setNamaLengkap(namaField.getText());
                k.setNik(nikField.getText());
                k.setJabatan(jabatanField.getText());

                java.util.Date selectedDate = (java.util.Date) tanggalPicker.getModel().getValue();
                if (selectedDate != null) {
                    k.setTanggalMasuk(new java.sql.Date(selectedDate.getTime()));
                }

                k.setStatus((String) statusBox.getSelectedItem());

                Departemen selectedDep = (Departemen) departemenBox.getSelectedItem();
                JamKerja selectedJam = (JamKerja) jamKerjaBox.getSelectedItem();

                if (selectedDep != null) k.setDepartemenId(selectedDep.getDepartemenId());
                if (selectedJam != null) k.setJamKerjaId(selectedJam.getJamKerjaId());

                onSubmit.accept(k);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Tanggal harus format yyyy-mm-dd", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
            setVisible(true);

    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormat.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof java.util.Calendar) {
                    return dateFormat.format(((java.util.Calendar) value).getTime());
                } else if (value instanceof java.util.Date) {
                    return dateFormat.format((java.util.Date) value);
                }
            }
            return "";
        }

    }
}
