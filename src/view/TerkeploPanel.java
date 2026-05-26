package view;

import controller.JatekController;
import controller.JatekController.Fazis;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * A játék térképét megjelenítő panel. Rajzolja a csomópontokat, útszakaszokat,
 * útegységeket (hó/jég állapottal) és járműveket. Kattintással jármű vagy útegység választható.
 */
public class TerkeploPanel extends JPanel {

    private final JatekController controller;

    private static final int UE_MERET = 20;
    private static final int CS_MERET = 28;

    private static final Color COL_TISZTA    = new Color(148, 152, 148);
    private static final Color COL_HO_KONNYU = new Color(195, 210, 238);
    private static final Color COL_HO_MELYE  = new Color(55,  65,  190);
    private static final Color COL_JEG       = new Color(75,  198, 215);
    private static final Color COL_SOS       = new Color(218, 214, 135);
    private static final Color COL_ZUZALEK   = new Color(185, 165, 135);

    public TerkeploPanel(JatekController controller) {
        this.controller = controller;
        setBackground(new Color(42, 44, 54));
        setPreferredSize(new Dimension(650, 530));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    handleClick(e.getX(), e.getY());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (controller.getTerkep() == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        rajzolUtszakaszok(g2);
        rajzolUtegysegek(g2);
        rajzolCsomopontok(g2);
        rajzolJarmuvek(g2);
        rajzolFazisJelzo(g2);
    }

    // ---------------------------------------------------------------
    // Rajzolás

    private void rajzolUtszakaszok(Graphics2D g2) {
        Map<Csomopont, int[]> poz = controller.getCsomopontPoziciok();
        g2.setStroke(new BasicStroke(5));
        for (Ut ut : controller.getTerkep().getElLista()) {
            Csomopont vp1 = ut.getVegpont1(), vp2 = ut.getVegpont2();
            if (vp1 == null || vp2 == null) continue;
            int[] p1 = poz.get(vp1), p2 = poz.get(vp2);
            if (p1 == null || p2 == null) continue;
            g2.setColor(ut.getAlagut() ? new Color(75, 52, 32) : new Color(75, 78, 92));
            g2.drawLine(p1[0], p1[1], p2[0], p2[1]);
            if (ut.getAlagut()) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 9));
                g2.setColor(new Color(160, 115, 75));
                int mx = (p1[0] + p2[0]) / 2, my = (p1[1] + p2[1]) / 2;
                g2.drawString("ALAGÚT", mx - 22, my - 8);
            }
        }
        g2.setStroke(new BasicStroke(1));
    }

    private void rajzolUtegysegek(Graphics2D g2) {
        Map<Utegyseg, int[]> poz   = controller.getUtegysegPoziciok();
        Map<Utegyseg, int[]> irany = controller.getUtegysegIranyok();
        Map<Iranyithato, List<Utegyseg>> tervek = controller.getTervekMap();
        Iranyithato kiv = controller.getKivalasztottJarmu();

        Set<Utegyseg> terveltSet = new HashSet<>();
        if (kiv != null) {
            List<Utegyseg> terv = tervek.get(kiv);
            if (terv != null) terveltSet.addAll(terv);
        }

        for (Map.Entry<Utegyseg, int[]> entry : poz.entrySet()) {
            Utegyseg ue = entry.getKey();
            int[] pos   = entry.getValue();
            int x = pos[0] - UE_MERET / 2;
            int y = pos[1] - UE_MERET / 2;

            // Kitöltés
            g2.setColor(utegysegSzin(ue));
            g2.fillRoundRect(x, y, UE_MERET, UE_MERET, 4, 4);

            // Keret
            boolean tervelt  = terveltSet.contains(ue);
            boolean blokkolt = ue.getBlokkolt();
            g2.setStroke(new BasicStroke(tervelt ? 2.5f : blokkolt ? 2f : 1f));
            g2.setColor(tervelt  ? new Color(255, 215, 0) :
                        blokkolt ? new Color(220, 50,  50) : new Color(22, 24, 34));
            g2.drawRoundRect(x, y, UE_MERET, UE_MERET, 4, 4);
            g2.setStroke(new BasicStroke(1));

            // Menetirány nyíl
            if (irany != null) {
                int[] dir = irany.get(ue);
                if (dir != null) rajzolNyil(g2, pos[0], pos[1], dir[0], dir[1]);
            }

            // Állapot szöveg
            rajzolAllapotSzoveg(g2, ue, pos[0], pos[1]);
        }
    }

    private void rajzolNyil(Graphics2D g2, int cx, int cy, int dx, int dy) {
        double len = Math.sqrt((double) dx * dx + (double) dy * dy);
        if (len < 1) return;
        double nx = dx / len, ny = dy / len;
        int sz = 5;
        int tipX  = (int)(cx + nx * sz),   tipY  = (int)(cy + ny * sz);
        int baseX = (int)(cx - nx * sz),   baseY = (int)(cy - ny * sz);
        int s1x   = (int)(baseX - ny * 3), s1y   = (int)(baseY + nx * 3);
        int s2x   = (int)(baseX + ny * 3), s2y   = (int)(baseY - nx * 3);
        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillPolygon(new int[]{tipX, s1x, s2x}, new int[]{tipY, s1y, s2y}, 3);
    }

    private void rajzolAllapotSzoveg(Graphics2D g2, Utegyseg ue, int cx, int cy) {
        String s = null;
        if (ue.getBlokkolt())                           s = "B!";
        else if (ue.getJeges() && ue.getJegMagassag() > 0) s = "j" + ue.getJegMagassag();
        else if (ue.getHoMagassag() > 0)               s = "h" + ue.getHoMagassag();
        else if (ue.getSoMennyiseg() > 0)              s = "só";
        if (s == null) return;

        g2.setFont(new Font("SansSerif", Font.BOLD, 7));
        FontMetrics fm = g2.getFontMetrics();
        int sw = fm.stringWidth(s);
        int tx = cx - sw / 2;
        int ty = cy + UE_MERET / 2 - 1;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(tx - 1, ty - fm.getAscent(), sw + 2, fm.getHeight());
        g2.setColor(Color.WHITE);
        g2.drawString(s, tx, ty);
    }

    private Color utegysegSzin(Utegyseg ue) {
        if (ue.getBlokkolt())   return COL_HO_MELYE;
        if (ue.getZuzalek())    return COL_ZUZALEK;
        int jeg = ue.getJegMagassag();
        if (ue.getJeges() && jeg > 0)
            return new Color(55, Math.min(225, 130 + jeg * 10), Math.min(225, 130 + jeg * 10));
        if (ue.getJeges())      return COL_JEG;
        int ho = ue.getHoMagassag();
        if (ho > 0) {
            int w = Math.min(85, ho * 9);
            return new Color(Math.min(255, 155 + w), Math.min(255, 172 + w), Math.min(255, 198 + w));
        }
        if (ue.getSoMennyiseg() > 0) return COL_SOS;
        return COL_TISZTA;
    }

    private void rajzolCsomopontok(Graphics2D g2) {
        Map<Csomopont, int[]> poz = controller.getCsomopontPoziciok();
        for (Map.Entry<Csomopont, int[]> entry : poz.entrySet()) {
            Csomopont cs = entry.getKey();
            int[] pos = entry.getValue();
            int x = pos[0] - CS_MERET / 2;
            int y = pos[1] - CS_MERET / 2;

            Color fill = cs.getCelpont()    ? new Color(195, 62, 62) :
                         cs.getBuszmegallo()? new Color(42,  102, 195) :
                                             new Color(105, 110, 125);
            g2.setColor(fill);
            g2.fillOval(x, y, CS_MERET, CS_MERET);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, CS_MERET, CS_MERET);
            g2.setStroke(new BasicStroke(1));

            // Azonosító betű
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            String lbl = cs.getAzonosito();
            g2.drawString(lbl, pos[0] - fm.stringWidth(lbl) / 2, pos[1] + fm.getAscent() / 2 - 1);

            // Típus felirat
            String tip = cs.getCelpont() ? "célpont" : cs.getBuszmegallo() ? "megálló" : "";
            if (!tip.isEmpty()) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                g2.setColor(new Color(215, 215, 150));
                FontMetrics fm2 = g2.getFontMetrics();
                g2.drawString(tip, pos[0] - fm2.stringWidth(tip) / 2, pos[1] + CS_MERET / 2 + 12);
            }
        }
    }

    private void rajzolJarmuvek(Graphics2D g2) {
        Map<Utegyseg, int[]> poz = controller.getUtegysegPoziciok();
        Iranyithato kiv = controller.getKivalasztottJarmu();

        for (Auto a : controller.getAutok())
            rajzolJarmu(g2, a, poz, new Color(235, 192, 48), "A", false);

        int bi = 1;
        for (Busz b : controller.getBuszok())
            rajzolJarmu(g2, b, poz, new Color(52, 185, 68), "B" + bi++, b == kiv);

        int hi = 1;
        for (Hokotro hk : controller.getHokotrók())
            rajzolJarmu(g2, hk, poz, new Color(235, 128, 28), "H" + hi++, hk == kiv);
    }

    private void rajzolJarmu(Graphics2D g2, Jarmu j, Map<Utegyseg, int[]> poz,
                              Color szin, String lbl, boolean kivalasztott) {
        if (j.getUtegyseg() == null) return;
        int[] pos = poz.get(j.getUtegyseg());
        if (pos == null) return;
        int r = 11;

        // Árnyék
        g2.setColor(new Color(0, 0, 0, 85));
        g2.fillOval(pos[0] - r + 2, pos[1] - r + 2, r * 2, r * 2);

        // Kitöltés (piros ha megcsúszott)
        g2.setColor(j.getMegcsuszott() ? new Color(212, 52, 52) : szin);
        g2.fillOval(pos[0] - r, pos[1] - r, r * 2, r * 2);

        // Keret (sárga ha kiválasztott)
        g2.setColor(kivalasztott ? new Color(255, 228, 0) : Color.BLACK);
        g2.setStroke(new BasicStroke(kivalasztott ? 2.5f : 1.5f));
        g2.drawOval(pos[0] - r, pos[1] - r, r * 2, r * 2);
        g2.setStroke(new BasicStroke(1));

        // Betű
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 9));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(lbl, pos[0] - fm.stringWidth(lbl) / 2, pos[1] + fm.getAscent() / 2 - 1);
    }

    private void rajzolFazisJelzo(Graphics2D g2) {
        JatekController.Fazis fazis = controller.getAktualisFazis();
        String szoveg;
        Color szin;
        switch (fazis) {
            case TERVEZES:
                szoveg = "TERVEZÉSI FÁZIS – válassz járművet, majd jelölj ki útegységeket";
                szin   = new Color(80, 160, 80);
                break;
            case SZIMULACIO:
                szoveg = "SZIMULÁCIÓ – tick " + (controller.getSzimulacioTickSzamlalo() + 1)
                       + " / " + JatekController.TICKS_PER_KOR;
                szin   = new Color(160, 100, 30);
                break;
            default:
                szoveg = "JÁTÉK VÉGE";
                szin   = new Color(180, 40, 40);
        }
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        int sw = fm.stringWidth(szoveg);
        int px2 = (getWidth() - sw) / 2;
        int py2 = getHeight() - 8;
        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(px2 - 6, py2 - fm.getAscent() - 2, sw + 12, fm.getHeight() + 4, 6, 6);
        g2.setColor(szin);
        g2.drawString(szoveg, px2, py2);
    }

    // ---------------------------------------------------------------
    // Egérkezelés

    private void handleClick(int mx, int my) {
        Map<Utegyseg, int[]> poz = controller.getUtegysegPoziciok();
        // Jármű kattintás elsőbbséget élvez (bármely fázisban)
        if (clickJarmu(mx, my, poz)) return;
        // Útegység kattintás: csak tervezési fázisban, ha ki van választva jármű
        if (controller.getAktualisFazis() == Fazis.TERVEZES
                && controller.getKivalasztottJarmu() != null) {
            clickUtegyseg(mx, my, poz);
        }
    }

    private boolean clickJarmu(int mx, int my, Map<Utegyseg, int[]> poz) {
        final int R = 14;
        for (Hokotro hk : controller.getHokotrók()) {
            if (hk.getUtegyseg() == null) continue;
            int[] p = poz.get(hk.getUtegyseg());
            if (p != null && Math.hypot(mx - p[0], my - p[1]) <= R) {
                controller.jarmuvKivalaszt(hk);
                return true;
            }
        }
        for (Busz b : controller.getBuszok()) {
            if (b.getUtegyseg() == null) continue;
            int[] p = poz.get(b.getUtegyseg());
            if (p != null && Math.hypot(mx - p[0], my - p[1]) <= R) {
                controller.jarmuvKivalaszt(b);
                return true;
            }
        }
        return false;
    }

    private void clickUtegyseg(int mx, int my, Map<Utegyseg, int[]> poz) {
        Utegyseg legkozelebbi = null;
        double minD = UE_MERET * 1.5;
        for (Map.Entry<Utegyseg, int[]> e : poz.entrySet()) {
            int[] p = e.getValue();
            double d = Math.hypot(mx - p[0], my - p[1]);
            if (d < minD) { minD = d; legkozelebbi = e.getKey(); }
        }
        if (legkozelebbi != null) controller.utegysegKijelol(legkozelebbi);
    }
}
