package view;

import dao.DepartemenDAO;
import dao.JamKerjaDAO;
import dao.UserDAO;
import java.awt.*;
import java.sql.Time;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Departemen;
import model.JamKerja;
import model.User;

public class AdminDashboard extends JFrame {
    public AdminDashboard(User currentUser) {
        setTitle("Dashboard Admin IT");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Departemen", getDepartemenPanel());
        tabs.add("Jam Kerja", getJamKerjaPanel());
        tabs.add("User", getUserPanel());

        add(tabs);
    }

    private JPanel getDepartemenPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Nama", "Lokasi"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        refreshDepartemenTable(model);

        JButton tambah = new JButton("Tambah");
        JButton edit = new JButton("Edit");
        JButton hapus = new JButton("Hapus");

        JPanel buttons = new JPanel();
        buttons.add(tambah);
        buttons.add(edit);
        buttons.add(hapus);

        tambah.addActionListener(e -> {
            DepartemenForm form = new DepartemenForm(null, d -> {
                if (DepartemenDAO.insert(d)) {
                    refreshDepartemenTable(model);
                }
            });
            form.setVisible(true);
        });

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            Departemen d = new Departemen();
            d.setDepartemenId((int) model.getValueAt(row, 0));
            d.setNamaDepartemen((String) model.getValueAt(row, 1));
            d.setLokasi((String) model.getValueAt(row, 2));

            DepartemenForm form = new DepartemenForm(d, updated -> {
                if (DepartemenDAO.update(updated)) {
                    refreshDepartemenTable(model);
                }
            });
            form.setVisible(true);
        });

        hapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            int id = (int) model.getValueAt(row, 0);
            if (DepartemenDAO.delete(id)) {
                refreshDepartemenTable(model);
            }
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getJamKerjaPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Shift", "Hari", "Masuk", "Pulang", "Keterangan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        refreshJamKerjaTable(model);

        JButton tambah = new JButton("Tambah");
        JButton edit = new JButton("Edit");
        JButton hapus = new JButton("Hapus");

        JPanel buttons = new JPanel();
        buttons.add(tambah);
        buttons.add(edit);
        buttons.add(hapus);

        tambah.addActionListener(e -> {
            JamKerjaForm form = new JamKerjaForm(null, j -> {
                if (JamKerjaDAO.insert(j)) {
                    refreshJamKerjaTable(model);
                }
            });
            form.setVisible(true);
        });

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            JamKerja j = new JamKerja();
            j.setJamKerjaId((int) model.getValueAt(row, 0));
            j.setNamaShift((String) model.getValueAt(row, 1));
            j.setHari((String) model.getValueAt(row, 2));
            j.setJamMasuk(Time.valueOf((String) model.getValueAt(row, 3)));
            j.setJamPulang(Time.valueOf((String) model.getValueAt(row, 4)));
            j.setKeterangan((String) model.getValueAt(row, 5));

            JamKerjaForm form = new JamKerjaForm(j, updated -> {
                if (JamKerjaDAO.update(updated)) {
                    refreshJamKerjaTable(model);
                }
            });
            form.setVisible(true);
        });

        hapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            if (JamKerjaDAO.delete(id)) {
                refreshJamKerjaTable(model);
            }
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Username", "Role", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        refreshUserTable(model);

        JButton tambah = new JButton("Tambah");
        JButton nonaktif = new JButton("Nonaktifkan");
        JButton delete = new JButton("Hapus");
        JButton aktifkan = new JButton("Aktifkan");


        JPanel buttons = new JPanel();
        buttons.add(tambah);
        buttons.add(nonaktif);
        buttons.add(delete);
        buttons.add(aktifkan);

        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus user ini?");
            if (confirm == JOptionPane.YES_OPTION && UserDAO.delete(id)) {
                refreshUserTable(model);
            }
        });

        aktifkan.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            if (UserDAO.reactivate(id)) {
                refreshUserTable(model);
            }
        });


        tambah.addActionListener(e -> {
            UserForm form = new UserForm(null, u -> {
                if (UserDAO.insert(u)) {
                    refreshUserTable(model);
                }
            });
            form.setVisible(true);
        });

        nonaktif.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            if (UserDAO.deactivate(id)) {
                refreshUserTable(model);
            }
        });


        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshDepartemenTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Departemen d : DepartemenDAO.getAll()) {
            model.addRow(new Object[]{d.getDepartemenId(), d.getNamaDepartemen(), d.getLokasi()});
        }
    }

    private void refreshJamKerjaTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (JamKerja j : JamKerjaDAO.getAll()) {
            model.addRow(new Object[]{
                j.getJamKerjaId(),
                j.getNamaShift(),
                j.getHari(),
                j.getJamMasuk().toString(),
                j.getJamPulang().toString(),
                j.getKeterangan()
            });
        }
    }

    private void refreshUserTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (User u : UserDAO.getAll()) {
            model.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getRole(),
                u.getStatus()
            });
        }
    }

}
