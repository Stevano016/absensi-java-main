package view;

import dao.AbsensiDAO;
import dao.IzinCutiDAO;
import dao.KaryawanDAO;
import model.Absensi;
import model.IzinCuti;
import model.Karyawan;
import model.User;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.jdatepicker.impl.*;
import java.util.Properties;
import java.text.SimpleDateFormat;



public class KaryawanDashboard extends JFrame {

    private final Karyawan karyawan;
    private JTable riwayatTable;
    private DefaultTableModel riwayatModel;

    public KaryawanDashboard(User user) {
        setTitle("Dashboard Karyawan");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load karyawan data
        this.karyawan = KaryawanDAO.findById(user.getIdKaryawan());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Profil", getProfilPanel());
        tabs.add("Absensi", getAbsenPanel());
        tabs.add("Ajukan Izin", getAjukanIzinPanel());
        tabs.add("Status Izin", getStatusIzinPanel());
        tabs.add("Riwayat Absensi", getRiwayatPanel());

        add(tabs);
    }

    private JPanel getProfilPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nama: " + karyawan.getNamaLengkap()));
        panel.add(new JLabel("NIK: " + karyawan.getNik()));
        panel.add(new JLabel("Jabatan: " + karyawan.getJabatan()));
        panel.add(new JLabel("Tanggal Masuk: " + karyawan.getTanggalMasuk()));
        panel.add(new JLabel("Status: " + karyawan.getStatus()));

        return panel;
    }

    private JPanel getAbsenPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel info = new JLabel("Tekan tombol di bawah untuk mencatat kehadiran hari ini:");
        JTextField ketField = new JTextField(20); // Tambahan
        JButton masuk = new JButton("Absen Masuk");
        JButton pulang = new JButton("Absen Pulang");

        JPanel btns = new JPanel();
        btns.add(new JLabel("Keterangan:"));
        btns.add(ketField);
        btns.add(masuk);
        btns.add(pulang);

        masuk.addActionListener(e -> {
            String keterangan = ketField.getText().trim();
            boolean success = AbsensiDAO.absenMasuk(karyawan.getKaryawanId(), keterangan);
            JOptionPane.showMessageDialog(this, success ? "Berhasil absen masuk!" : "Sudah absen hari ini atau gagal.");
            loadRiwayatData();
        });

        pulang.addActionListener(e -> {
            boolean success = AbsensiDAO.absenPulang(karyawan.getKaryawanId());
            JOptionPane.showMessageDialog(this, success ? "Berhasil absen pulang!" : "Belum absen masuk atau sudah pulang.");
            loadRiwayatData();
        });

        panel.add(info, BorderLayout.NORTH);
        panel.add(btns, BorderLayout.CENTER);
        return panel;
    }


    private JPanel getAjukanIzinPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        JDatePickerImpl tglMulai = createDatePicker();
        JDatePickerImpl tglSelesai = createDatePicker();
        JComboBox<String> jenisBox = new JComboBox<>(new String[]{"izin", "cuti", "sakit", "lainnya"});
        JTextArea alasanArea = new JTextArea(3, 20);

        JButton ajukan = new JButton("Ajukan");

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Tanggal Mulai:"));
        panel.add(tglMulai);
        panel.add(new JLabel("Tanggal Selesai:"));
        panel.add(tglSelesai);
        panel.add(new JLabel("Jenis:"));
        panel.add(jenisBox);
        panel.add(new JLabel("Alasan:"));
        panel.add(new JScrollPane(alasanArea));
        panel.add(new JLabel());
        panel.add(ajukan);

        ajukan.addActionListener(e -> {
            IzinCuti i = new IzinCuti();
            i.setKaryawanId(karyawan.getKaryawanId());
            i.setTanggalPengajuan(Date.valueOf(LocalDate.now()));
            i.setTanggalMulai(new java.sql.Date(((java.util.Date) tglMulai.getModel().getValue()).getTime()));
            i.setTanggalSelesai(new java.sql.Date(((java.util.Date) tglSelesai.getModel().getValue()).getTime()));
            i.setJenis((String) jenisBox.getSelectedItem());
            i.setAlasan(alasanArea.getText());
            i.setStatus("menunggu");

            if (IzinCutiDAO.insert(i)) {
                JOptionPane.showMessageDialog(this, "Permohonan dikirim.");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengirim permohonan.");
            }
        });

        return panel;
    }

    private JPanel getStatusIzinPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Tanggal", "Mulai", "Selesai", "Jenis", "Alasan", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        List<IzinCuti> list = IzinCutiDAO.getByKaryawanId(karyawan.getKaryawanId());
        for (IzinCuti i : list) {
            model.addRow(new Object[]{
                i.getTanggalPengajuan(),
                i.getTanggalMulai(),
                i.getTanggalSelesai(),
                i.getJenis(),
                i.getAlasan(),
                i.getStatus()
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getRiwayatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Tanggal", "Masuk", "Pulang", "Status", "Status Karyawan", "Catatan"};
        riwayatModel = new DefaultTableModel(columns, 0);
        riwayatTable = new JTable(riwayatModel);

        loadRiwayatData(); // pertama kali isi data

        panel.add(new JScrollPane(riwayatTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadRiwayatData() {
        riwayatModel.setRowCount(0); // clear isi lama
        List<Absensi> list = AbsensiDAO.getRecentByKaryawanId(karyawan.getKaryawanId());
        for (Absensi a : list) {
            riwayatModel.addRow(new Object[]{
                a.getTanggal(),
                a.getWaktuMasuk() != null ? a.getWaktuMasuk().toString() : "-",
                a.getWaktuPulang() != null ? a.getWaktuPulang().toString() : "-",
                a.getStatusKehadiran(),
                a.getStatusKaryawan() != null ? a.getStatusKaryawan() : "-",
                a.getCatatan() != null ? a.getCatatan() : "-"
            });
        }
    }


    private JDatePickerImpl createDatePicker() {
    UtilDateModel model = new UtilDateModel();
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");

    JDatePanelImpl panel = new JDatePanelImpl(model, p);
    return new JDatePickerImpl(panel, new DateLabelFormatter());
}

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormat.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value instanceof java.util.Date) {
                return dateFormat.format((java.util.Date) value);
            } else if (value instanceof java.util.Calendar) {
                return dateFormat.format(((java.util.Calendar) value).getTime());
            }
            return "";
        }
    }

}
