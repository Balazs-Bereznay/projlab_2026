package view;

import model.Csomopont;
import java.awt.*;

public class CsomopontView extends View {
    private final Csomopont csomopont;
    private int x, y;
    private static final int CS_MERET = 28;

    public CsomopontView(Csomopont csomopont, Pont pozicio, PalyaPanel panel) {
        super(csomopont, panel);
        this.csomopont = csomopont;
        if (pozicio != null) { this.x = pozicio.x; this.y = pozicio.y; }
    }

    public void setPozicio(int x, int y) { this.x = x; this.y = y; }

    @Override
    public void kirajzol(Graphics2D g) {
        int px = x - CS_MERET / 2;
        int py = y - CS_MERET / 2;

        Color fill = csomopont.getCelpont()    ? new Color(195, 62,  62)  :
                     csomopont.getBuszmegallo() ? new Color(42,  102, 195) :
                                                  new Color(105, 110, 125);
        g.setColor(fill);
        g.fillOval(px, py, CS_MERET, CS_MERET);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawOval(px, py, CS_MERET, CS_MERET);
        g.setStroke(new BasicStroke(1));

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        FontMetrics fm = g.getFontMetrics();
        String lbl = csomopont.getAzonosito();
        g.drawString(lbl, x - fm.stringWidth(lbl) / 2, y + fm.getAscent() / 2 - 1);

        String tip = csomopont.getCelpont() ? "célpont" : csomopont.getBuszmegallo() ? "megálló" : "";
        if (!tip.isEmpty()) {
            g.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g.setColor(new Color(215, 215, 150));
            FontMetrics fm2 = g.getFontMetrics();
            g.drawString(tip, x - fm2.stringWidth(tip) / 2, y + CS_MERET / 2 + 12);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Csomopont getCsomopont() { return csomopont; }
}
