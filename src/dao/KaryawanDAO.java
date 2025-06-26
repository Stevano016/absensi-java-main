package dao;

import db.DBConnection;
import model.Karyawan;
import java.sql.*;
import java.util.*;

public class KaryawanDAO {

    public static boolean insert(Karyawan k) {
        String sql = """
            INSERT INTO karyawan (departemen_id, jam_kerja_id, nama_lengkap, nik, jabatan, tanggal_masuk, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, k.getDepartemenId());
            stmt.setInt(2, k.getJamKerjaId());
            stmt.setString(3, k.getNamaLengkap());
            stmt.setString(4, k.getNik());
            stmt.setString(5, k.getJabatan());
            stmt.setDate(6, k.getTanggalMasuk());
            stmt.setString(7, k.getStatus());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) k.setKaryawanId(rs.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean update(Karyawan k) {
        String sql = """
            UPDATE karyawan
            SET departemen_id=?, jam_kerja_id=?, nama_lengkap=?, nik=?, jabatan=?, tanggal_masuk=?, status=?
            WHERE karyawan_id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, k.getDepartemenId());
            stmt.setInt(2, k.getJamKerjaId());
            stmt.setString(3, k.getNamaLengkap());
            stmt.setString(4, k.getNik());
            stmt.setString(5, k.getJabatan());
            stmt.setDate(6, k.getTanggalMasuk());
            stmt.setString(7, k.getStatus());
            stmt.setInt(8, k.getKaryawanId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Karyawan findById(int id) {
        String sql = "SELECT * FROM karyawan WHERE karyawan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Karyawan k = new Karyawan();
                k.setKaryawanId(rs.getInt("karyawan_id"));
                k.setDepartemenId(rs.getInt("departemen_id"));
                k.setJamKerjaId(rs.getInt("jam_kerja_id"));
                k.setNamaLengkap(rs.getString("nama_lengkap"));
                k.setNik(rs.getString("nik"));
                k.setJabatan(rs.getString("jabatan"));
                k.setTanggalMasuk(rs.getDate("tanggal_masuk"));
                k.setStatus(rs.getString("status"));
                return k;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Karyawan> getKaryawanWithoutUser() {
        List<Karyawan> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = """
                SELECT k.*
                FROM karyawan k
                LEFT JOIN user u ON k.karyawan_id = u.id_karyawan
                WHERE u.id_karyawan IS NULL
            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Karyawan k = new Karyawan();
                k.setKaryawanId(rs.getInt("karyawan_id"));
                k.setNamaLengkap(rs.getString("nama_lengkap"));
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Karyawan> getKaryawanBelumPunyaUser() {
        List<Karyawan> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM karyawan WHERE karyawan_id NOT IN (SELECT id_karyawan FROM user)";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Karyawan k = new Karyawan();
                k.setKaryawanId(rs.getInt("karyawan_id"));
                k.setNamaLengkap(rs.getString("nama_lengkap"));
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Karyawan> getAll() {
        List<Karyawan> list = new ArrayList<>();
        String sql = "SELECT * FROM karyawan";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Karyawan k = new Karyawan();
                k.setKaryawanId(rs.getInt("karyawan_id"));
                k.setDepartemenId(rs.getInt("departemen_id"));
                k.setJamKerjaId(rs.getInt("jam_kerja_id"));
                k.setNamaLengkap(rs.getString("nama_lengkap"));
                k.setNik(rs.getString("nik"));
                k.setJabatan(rs.getString("jabatan"));
                k.setTanggalMasuk(rs.getDate("tanggal_masuk"));
                k.setStatus(rs.getString("status"));
                list.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
