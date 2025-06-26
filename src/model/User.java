package model;

public class User {
    private int userId;
    private int idKaryawan;
    private String username;
    private String password;
    private String role;
    private String status;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getIdKaryawan() { return idKaryawan; }
    public void setIdKaryawan(int idKaryawan) { this.idKaryawan = idKaryawan; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return username;
    }
}
