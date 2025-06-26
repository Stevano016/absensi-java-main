import view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true); // tanpa "view." karena sudah import
        });
    }
}
