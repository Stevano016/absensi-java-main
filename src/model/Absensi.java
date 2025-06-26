package model;

import java.sql.Date;
import java.sql.Time;

public class Absensi {
    private int absensiId;
    private int karyawanId;
    private Date tanggal;
    private Time waktuMasuk;
    private Time waktuPulang;
    private String statusKehadiran;
    private String statusKaryawan;
    private String catatan;
    private String karyawanName;
    
    public String getKaryawanName() { return karyawanName; }
    public void setKaryawanName(String karyawanName) { this.karyawanName = karyawanName; }


    public int getAbsensiId() { return absensiId; }
    public void setAbsensiId(int absensiId) { this.absensiId = absensiId; }

    public int getKaryawanId() { return karyawanId; }
    public void setKaryawanId(int karyawanId) { this.karyawanId = karyawanId; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public Time getWaktuMasuk() { return waktuMasuk; }
    public void setWaktuMasuk(Time waktuMasuk) { this.waktuMasuk = waktuMasuk; }

    public Time getWaktuPulang() { return waktuPulang; }
    public void setWaktuPulang(Time waktuPulang) { this.waktuPulang = waktuPulang; }

    public String getStatusKehadiran() { return statusKehadiran; }
    public void setStatusKehadiran(String statusKehadiran) { this.statusKehadiran = statusKehadiran; }

    public String getStatusKaryawan() { return statusKaryawan; }
    public void setStatusKaryawan(String statusKaryawan) { this.statusKaryawan = statusKaryawan; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}
