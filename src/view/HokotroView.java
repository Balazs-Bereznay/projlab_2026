package view;

import model.Hokotro;
import java.awt.*;
import java.util.Map;

public class HokotroView extends JarmuView {
    private final Hokotro hokotro;
    private boolean kivalasztott = false;

    public HokotroView(Hokotro hokotro, PalyaPanel panel, Map<model.Utegyseg, int[]> poz) {
        super(hokotro, panel, poz);
        this.hokotro = hokotro;
    }

    public void setKivalasztott(boolean k) { this.kivalasztott = k; }

    @Override
    public void kirajzol(Graphics2D g) {
        int[] pos = getPozicio();
        if (pos == null) return;
        int r = 11;

        g.setColor(new Color(0, 0, 0, 85));
        g.fillOval(pos[0] - r + 2, pos[1] - r + 2, r * 2, r * 2);

        Color szin = hokotro.isBaleset() ? new Color(200, 0, 0) :
                     hokotro.getMegcsuszott() ? new Color(212, 52, 52) :
                     new Color(235, 128, 28);
        g.setColor(szin);
        g.fillOval(pos[0] - r, pos[1] - r, r * 2, r * 2);

        g.setColor(kivalasztott ? new Color(255, 228, 0) : Color.BLACK);
        g.setStroke(new BasicStroke(kivalasztott ? 2.5f : 1.5f));
        g.drawOval(pos[0] - r, pos[1] - r, r * 2, r * 2);
        g.setStroke(new BasicStroke(1));

        String fejRovid = "";
        if (hokotro.getFej() != null) {
            String fn = hokotro.getFej().getClass().getSimpleName();
            switch (fn) {
                case "Sopro":         fejRovid = "Sp"; break;
                case "Soszoro":       fejRovid = "So"; break;
                case "Hanyo":         fejRovid = "Há"; break;
                case "Jegtoro":       fejRovid = "Jt"; break;
                case "Sarkany":       fejRovid = "Sá"; break;
                case "Zuzalekszoro":  fejRovid = "Zs"; break;
                default: fejRovid = fn.length() >= 2 ? fn.substring(0, 2) : fn;
            }
        }
        String lbl = "H" + fejRovid;

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 8));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(lbl, pos[0] - fm.stringWidth(lbl) / 2, pos[1] + fm.getAscent() / 2 - 1);

        if (hokotro.isBaleset()) {
            g.setColor(new Color(255, 0, 0));
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            g.drawString("X", pos[0] - 4, pos[1] - r - 2);
        }
    }
}
