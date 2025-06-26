package model;

public class Departemen {
    private int departemenId;
    private String namaDepartemen;
    private String lokasi;

    public int getDepartemenId() { return departemenId; }
    public void setDepartemenId(int departemenId) { this.departemenId = departemenId; }

    public String getNamaDepartemen() { return namaDepartemen; }
    public void setNamaDepartemen(String namaDepartemen) { this.namaDepartemen = namaDepartemen; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    @Override
    public String toString() {
        return namaDepartemen;
    }
}
