package dao;

import db.DBConnection;
import model.JamKerja;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class JamKerjaDAO {

    public static List<JamKerja> getAll() {
        List<JamKerja> list = new ArrayList<>();
        String sql = "SELECT * FROM jam_kerja";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                JamKerja j = new JamKerja();
                j.setJamKerjaId(rs.getInt("jam_kerja_id"));
                j.setNamaShift(rs.getString("nama_shift"));
                j.setHari(rs.getString("hari"));
                j.setJamMasuk(rs.getTime("jam_masuk"));
                j.setJamPulang(rs.getTime("jam_pulang"));
                j.setKeterangan(rs.getString("keterangan"));
                list.add(j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insert(JamKerja j) {
        String sql = "INSERT INTO jam_kerja (nama_shift, hari, jam_masuk, jam_pulang, keterangan) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, j.getNamaShift());
            stmt.setString(2, j.getHari());
            stmt.setTime(3, j.getJamMasuk());
            stmt.setTime(4, j.getJamPulang());
            stmt.setString(5, j.getKeterangan());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) j.setJamKerjaId(rs.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(JamKerja j) {
        String sql = "UPDATE jam_kerja SET nama_shift=?, hari=?, jam_masuk=?, jam_pulang=?, keterangan=? WHERE jam_kerja_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, j.getNamaShift());
            stmt.setString(2, j.getHari());
            stmt.setTime(3, j.getJamMasuk());
            stmt.setTime(4, j.getJamPulang());
            stmt.setString(5, j.getKeterangan());
            stmt.setInt(6, j.getJamKerjaId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
        if (isJamKerjaUsed(id)) {
            JOptionPane.showMessageDialog(null, "Jam kerja sedang digunakan oleh karyawan dan tidak bisa dihapus.");
            return false;
        }

        String sql = "DELETE FROM jam_kerja WHERE jam_kerja_id=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isJamKerjaUsed(int id) {
        String sql = "SELECT COUNT(*) FROM karyawan WHERE jam_kerja_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static JamKerja findById(int id) {
        String sql = "SELECT * FROM jam_kerja WHERE jam_kerja_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JamKerja jk = new JamKerja();
                jk.setJamKerjaId(rs.getInt("jam_kerja_id"));
                jk.setJamMasuk(rs.getTime("jam_masuk"));
                jk.setJamPulang(rs.getTime("jam_pulang"));
                return jk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
