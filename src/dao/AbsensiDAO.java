package dao;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import model.Absensi;
import model.Karyawan;

public class AbsensiDAO {

    public static List<Absensi> getRecentByKaryawanId(int karyawanId) {
        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT * FROM absensi WHERE karyawan_id = ? ORDER BY tanggal DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, karyawanId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Absensi a = new Absensi();
                a.setAbsensiId(rs.getInt("absensi_id"));
                a.setKaryawanId(rs.getInt("karyawan_id"));
                a.setTanggal(rs.getDate("tanggal"));
                a.setWaktuMasuk(rs.getTime("waktu_masuk"));
                a.setWaktuPulang(rs.getTime("waktu_pulang"));
                a.setStatusKehadiran(rs.getString("status_kehadiran"));
                a.setStatusKaryawan(rs.getString("status_karyawan"));
                a.setCatatan(rs.getString("catatan"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Absensi> getAllWithKaryawanName() {
        List<Absensi> list = new ArrayList<>();
        String sql = """
            SELECT a.*, k.nama_lengkap 
            FROM absensi a
            JOIN karyawan k ON a.karyawan_id = k.karyawan_id
            ORDER BY a.tanggal DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Absensi a = new Absensi();
                a.setAbsensiId(rs.getInt("absensi_id"));
                a.setKaryawanId(rs.getInt("karyawan_id"));
                a.setTanggal(rs.getDate("tanggal"));
                a.setWaktuMasuk(rs.getTime("waktu_masuk"));
                a.setWaktuPulang(rs.getTime("waktu_pulang"));
                a.setStatusKehadiran(rs.getString("status_kehadiran"));
                a.setStatusKaryawan(rs.getString("status_karyawan"));
                a.setCatatan(rs.getString("catatan"));
                a.setKaryawanName(rs.getString("nama_lengkap"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean absenMasuk(int karyawanId, String keterangan) {
        try (Connection conn = DBConnection.getConnection()) {
            // Cek apakah sudah absen masuk tapi belum pulang
            String checkSql = "SELECT * FROM absensi WHERE karyawan_id = ? AND waktu_pulang IS NULL";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, karyawanId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // Sudah absen masuk
            }

            // Ambil data karyawan
            Karyawan karyawan = KaryawanDAO.findById(karyawanId);

            // Insert absen
            String insertSql = """
                INSERT INTO absensi (
                    karyawan_id, tanggal, waktu_masuk,
                    status_kehadiran, status_karyawan, catatan
                ) VALUES (?, ?, ?, 'hadir', ?, ?)
            """;

            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, karyawanId);
            insertStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            insertStmt.setTime(3, Time.valueOf(LocalTime.now()));
            insertStmt.setString(4, karyawan.getStatus()); // ambil dari kolom status di tabel karyawan
            insertStmt.setString(5, keterangan); // diisi manual

            return insertStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean absenPulang(int karyawanId) {
        String sql = "UPDATE absensi SET waktu_pulang = ?, status_kehadiran = 'hadir' " +
                    "WHERE karyawan_id = ? AND waktu_pulang IS NULL ORDER BY absensi_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTime(1, Time.valueOf(LocalTime.now()));
            stmt.setInt(2, karyawanId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static class RekapEntry {
        public String nama;
        public int hadir, terlambat, izin, sakit, alpha;

        public RekapEntry() {}

        public RekapEntry(String nama, int hadir, int terlambat, int izin, int sakit, int alpha) {
            this.nama = nama;
            this.hadir = hadir;
            this.terlambat = terlambat;
            this.izin = izin;
            this.sakit = sakit;
            this.alpha = alpha;
        }
    }

    public static List<RekapEntry> getRekapByMonthAndDepartemen(int bulan, int tahun, int departemenId) {
        List<RekapEntry> list = new ArrayList<>();

        String sql = """
            SELECT k.nama_lengkap,
                SUM(CASE WHEN a.status_kehadiran = 'Hadir' THEN 1 ELSE 0 END) AS hadir,
                SUM(CASE WHEN a.status_kehadiran = 'Terlambat' THEN 1 ELSE 0 END) AS terlambat,
                SUM(CASE WHEN a.status_kehadiran = 'Izin' THEN 1 ELSE 0 END) AS izin,
                SUM(CASE WHEN a.status_kehadiran = 'Sakit' THEN 1 ELSE 0 END) AS sakit,
                SUM(CASE WHEN a.status_kehadiran = 'Alpha' THEN 1 ELSE 0 END) AS alpha
            FROM absensi a
            JOIN karyawan k ON a.karyawan_id = k.karyawan_id
            WHERE MONTH(a.tanggal) = ? AND YEAR(a.tanggal) = ? AND k.departemen_id = ?
            GROUP BY k.nama_lengkap
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bulan);
            stmt.setInt(2, tahun);
            stmt.setInt(3, departemenId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RekapEntry entry = new RekapEntry(
                    rs.getString("nama_lengkap"),
                    rs.getInt("hadir"),
                    rs.getInt("terlambat"),
                    rs.getInt("izin"),
                    rs.getInt("sakit"),
                    rs.getInt("alpha")
                );
                list.add(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<RekapEntry> getAllRekap() {
        List<RekapEntry> list = new ArrayList<>();
        String sql = """
            SELECT k.nama_lengkap,
                SUM(CASE WHEN a.status_kehadiran = 'Hadir' THEN 1 ELSE 0 END) AS hadir,
                SUM(CASE WHEN a.status_kehadiran = 'Terlambat' THEN 1 ELSE 0 END) AS terlambat,
                SUM(CASE WHEN a.status_kehadiran = 'Izin' THEN 1 ELSE 0 END) AS izin,
                SUM(CASE WHEN a.status_kehadiran = 'Sakit' THEN 1 ELSE 0 END) AS sakit,
                SUM(CASE WHEN a.status_kehadiran = 'Alpha' THEN 1 ELSE 0 END) AS alpha
            FROM absensi a
            JOIN karyawan k ON a.karyawan_id = k.karyawan_id
            GROUP BY k.nama_lengkap
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RekapEntry r = new RekapEntry(
                    rs.getString("nama_lengkap"),
                    rs.getInt("hadir"),
                    rs.getInt("terlambat"),
                    rs.getInt("izin"),
                    rs.getInt("sakit"),
                    rs.getInt("alpha")
                );
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
