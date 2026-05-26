package view;

import model.Utegyseg;
import java.awt.*;

public class UtegysegView extends View {
    private final Utegyseg utegyseg;
    private Rectangle befoglaloTeglalap;

    private static final Color COL_TISZTA    = new Color(148, 152, 148);
    private static final Color COL_HO_KONNYU = new Color(195, 210, 238);
    private static final Color COL_JEG       = new Color(75,  198, 215);
    private static final Color COL_SOS       = new Color(218, 214, 135);
    private static final Color COL_ZUZALEK   = new Color(185, 165, 135);
    private static final Color COL_BLOKKOLT  = new Color(55,  65,  190);

    public UtegysegView(Utegyseg utegyseg, PalyaPanel panel) {
        super(utegyseg, panel);
        this.utegyseg = utegyseg;
    }

    public void setBefoglaloTeglalap(Rectangle r) {
        this.befoglaloTeglalap = r;
    }

    public Rectangle getBefoglaloTeglalap() {
        return befoglaloTeglalap;
    }

    public boolean tartalmaz(int x, int y) {
        return befoglaloTeglalap != null && befoglaloTeglalap.contains(x, y);
    }

    @Override
    public void kirajzol(Graphics2D g) {
        if (befoglaloTeglalap == null) return;
        int x = befoglaloTeglalap.x;
        int y = befoglaloTeglalap.y;
        int w = befoglaloTeglalap.width;
        int h = befoglaloTeglalap.height;

        g.setColor(szin());
        g.fillRoundRect(x, y, w, h, 4, 4);

        boolean blokkolt = utegyseg.getBlokkolt();
        g.setStroke(blokkolt ? new BasicStroke(2f) : new BasicStroke(1f));
        g.setColor(blokkolt ? new Color(220, 50, 50) : new Color(22, 24, 34));
        g.drawRoundRect(x, y, w, h, 4, 4);
        g.setStroke(new BasicStroke(1f));

        String s = allapotSzoveg();
        if (s != null) {
            g.setFont(new Font("SansSerif", Font.BOLD, 7));
            FontMetrics fm = g.getFontMetrics();
            int sw = fm.stringWidth(s);
            int tx = x + (w - sw) / 2;
            int ty = y + h - 1;
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(tx - 1, ty - fm.getAscent(), sw + 2, fm.getHeight());
            g.setColor(Color.WHITE);
            g.drawString(s, tx, ty);
        }
    }

    private Color szin() {
        if (utegyseg.getBlokkolt()) return COL_BLOKKOLT;
        if (utegyseg.getZuzalek()) return COL_ZUZALEK;
        int jeg = utegyseg.getJegMagassag();
        if (utegyseg.getJeges() && jeg > 0)
            return new Color(55, Math.min(225, 130 + jeg * 10), Math.min(225, 130 + jeg * 10));
        if (utegyseg.getJeges()) return COL_JEG;
        int ho = utegyseg.getHoMagassag();
        if (ho > 0) {
            int w = Math.min(85, ho * 9);
            return new Color(Math.min(255, 155 + w), Math.min(255, 172 + w), Math.min(255, 198 + w));
        }
        if (utegyseg.getSoMennyiseg() > 0) return COL_SOS;
        return COL_TISZTA;
    }

    private String allapotSzoveg() {
        if (utegyseg.getBlokkolt()) return "B!";
        int jeg = utegyseg.getJegMagassag();
        int so  = utegyseg.getSoMennyiseg();
        if (utegyseg.getJeges() && jeg > 0) return "j" + jeg + (so > 0 ? " s" + so : "");
        int ho = utegyseg.getHoMagassag();
        if (ho > 0 && so > 0) return "h" + ho + " s" + so;
        if (ho > 0) return "h" + ho;
        if (so > 0) return "s" + so;
        if (utegyseg.getZuzalek()) return "Z";
        return null;
    }

    public Utegyseg getUtegyseg() { return utegyseg; }
}
