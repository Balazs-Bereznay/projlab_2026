package view;

import common.Megfigyelo;
import controller.JatekController;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BoltPanel extends JPanel implements Megfigyelo {

    private static final Color KINALAT_BG = new Color(255, 210, 215);
    private static final Color DETAIL_BG  = new Color(210, 230, 255);
    private static final Color ZOLD_GOMB  = new Color(150, 230, 150);
    private static final Color KAT_SZIN   = new Color(255, 200, 210);

    private final JatekController controller;
    private Jatekos aktualisJatekos;

    private final JPanel kategoriaPanel;
    private final JPanel kinalatPanel;
    private final JLabel kivalasztvaLabel = new JLabel("Kiválasztva: -");
    private final JLabel leirasLabel      = new JLabel(" ");
    private final JLabel egyenlegLabel    = new JLabel();
    private final JComboBox<String> celCB = new JComboBox<>();
    private final JButton megveszBtn;

    private Runnable vasarlasAkcio = null;
    private String kivalasztottKategoria = "Fejek";

    public BoltPanel(JatekController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel fejlec = new JPanel(new BorderLayout());
        fejlec.setBackground(Color.WHITE);
        fejlec.setBorder(new EmptyBorder(8, 14, 8, 14));
        JLabel cim = new JLabel("Bolt");
        cim.setFont(new Font("Serif", Font.PLAIN, 22));
        fejlec.add(cim, BorderLayout.WEST);
        egyenlegLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        egyenlegLabel.setForeground(new Color(60, 60, 90));
        fejlec.add(egyenlegLabel, BorderLayout.CENTER);
        JButton bezarBtn = kisGomb("X");
        bezarBtn.addActionListener(e -> controller.boltBezar());
        fejlec.add(bezarBtn, BorderLayout.EAST);
        add(fejlec, BorderLayout.NORTH);

        JPanel kozep = new JPanel(new BorderLayout(0, 0));
        kozep.setBackground(Color.WHITE);

        kategoriaPanel = new JPanel();
        kategoriaPanel.setLayout(new BoxLayout(kategoriaPanel, BoxLayout.Y_AXIS));
        kategoriaPanel.setBackground(Color.WHITE);
        kategoriaPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 0, 1, new Color(200, 200, 210)),
            new EmptyBorder(14, 12, 14, 12)
        ));
        kategoriaPanel.setPreferredSize(new Dimension(155, 0));
        JLabel katCim = new JLabel("Kategória");
        katCim.setFont(new Font("SansSerif", Font.PLAIN, 11));
        katCim.setForeground(new Color(100, 100, 120));
        katCim.setAlignmentX(Component.CENTER_ALIGNMENT);
        kategoriaPanel.add(katCim);
        kategoriaPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        for (String kat : new String[]{"Fejek", "Erőforrás", "Buszfejlesztés", "Új jármű"}) {
            kategoriaPanel.add(kategoriaGomb(kat));
            kategoriaPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        kozep.add(kategoriaPanel, BorderLayout.WEST);

        JPanel kinalatTartaly = new JPanel(new BorderLayout());
        kinalatTartaly.setBackground(Color.WHITE);
        JLabel kinalatCim = new JLabel("Kínálat");
        kinalatCim.setFont(new Font("SansSerif", Font.PLAIN, 11));
        kinalatCim.setForeground(new Color(100, 100, 120));
        kinalatCim.setBorder(new EmptyBorder(14, 12, 4, 12));
        kinalatTartaly.add(kinalatCim, BorderLayout.NORTH);
        kinalatPanel = new JPanel();
        kinalatPanel.setLayout(new BoxLayout(kinalatPanel, BoxLayout.Y_AXIS));
        kinalatPanel.setBackground(Color.WHITE);
        kinalatPanel.setBorder(new EmptyBorder(0, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(kinalatPanel);
        scroll.setBorder(null);
        kinalatTartaly.add(scroll, BorderLayout.CENTER);
        kozep.add(kinalatTartaly, BorderLayout.CENTER);
        add(kozep, BorderLayout.CENTER);

        JPanel also = new JPanel(new BorderLayout(0, 0));
        also.setBackground(DETAIL_BG);
        also.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(150, 170, 210)),
            new EmptyBorder(10, 14, 10, 14)
        ));

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);
        kivalasztvaLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        kivalasztvaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(kivalasztvaLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        leirasLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        leirasLabel.setForeground(new Color(60, 60, 80));
        leirasLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(leirasLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        celCB.setVisible(false);
        celCB.setAlignmentX(Component.LEFT_ALIGNMENT);
        celCB.setMaximumSize(new Dimension(180, 28));
        detailPanel.add(celCB);
        also.add(detailPanel, BorderLayout.CENTER);

        JPanel gombokPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        gombokPanel.setOpaque(false);
        megveszBtn = new JButton("Megvesz");
        megveszBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        megveszBtn.setBackground(ZOLD_GOMB);
        megveszBtn.setForeground(new Color(20, 60, 20));
        megveszBtn.setBorderPainted(true);
        megveszBtn.setFocusPainted(false);
        megveszBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        megveszBtn.setPreferredSize(new Dimension(110, 34));
        megveszBtn.setEnabled(false);
        megveszBtn.addActionListener(e -> { if (vasarlasAkcio != null) vasarlasAkcio.run(); });
        JButton megseBtn = new JButton("Mégse");
        megseBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        megseBtn.setBackground(ZOLD_GOMB);
        megseBtn.setForeground(new Color(20, 60, 20));
        megseBtn.setBorderPainted(true);
        megseBtn.setFocusPainted(false);
        megseBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        megseBtn.setPreferredSize(new Dimension(110, 34));
        megseBtn.addActionListener(e -> controller.boltBezar());
        gombokPanel.add(megveszBtn);
        gombokPanel.add(megseBtn);
        also.add(gombokPanel, BorderLayout.SOUTH);
        add(also, BorderLayout.SOUTH);

        betolKinalat("Fejek");
    }

    public void setAktualisJatekos(Jatekos j) {
        this.aktualisJatekos = j;
        frissit();
    }

    @Override
    public void frissit() {
        Nyilvantarto ny = controller.getNyilvantarto();
        if (ny != null) egyenlegLabel.setText("  Kassza: " + ny.getPenz() + " T");
        betolKinalat(kivalasztottKategoria);
    }

    private JButton kategoriaGomb(String kat) {
        JButton btn = new JButton(kat);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(KAT_SZIN);
        btn.setForeground(new Color(60, 20, 30));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 140, 150), 1, true));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            kivalasztottKategoria = kat;
            betolKinalat(kat);
            highlightKatGomb(kat);
        });
        return btn;
    }

    private void highlightKatGomb(String aktiv) {
        for (Component c : kategoriaPanel.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                b.setBackground(b.getText().equals(aktiv) ? KAT_SZIN.darker() : KAT_SZIN);
            }
        }
        kategoriaPanel.repaint();
    }

    private void betolKinalat(String kategoria) {
        kinalatPanel.removeAll();
        vasarlasAkcio = null;
        megveszBtn.setEnabled(false);
        kivalasztvaLabel.setText("Kiválasztva: -");
        leirasLabel.setText(" ");
        celCB.setVisible(false);

        Bolt bolt = controller.getBolt();
        if (bolt == null) { kinalatPanel.revalidate(); kinalatPanel.repaint(); return; }

        List<Hokotro> hokotrók = controller.getHokotrók();
        List<Busz> buszok = controller.getBuszok();

        switch (kategoria) {
            case "Fejek":
                hozzaadSor("Söprő", bolt.getSeproAr(),
                    "A havat a jobb oldali sávba tolja át.",
                    () -> vasarolFejjel("Fejek", "Söprő"), !hokotrók.isEmpty());
                hozzaadSor("Hányó", bolt.getHanyoAr(),
                    "A havat a levegőbe hajítja.",
                    () -> vasarolFejjel("Fejek", "Hányó"), !hokotrók.isEmpty());
                hozzaadSor("Jégtörő", bolt.getJegtoroAr(),
                    "A jeget feltöri, ezután eltávolítható.",
                    () -> vasarolFejjel("Fejek", "Jégtörő"), !hokotrók.isEmpty());
                hozzaadSor("Sószóró", bolt.getSoszoroAr(),
                    "Sót szór az útegységre.",
                    () -> vasarolFejjel("Fejek", "Sószóró"), !hokotrók.isEmpty());
                hozzaadSor("Sárkány", bolt.getSarkanyAr(),
                    "Lángszóróval olvasztja el a jeget és havat.",
                    () -> vasarolFejjel("Fejek", "Sárkány"), !hokotrók.isEmpty());
                hozzaadSor("Zúzalékszóró", bolt.getZuzalekszoroAr(),
                    "Zúzalékot szór az útegységre.",
                    () -> vasarolFejjel("Fejek", "Zúzalékszóró"), !hokotrók.isEmpty());
                break;
            case "Erőforrás":
                hozzaadSor("Só (+10)", bolt.getSoAr() * 10,
                    "10 egység só vásárlása.",
                    () -> controller.vasarol("Erőforrás", "Só", null, 10), true);
                hozzaadSor("Biokerozin (+5)", bolt.getBiokerozinAr() * 5,
                    "5 egység biokerozin vásárlása.",
                    () -> controller.vasarol("Erőforrás", "Biokerozin", null, 5), true);
                break;
            case "Buszfejlesztés":
                hozzaadSor("Sebesség (+10)", bolt.getSebessegfejlesztesAr(),
                    "A busz sebességét 10-zel növeli.",
                    () -> vasarolBusszal("Buszfejlesztés", "Sebesség"), !buszok.isEmpty());
                hozzaadSor("Tapadás (+10)", bolt.getTapadasfejlesztesAr(),
                    "A busz tapadását 10-zel növeli.",
                    () -> vasarolBusszal("Buszfejlesztés", "Tapadás"), !buszok.isEmpty());
                hozzaadSor("Hozam (+10)", bolt.getHozamfejlesztesAr(),
                    "A busz hozamát 10-zel növeli.",
                    () -> vasarolBusszal("Buszfejlesztés", "Hozam"), !buszok.isEmpty());
                break;
            case "Új jármű":
                hozzaadSor("Hókotró", bolt.gethokotroAr(),
                    "Új hókotró a flottához (alap Söprő fejjel).",
                    () -> controller.vasarol("Új jármű", "Hókotró", null, 1), true);
                break;
        }

        kinalatPanel.revalidate();
        kinalatPanel.repaint();
    }

    private void vasarolFejjel(String kat, String aru) {
        List<Hokotro> hk = controller.getHokotrók();
        int idx = celCB.getSelectedIndex();
        if (idx >= 0 && idx < hk.size()) {
            controller.vasarol(kat, aru, hk.get(idx), 1);
        }
    }

    private void vasarolBusszal(String kat, String aru) {
        List<Busz> buszok = controller.getBuszok();
        int idx = celCB.getSelectedIndex();
        if (idx >= 0 && idx < buszok.size()) {
            controller.vasarol(kat, aru, buszok.get(idx), 10);
        }
    }

    private void hozzaadSor(String nev, int ar, String leiras, Runnable akcio, boolean enabled) {
        JPanel sor = new JPanel(new BorderLayout(8, 0));
        sor.setBackground(KINALAT_BG);
        sor.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(220, 180, 185)),
            new EmptyBorder(6, 8, 6, 8)
        ));
        sor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        sor.setAlignmentX(Component.LEFT_ALIGNMENT);

        Color fg = enabled ? new Color(40, 20, 30) : new Color(160, 140, 150);
        JLabel nevL = new JLabel(nev);
        nevL.setFont(new Font("SansSerif", Font.PLAIN, 12));
        nevL.setForeground(fg);
        JLabel arL = new JLabel(ar + " Ft");
        arL.setFont(new Font("SansSerif", Font.PLAIN, 12));
        arL.setForeground(fg);
        arL.setPreferredSize(new Dimension(80, 20));
        arL.setHorizontalAlignment(SwingConstants.RIGHT);
        sor.add(nevL, BorderLayout.CENTER);
        sor.add(arL,  BorderLayout.EAST);

        if (enabled) {
            sor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            sor.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    kivalasztvaLabel.setText("Kiválasztva: " + nev);
                    leirasLabel.setText("\"" + leiras + "\"");
                    vasarlasAkcio = akcio;
                    megveszBtn.setEnabled(true);
                    frissitCelCB(kivalasztottKategoria);
                }
                @Override public void mouseEntered(MouseEvent e) { sor.setBackground(KINALAT_BG.darker()); }
                @Override public void mouseExited(MouseEvent e)  { sor.setBackground(KINALAT_BG); }
            });
        }
        kinalatPanel.add(sor);
    }

    private void frissitCelCB(String kat) {
        celCB.removeAllItems();
        if ("Fejek".equals(kat) || "Erőforrás".equals(kat)) {
            List<Hokotro> hk = controller.getHokotrók();
            for (int i = 0; i < hk.size(); i++) celCB.addItem("Hókotró " + (i + 1));
            celCB.setVisible(!hk.isEmpty() && ("Fejek".equals(kat) || "Zúzalék".equals(kivalasztvaLabel.getText())));
        } else if ("Buszfejlesztés".equals(kat)) {
            List<Busz> b = controller.getBuszok();
            for (int i = 0; i < b.size(); i++) celCB.addItem("Busz " + (i + 1));
            celCB.setVisible(!b.isEmpty());
        } else {
            celCB.setVisible(false);
        }
    }

    private JButton kisGomb(String szoveg) {
        JButton btn = new JButton(szoveg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(ZOLD_GOMB);
        btn.setForeground(new Color(30, 80, 30));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(36, 28));
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        return btn;
    }
}
