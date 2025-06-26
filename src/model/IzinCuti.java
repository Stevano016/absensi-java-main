package model;

import java.sql.Date;

public class IzinCuti {
    private int izinCutiId;
    private int karyawanId;
    private Date tanggalPengajuan;
    private Date tanggalMulai;
    private Date tanggalSelesai;
    private String jenis;
    private String alasan;
    private String status;

    private String karyawanName;
    public String getKaryawanName() { return karyawanName; }
    public void setKaryawanName(String karyawanName) { this.karyawanName = karyawanName; }


    public int getIzinCutiId() { return izinCutiId; }
    public void setIzinCutiId(int izinCutiId) { this.izinCutiId = izinCutiId; }

    public int getKaryawanId() { return karyawanId; }
    public void setKaryawanId(int karyawanId) { this.karyawanId = karyawanId; }

    public Date getTanggalPengajuan() { return tanggalPengajuan; }
    public void setTanggalPengajuan(Date tanggalPengajuan) { this.tanggalPengajuan = tanggalPengajuan; }

    public Date getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(Date tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public Date getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(Date tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public String getAlasan() { return alasan; }
    public void setAlasan(String alasan) { this.alasan = alasan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
