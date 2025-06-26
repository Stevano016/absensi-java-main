package model;

import java.sql.Time;

public class JamKerja {
    private int jamKerjaId;
    private String namaShift;
    private String hari;
    private Time jamMasuk;
    private Time jamPulang;
    private String keterangan;

    public int getJamKerjaId() { return jamKerjaId; }
    public void setJamKerjaId(int jamKerjaId) { this.jamKerjaId = jamKerjaId; }

    public String getNamaShift() { return namaShift; }
    public void setNamaShift(String namaShift) { this.namaShift = namaShift; }

    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }

    public Time getJamMasuk() { return jamMasuk; }
    public void setJamMasuk(Time jamMasuk) { this.jamMasuk = jamMasuk; }

    public Time getJamPulang() { return jamPulang; }
    public void setJamPulang(Time jamPulang) { this.jamPulang = jamPulang; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    @Override
    public String toString() {
        return namaShift;
    }
}
