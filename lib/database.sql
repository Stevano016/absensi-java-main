-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               9.3.0 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.7.0.6850
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for employee_db
CREATE DATABASE IF NOT EXISTS `employee_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `employee_db`;

-- Dumping structure for table employee_db.absensi
CREATE TABLE IF NOT EXISTS `absensi` (
  `absensi_id` int NOT NULL AUTO_INCREMENT,
  `karyawan_id` int DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `waktu_masuk` time DEFAULT NULL,
  `waktu_pulang` time DEFAULT NULL,
  `status_kehadiran` enum('hadir','terlambat','izin','sakit','alpha') DEFAULT NULL,
  `status_karyawan` enum('aktif','resign') DEFAULT NULL,
  `catatan` text,
  PRIMARY KEY (`absensi_id`),
  KEY `karyawan_id` (`karyawan_id`),
  CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`karyawan_id`) REFERENCES `karyawan` (`karyawan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table employee_db.absensi: ~13 rows (approximately)
INSERT INTO `absensi` (`absensi_id`, `karyawan_id`, `tanggal`, `waktu_masuk`, `waktu_pulang`, `status_kehadiran`, `status_karyawan`, `catatan`) VALUES
	(1, 1, '2024-06-01', '08:00:00', '16:00:00', 'hadir', 'aktif', 'Tepat waktu'),
	(2, 2, '2024-06-01', '08:30:00', '16:30:00', 'terlambat', 'aktif', 'Keterlambatan 30 menit'),
	(3, 3, '2024-06-01', NULL, NULL, 'sakit', 'resign', 'Demam'),
	(4, 1, '2024-06-02', '08:05:00', '16:00:00', 'terlambat', 'aktif', 'Terlambat 5 menit'),
	(5, 2, '2024-06-02', NULL, NULL, 'alpha', 'aktif', 'Tanpa keterangan'),
	(6, 4, '2025-06-24', '23:59:28', '00:06:23', 'hadir', NULL, NULL),
	(7, 4, '2025-06-25', '00:03:38', '00:03:48', 'hadir', NULL, NULL),
	(8, 4, '2025-06-25', '00:06:39', '00:06:51', 'hadir', NULL, NULL),
	(9, 4, '2025-06-25', '00:10:14', '00:10:23', 'hadir', 'aktif', 'Tepat waktu'),
	(10, 4, '2025-06-25', '00:15:50', '00:15:53', 'hadir', 'aktif', 'Tepat waktu'),
	(11, 4, '2025-06-25', '00:18:43', '00:18:50', 'hadir', NULL, 'telat 12 jam'),
	(12, 4, '2025-06-25', '00:22:06', '00:22:23', 'hadir', 'aktif', 'telat banget'),
	(13, 4, '2025-06-25', '00:26:28', '00:26:30', 'hadir', 'aktif', 'telat');

