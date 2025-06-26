package dao;

import db.DBConnection;
import model.IzinCuti;
import java.sql.*;
import java.util.*;

public class IzinCutiDAO {

    public static List<IzinCuti> getByKaryawanId(int karyawanId) {
        List<IzinCuti> list = new ArrayList<>();
        String sql = "SELECT * FROM izin_cuti WHERE karyawan_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, karyawanId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                IzinCuti i = new IzinCuti();
                i.setIzinCutiId(rs.getInt("izin_id"));
                i.setKaryawanId(rs.getInt("karyawan_id"));
                i.setTanggalPengajuan(rs.getDate("tanggal_pengajuan"));
                i.setTanggalMulai(rs.getDate("tanggal_mulai"));
                i.setTanggalSelesai(rs.getDate("tanggal_selesai"));
                i.setJenis(rs.getString("jenis"));
                i.setAlasan(rs.getString("alasan"));
                i.setStatus(rs.getString("status"));
                list.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<IzinCuti> getAllWithKaryawanName() {
        List<IzinCuti> list = new ArrayList<>();
        String sql = """
            SELECT i.*, k.nama_lengkap 
            FROM izin_cuti i
            JOIN karyawan k ON i.karyawan_id = k.karyawan_id
            ORDER BY i.tanggal_pengajuan DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                IzinCuti i = new IzinCuti();
                i.setIzinCutiId(rs.getInt("izin_id"));
                i.setKaryawanId(rs.getInt("karyawan_id"));
                i.setTanggalPengajuan(rs.getDate("tanggal_pengajuan"));
                i.setTanggalMulai(rs.getDate("tanggal_mulai"));
                i.setTanggalSelesai(rs.getDate("tanggal_selesai"));
                i.setJenis(rs.getString("jenis"));
                i.setAlasan(rs.getString("alasan"));
                i.setStatus(rs.getString("status"));
                i.setKaryawanName(rs.getString("nama_lengkap")); // Add field in model
                list.add(i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean updateStatus(int id, String status) {
        String sql = "UPDATE izin_cuti SET status=? WHERE izin_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(IzinCuti i) {
        String sql = """
            INSERT INTO izin_cuti (karyawan_id, tanggal_pengajuan, tanggal_mulai, tanggal_selesai, jenis, alasan, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i.getKaryawanId());
            stmt.setDate(2, i.getTanggalPengajuan());
            stmt.setDate(3, i.getTanggalMulai());
            stmt.setDate(4, i.getTanggalSelesai());
            stmt.setString(5, i.getJenis());
            stmt.setString(6, i.getAlasan());
            stmt.setString(7, i.getStatus());
    
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false;
    }
    
}
