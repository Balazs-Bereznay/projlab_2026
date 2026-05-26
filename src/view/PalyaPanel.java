package view;

import controller.JatekController;
import controller.Fazis;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PalyaPanel extends JPanel {

    private JatekController controller;
    private PalyaLayout layout;

    private final Map<Csomopont, CsomopontView> csomopontViewk = new LinkedHashMap<>();
    private final Map<Utegyseg, UtegysegView>   utegysegViewk  = new LinkedHashMap<>();
    private final Map<Jarmu, JarmuView>          jarmuViewk     = new LinkedHashMap<>();
    private final List<UtView>                   utViewk        = new ArrayList<>();

    private List<Utegyseg> kijeloltUtegysegek = new ArrayList<>();
    private Set<Utegyseg> kijelolhetoUtegysegek = new LinkedHashSet<>();
    private Jarmu kivalasztottJarmu;
    private Busz kivalasztottBusz;

    public PalyaPanel() {
        setBackground(new Color(42, 44, 54));
        setPreferredSize(new Dimension(650, 530));
    }

    public void setController(JatekController ctrl) { this.controller = ctrl; }
    public void setLayout2(PalyaLayout l) { this.layout = l; }

    public void setCsomopontViewk(Map<Csomopont, CsomopontView> m) { csomopontViewk.clear(); csomopontViewk.putAll(m); }
    public void setUtegysegViewk(Map<Utegyseg, UtegysegView> m)    { utegysegViewk.clear(); utegysegViewk.putAll(m); }
    public void setJarmuViewk(Map<Jarmu, JarmuView> m)             { jarmuViewk.clear(); jarmuViewk.putAll(m); }
    public void setUtViewk(List<UtView> list)                       { utViewk.clear(); utViewk.addAll(list); }

    public void setKijeloltUtegysegek(List<Utegyseg> lista) {
        this.kijeloltUtegysegek = lista != null ? lista : new ArrayList<>();
        repaint();
    }

    public void setKijelolhetoUtegysegek(Collection<Utegyseg> lista) {
        this.kijelolhetoUtegysegek = lista != null ? new LinkedHashSet<>(lista) : new LinkedHashSet<>();
        repaint();
    }

    public void setKivalasztottJarmu(Jarmu jarmu) {
        this.kivalasztottJarmu = jarmu;
        repaint();
    }

    public void setKivalasztottBusz(Busz busz) {
        this.kivalasztottBusz = busz;
        repaint();
    }

    public Map<Utegyseg, UtegysegView> getUtegysegViewk() { return utegysegViewk; }
    public Map<Jarmu, JarmuView> getJarmuViewk() { return jarmuViewk; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (UtView uv : utViewk) uv.kirajzol(g2);
        for (UtegysegView uev : utegysegViewk.values()) uev.kirajzol(g2);
        rajzolSavIranyok(g2);
        rajzolHatotav(g2);
        for (CsomopontView cv : csomopontViewk.values()) cv.kirajzol(g2);
        rajzolBuszAllomasok(g2);
        rajzolKijeloltek(g2);
        for (JarmuView jv : jarmuViewk.values()) jv.kirajzol(g2);

        if (controller != null) rajzolFazisJelzo(g2);
    }

    private void rajzolHatotav(Graphics2D g2) {
        if (kijelolhetoUtegysegek == null || kijelolhetoUtegysegek.isEmpty()) return;
        for (Utegyseg ue : kijelolhetoUtegysegek) {
            UtegysegView uev = utegysegViewk.get(ue);
            if (uev == null || uev.getBefoglaloTeglalap() == null) continue;
            Rectangle r = uev.getBefoglaloTeglalap();
            g2.setColor(new Color(70, 210, 110, 70));
            g2.fillRoundRect(r.x - 3, r.y - 3, r.width + 6, r.height + 6, 8, 8);
            g2.setColor(new Color(25, 145, 70, 190));
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(r.x - 3, r.y - 3, r.width + 6, r.height + 6, 8, 8);
            g2.setStroke(new BasicStroke(1));
        }
    }

    private void rajzolKijeloltek(Graphics2D g2) {
        if (kijeloltUtegysegek == null) return;
        for (Utegyseg ue : kijeloltUtegysegek) {
            UtegysegView uev = utegysegViewk.get(ue);
            if (uev == null || uev.getBefoglaloTeglalap() == null) continue;
            Rectangle r = uev.getBefoglaloTeglalap();
            g2.setColor(new Color(255, 215, 0, 180));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRoundRect(r.x - 2, r.y - 2, r.width + 4, r.height + 4, 6, 6);
            g2.setStroke(new BasicStroke(1));
        }
    }

    private void rajzolSavIranyok(Graphics2D g2) {
        if (layout == null) return;
        Map<Utegyseg, int[]> iranyok = layout.getUtegysegIranyok();
        for (Map.Entry<Utegyseg, UtegysegView> e : utegysegViewk.entrySet()) {
            Rectangle r = e.getValue().getBefoglaloTeglalap();
            int[] dir = iranyok.get(e.getKey());
            if (r == null || dir == null) continue;

            double dx = dir[0] / 1000.0;
            double dy = dir[1] / 1000.0;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len < 0.01) continue;
            dx /= len;
            dy /= len;

            int cx = r.x + r.width / 2;
            int cy = r.y + r.height / 2;
            int half = Math.max(5, Math.min(r.width, r.height) / 3);
            int x1 = (int) Math.round(cx - dx * half);
            int y1 = (int) Math.round(cy - dy * half);
            int x2 = (int) Math.round(cx + dx * half);
            int y2 = (int) Math.round(cy + dy * half);

            g2.setColor(new Color(24, 28, 38, 180));
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);

            double ax = -dx * 4;
            double ay = -dy * 4;
            double px = -dy * 3;
            double py = dx * 3;
            g2.drawLine(x2, y2, (int) Math.round(x2 + ax + px), (int) Math.round(y2 + ay + py));
            g2.drawLine(x2, y2, (int) Math.round(x2 + ax - px), (int) Math.round(y2 + ay - py));
            g2.setStroke(new BasicStroke(1));
        }
    }

    private void rajzolBuszAllomasok(Graphics2D g2) {
        if (kivalasztottBusz == null) return;
        rajzolCsomopontJeloles(g2, kivalasztottBusz.getVegallomas1(), "V1", new Color(255, 145, 35));
        rajzolCsomopontJeloles(g2, kivalasztottBusz.getVegallomas2(), "V2", new Color(255, 145, 35));
        for (Csomopont megallo : kivalasztottBusz.getMegallokLista()) {
            Color c = kivalasztottBusz.getErintettLista().contains(megallo)
                ? new Color(40, 180, 95)
                : new Color(55, 190, 220);
            rajzolCsomopontJeloles(g2, megallo, "M", c);
        }
    }

    private void rajzolCsomopontJeloles(Graphics2D g2, Csomopont cs, String label, Color color) {
        if (cs == null) return;
        CsomopontView cv = csomopontViewk.get(cs);
        if (cv == null) return;
        int x = cv.getX();
        int y = cv.getY();
        int r = 20;

        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 85));
        g2.fillOval(x - r, y - r, r * 2, r * 2);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(x - r, y - r, r * 2, r * 2);
        g2.setStroke(new BasicStroke(1));

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, x - fm.stringWidth(label) / 2, y - r - 4);
    }

    private void rajzolFazisJelzo(Graphics2D g2) {
        Fazis fazis = controller.getAktualisFazis();
        String szoveg;
        Color szin;
        switch (fazis) {
            case TERVEZES:
                szoveg = "TERVEZÉSI FÁZIS";
                if (controller.getKivalasztottJarmu() != null) {
                    szoveg += " – " + controller.getJarmuNev(controller.getKivalasztottJarmu())
                        + " | hatótáv: " + controller.getAktivJarmuHatotav()
                        + " | hátra: " + controller.getHatralevoHatotav();
                }
                szin   = new Color(80, 160, 80);
                break;
            case SZIMULACIO:
                szoveg = "SZIMULÁCIÓ FUT…";
                szin   = new Color(160, 100, 30);
                break;
            case BOLT:
                szoveg = "BOLT NYITVA";
                szin   = new Color(80, 80, 180);
                break;
            default:
                szoveg = "JÁTÉK VÉGE";
                szin   = new Color(180, 40, 40);
        }
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        int sw = fm.stringWidth(szoveg);
        int px = (getWidth() - sw) / 2;
        int py = getHeight() - 8;
        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(px - 6, py - fm.getAscent() - 2, sw + 12, fm.getHeight() + 4, 6, 6);
        g2.setColor(szin);
        g2.drawString(szoveg, px, py);
    }

    public Utegyseg utegysegKattintas(int mx, int my) {
        for (Map.Entry<Utegyseg, UtegysegView> e : utegysegViewk.entrySet()) {
            if (e.getValue().tartalmaz(mx, my)) return e.getKey();
        }
        return null;
    }

    public Jarmu jarmuKattintas(int mx, int my) {
        if (layout == null) return null;
        final int R = 14;
        for (Map.Entry<Jarmu, JarmuView> e : jarmuViewk.entrySet()) {
            Jarmu j = e.getKey();
            if (j.getUtegyseg() == null) continue;
            int[] pos = layout.getUtegysegPoziciok().get(j.getUtegyseg());
            if (pos != null && Math.hypot(mx - pos[0], my - pos[1]) <= R) return j;
        }
        return null;
    }

    public void ujraRajzol() { repaint(); }
}