-- Dumping structure for table employee_db.departemen
CREATE TABLE IF NOT EXISTS `departemen` (
  `departemen_id` int NOT NULL AUTO_INCREMENT,
  `nama_departemen` varchar(100) DEFAULT NULL,
  `lokasi` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`departemen_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table employee_db.departemen: ~4 rows (approximately)
INSERT INTO `departemen` (`departemen_id`, `nama_departemen`, `lokasi`) VALUES
	(1, 'IT', 'Jakarta'),
	(2, 'HRD', 'Bandung'),
	(3, 'Finance', 'Surabaya'),
	(4, 'Pembersih', 'Jakarta');

-- Dumping structure for table employee_db.izin_cuti
CREATE TABLE IF NOT EXISTS `izin_cuti` (
  `izin_id` int NOT NULL AUTO_INCREMENT,
  `karyawan_id` int DEFAULT NULL,
  `tanggal_pengajuan` date DEFAULT NULL,
  `tanggal_mulai` date DEFAULT NULL,
  `tanggal_selesai` date DEFAULT NULL,
  `jenis` varchar(20) DEFAULT NULL,
  `alasan` text,
  `status` enum('menunggu','disetujui','ditolak') DEFAULT NULL,
  PRIMARY KEY (`izin_id`),
  KEY `karyawan_id` (`karyawan_id`),
  CONSTRAINT `izin_cuti_ibfk_1` FOREIGN KEY (`karyawan_id`) REFERENCES `karyawan` (`karyawan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table employee_db.izin_cuti: ~4 rows (approximately)
INSERT INTO `izin_cuti` (`izin_id`, `karyawan_id`, `tanggal_pengajuan`, `tanggal_mulai`, `tanggal_selesai`, `jenis`, `alasan`, `status`) VALUES
	(1, 1, '2024-06-01', '2024-06-10', '2024-06-12', 'cuti', 'Cuti keluarga', 'menunggu'),
	(2, 2, '2024-06-02', '2024-06-15', '2024-06-17', 'izin', 'Urusan pribadi', 'disetujui'),
	(3, 3, '2024-06-03', '2024-06-20', '2024-06-21', 'sakit', 'Rawat jalan', 'ditolak'),
	(4, 4, '2025-06-25', '2025-06-28', '2025-07-02', 'sakit', 'menderita', 'disetujui');

-- Dumping structure for table employee_db.jam_kerja
CREATE TABLE IF NOT EXISTS `jam_kerja` (
  `jam_kerja_id` int NOT NULL AUTO_INCREMENT,
  `nama_shift` varchar(50) DEFAULT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu') DEFAULT NULL,
  `jam_masuk` time DEFAULT NULL,
  `jam_pulang` time DEFAULT NULL,
  `keterangan` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`jam_kerja_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table employee_db.jam_kerja: ~3 rows (approximately)
INSERT INTO `jam_kerja` (`jam_kerja_id`, `nama_shift`, `hari`, `jam_masuk`, `jam_pulang`, `keterangan`) VALUES
	(1, 'Senin Pagi', 'Senin', '08:00:00', '16:00:00', 'Shift Pagi'),
	(2, 'Selasa Siang', 'Selasa', '14:00:00', '22:00:00', 'Shift Siang'),
	(4, 'Kamis Pagi', 'Kamis', '08:00:00', '12:00:00', 'Shift Pagi');

-- Dumping structure for table employee_db.karyawan
CREATE TABLE IF NOT EXISTS `karyawan` (
  `karyawan_id` int NOT NULL AUTO_INCREMENT,
  `departemen_id` int DEFAULT NULL,
  `jam_kerja_id` int DEFAULT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL,
  `nik` varchar(20) DEFAULT NULL,
  `jabatan` varchar(50) DEFAULT NULL,
  `tanggal_masuk` date DEFAULT NULL,
  `status` enum('aktif','resign') DEFAULT NULL,
  PRIMARY KEY (`karyawan_id`),
  KEY `departemen_id` (`departemen_id`),
  KEY `jam_kerja_id` (`jam_kerja_id`),
  CONSTRAINT `karyawan_ibfk_1` FOREIGN KEY (`departemen_id`) REFERENCES `departemen` (`departemen_id`),
  CONSTRAINT `karyawan_ibfk_2` FOREIGN KEY (`jam_kerja_id`) REFERENCES `jam_kerja` (`jam_kerja_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table employee_db.karyawan: ~4 rows (approximately)
INSERT INTO `karyawan` (`karyawan_id`, `departemen_id`, `jam_kerja_id`, `nama_lengkap`, `nik`, `jabatan`, `tanggal_masuk`, `status`) VALUES
	(1, 1, 1, 'Andi Saputra', '1234567890', 'Programmer', '2022-01-10', 'aktif'),
	(2, 2, 2, 'Budi Hartono', '9876543210', 'HR Staff', '2021-11-05', 'aktif'),
	(3, 3, 2, 'Citra Lestari', '1928374650', 'Akuntan', '2023-02-15', 'resign'),
	(4, 1, 1, 'Jalu Mahardika', '12455123', 'Office', '2025-06-20', 'aktif');

-- Dumping structure for table employee_db.user
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `id_karyawan` int DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` enum('admin','hr','karyawan') DEFAULT NULL,
  `status` enum('aktif','resign') DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `id_karyawan` (`id_karyawan`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`karyawan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table employee_db.user: ~4 rows (approximately)
INSERT INTO `user` (`user_id`, `id_karyawan`, `username`, `password`, `role`, `status`) VALUES
	(1, 1, 'andi', 'password123', 'admin', 'aktif'),
	(2, 2, 'budi', 'password456', 'hr', 'aktif'),
	(3, 3, 'citra', 'password789', 'karyawan', 'aktif'),
	(5, 4, 'jalu', '123', 'karyawan', 'aktif');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
