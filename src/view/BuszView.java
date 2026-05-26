package view;

import model.Busz;
import java.awt.*;
import java.util.Map;

public class BuszView extends JarmuView {
    private final Busz busz;
    private boolean kivalasztott = false;
    private String nev;

    public BuszView(Busz busz, String nev, PalyaPanel panel, Map<model.Utegyseg, int[]> poz) {
        super(busz, panel, poz);
        this.busz = busz;
        this.nev = nev;
    }

    public void setKivalasztott(boolean k) { this.kivalasztott = k; }

    @Override
    public void kirajzol(Graphics2D g) {
        int[] pos = getPozicio();
        if (pos == null) return;
        int r = 11;

        g.setColor(new Color(0, 0, 0, 85));
        g.fillOval(pos[0] - r + 2, pos[1] - r + 2, r * 2, r * 2);

        Color szin = busz.isBaleset() ? new Color(200, 0, 0) :
                     busz.getMegcsuszott() ? new Color(212, 52, 52) :
                     new Color(52, 185, 68);
        g.setColor(szin);
        g.fillOval(pos[0] - r, pos[1] - r, r * 2, r * 2);

        g.setColor(kivalasztott ? new Color(255, 228, 0) : Color.BLACK);
        g.setStroke(new BasicStroke(kivalasztott ? 2.5f : 1.5f));
        g.drawOval(pos[0] - r, pos[1] - r, r * 2, r * 2);
        g.setStroke(new BasicStroke(1));

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 9));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(nev, pos[0] - fm.stringWidth(nev) / 2, pos[1] + fm.getAscent() / 2 - 1);

        if (busz.isBaleset()) {
            g.setColor(new Color(255, 0, 0));
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            g.drawString("X", pos[0] - 4, pos[1] - r - 2);
        }
    }
}
