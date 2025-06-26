package view;

import dao.AbsensiDAO;
import dao.IzinCutiDAO;
import dao.KaryawanDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Absensi;
import model.IzinCuti;
import model.Karyawan;
import model.User;

public class HRDashboard extends JFrame {
    public HRDashboard(User currentUser) {
        setTitle("Dashboard HR");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Data Karyawan", getKaryawanPanel());
        tabs.add("Absensi", getAbsensiPanel());
        tabs.add("Izin & Cuti", getIzinPanel());
        tabs.add("Laporan Kehadiran", getLaporanPanel());

        add(tabs);
    }

    private JPanel getKaryawanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Nama", "NIK", "Jabatan", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        refreshKaryawanTable(model);

        JButton tambah = new JButton("Tambah");
        JButton edit = new JButton("Edit");

        JPanel actions = new JPanel();
        actions.add(tambah);
        actions.add(edit);

        tambah.addActionListener(e -> {
            KaryawanForm form = new KaryawanForm(null, k -> {
                if (KaryawanDAO.insert(k)) refreshKaryawanTable(model);
            });
            form.setVisible(true);
        });

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            Karyawan k = new Karyawan();
            k.setKaryawanId((int) model.getValueAt(row, 0));
            k.setNamaLengkap((String) model.getValueAt(row, 1));
            k.setNik((String) model.getValueAt(row, 2));
            k.setJabatan((String) model.getValueAt(row, 3));
            k.setStatus((String) model.getValueAt(row, 4));

            KaryawanForm form = new KaryawanForm(k, updated -> {
                if (KaryawanDAO.update(updated)) refreshKaryawanTable(model);
            });
            form.setVisible(true);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getAbsensiPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Nama", "Tanggal", "Masuk", "Pulang", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Show all absensi
        for (Absensi a : AbsensiDAO.getAllWithKaryawanName()) {
            model.addRow(new Object[]{
                a.getAbsensiId(),
                a.getKaryawanName(),
                a.getTanggal(),
                a.getWaktuMasuk(),
                a.getWaktuPulang(),
                a.getStatusKehadiran()
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getIzinPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Nama", "Tanggal", "Mulai", "Selesai", "Jenis", "Alasan", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshIzinTable(model);

        JButton approve = new JButton("Setujui");
        JButton reject = new JButton("Tolak");

        JPanel action = new JPanel();
        action.add(approve);
        action.add(reject);

        approve.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            if (IzinCutiDAO.updateStatus(id, "disetujui")) refreshIzinTable(model);
        });

        reject.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            if (IzinCutiDAO.updateStatus(id, "ditolak")) refreshIzinTable(model);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(action, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshKaryawanTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Karyawan> list = KaryawanDAO.getAll();
        for (Karyawan k : list) {
            model.addRow(new Object[]{
                k.getKaryawanId(),
                k.getNamaLengkap(),
                k.getNik(),
                k.getJabatan(),
                k.getStatus()
            });
        }
    }

    private void refreshIzinTable(DefaultTableModel model) {
            model.setRowCount(0);
            List<IzinCuti> list = IzinCutiDAO.getAllWithKaryawanName();
            for (IzinCuti i : list) {
                model.addRow(new Object[]{
                    i.getIzinCutiId(),
                    i.getKaryawanName(),
                    i.getTanggalPengajuan(),
                    i.getTanggalMulai(),
                    i.getTanggalSelesai(),
                    i.getJenis(),
                    i.getAlasan(),
                    i.getStatus()
                });
            }
        }
        private JPanel getLaporanPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] kolom = {"Nama", "Hadir", "Terlambat", "Izin", "Sakit", "Alpha"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        JTable tabel = new JTable(model);

        List<AbsensiDAO.RekapEntry> semuaRekap = AbsensiDAO.getAllRekap(); // Ambil semua rekap tanpa filter

        for (AbsensiDAO.RekapEntry re : semuaRekap) {
            model.addRow(new Object[]{
                re.nama,
                re.hadir,
                re.terlambat,
                re.izin,
                re.sakit,
                re.alpha
            });
        }

        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }

}
