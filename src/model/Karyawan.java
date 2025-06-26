package model;

import java.sql.Date;

public class Karyawan {
    private int karyawanId;
    private int departemenId;
    private int jamKerjaId;
    private String namaLengkap;
    private String nik;
    private String jabatan;
    private Date tanggalMasuk;
    private String status;

    public int getKaryawanId() { return karyawanId; }
    public void setKaryawanId(int karyawanId) { this.karyawanId = karyawanId; }

    public int getDepartemenId() { return departemenId; }
    public void setDepartemenId(int departemenId) { this.departemenId = departemenId; }

    public int getJamKerjaId() { return jamKerjaId; }
    public void setJamKerjaId(int jamKerjaId) { this.jamKerjaId = jamKerjaId; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }

    public Date getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(Date tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return namaLengkap;
    }
}
