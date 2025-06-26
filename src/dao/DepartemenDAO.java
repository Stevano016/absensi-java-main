package dao;

import db.DBConnection;
import model.Departemen;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;


public class DepartemenDAO {
    public static List<Departemen> getAll() {
        List<Departemen> list = new ArrayList<>();
        String sql = "SELECT * FROM departemen";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Departemen d = new Departemen();
                d.setDepartemenId(rs.getInt("departemen_id"));
                d.setNamaDepartemen(rs.getString("nama_departemen"));
                d.setLokasi(rs.getString("lokasi"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    
    public static boolean insert(Departemen d) {
        String sql = "INSERT INTO departemen (nama_departemen, lokasi) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, d.getNamaDepartemen());
            stmt.setString(2, d.getLokasi());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) d.setDepartemenId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(Departemen d) {
        String sql = "UPDATE departemen SET nama_departemen=?, lokasi=? WHERE departemen_id=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getNamaDepartemen());
            stmt.setString(2, d.getLokasi());
            stmt.setInt(3, d.getDepartemenId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
    // Cek dulu apakah departemen ini masih dipakai di karyawan
    if (isDepartemenUsed(id)) {
        JOptionPane.showMessageDialog(null, "Departemen sedang digunakan oleh karyawan dan tidak bisa dihapus.");
        return false;
    }

    String sql = "DELETE FROM departemen WHERE departemen_id=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isDepartemenUsed(int id) {
        String sql = "SELECT COUNT(*) FROM karyawan WHERE departemen_id=?";
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
        return true;
    }

}

