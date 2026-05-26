package view;

import model.Auto;
import java.awt.*;
import java.util.Map;

public class AutoView extends JarmuView {
    private final Auto auto;

    public AutoView(Auto auto, PalyaPanel panel, Map<model.Utegyseg, int[]> poz) {
        super(auto, panel, poz);
        this.auto = auto;
    }

    @Override
    public void kirajzol(Graphics2D g) {
        int[] pos = getPozicio();
        if (pos == null) return;
        int r = 9;

        g.setColor(new Color(0, 0, 0, 85));
        g.fillOval(pos[0] - r + 2, pos[1] - r + 2, r * 2, r * 2);

        Color szin = auto.isBaleset() ? new Color(150, 0, 0) : new Color(235, 192, 48);
        g.setColor(szin);
        g.fillOval(pos[0] - r, pos[1] - r, r * 2, r * 2);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(pos[0] - r, pos[1] - r, r * 2, r * 2);
        g.setStroke(new BasicStroke(1));

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 8));
        FontMetrics fm = g.getFontMetrics();
        g.drawString("A", pos[0] - fm.stringWidth("A") / 2, pos[1] + fm.getAscent() / 2 - 1);

        if (auto.isBaleset()) {
            g.setColor(new Color(255, 0, 0));
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString("X", pos[0] - 3, pos[1] - r - 1);
        }
    }
}
