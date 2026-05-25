import view.JatekAblak;
import javax.swing.SwingUtilities;

/**
 * A grafikus felhasználói felület belépési pontja.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JatekAblak ablak = new JatekAblak();
            ablak.setVisible(true);
        });
    }
}
