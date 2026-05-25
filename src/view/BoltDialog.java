package view;

import controller.JatekController;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * Bolt ablak – kategória-alapú vásárlási felület.
 */
public class BoltDialog extends JDialog {

    private static final Color ROSA = new Color(255, 200, 210);
    private static final Color KINALAT_BG = new Color(255, 210, 215);
    private static final Color DETAIL_BG = new Color(210, 230, 255);
    private static final Color ZOLD_GOMB = new Color(150, 230, 150);

    private final JatekController controller;

    // Kategória lista panel
    private final JPanel kategoriaPanel;
    // Kínálat panel
    private final JPanel kinalatPanel;
    // Részletek panel
    private final JLabel kivalasztvaLabel = new JLabel("Kiválasztva: -");
    private final JLabel leirasLabel = new JLabel(" ");
    private final JComboBox<String> hokotroSelectorCB;
    private final JButton megveszBtn;

    // Aktuális vásárlási akció
    private Runnable aktualisVasarlasAkcio = null;
    private String kivalasztottKategoria = "Fejek";

    public BoltDialog(Frame owner, JatekController controller) {
        super(owner, "Bolt", true);
        this.controller = controller;

        setSize(640, 480);
        setLocationRelativeTo(owner);
        setResizable(false);
        setLayout(new BorderLayout());

        // Fejléc
        JPanel fejlec = new JPanel(new BorderLayout());
        fejlec.setBackground(Color.WHITE);
        fejlec.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel cim = new JLabel("Bolt");
        cim.setFont(new Font("Serif", Font.PLAIN, 22));
        fejlec.add(cim, BorderLayout.WEST);

        JButton bezarBtn = new JButton("X");
        bezarBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        bezarBtn.setBackground(new Color(150, 220, 150));
        bezarBtn.setForeground(new Color(30, 80, 30));
        bezarBtn.setBorderPainted(true);
        bezarBtn.setFocusPainted(false);
        bezarBtn.setPreferredSize(new Dimension(36, 28));
        bezarBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        bezarBtn.addActionListener(e -> dispose());
        fejlec.add(bezarBtn, BorderLayout.EAST);
        add(fejlec, BorderLayout.NORTH);

        // --- Középső rész: kategória + kínálat ---
        JPanel kozep = new JPanel(new BorderLayout(0, 0));
        kozep.setBackground(Color.WHITE);

        // Bal: kategória gombok
        kategoriaPanel = new JPanel();
        kategoriaPanel.setLayout(new BoxLayout(kategoriaPanel, BoxLayout.Y_AXIS));
        kategoriaPanel.setBackground(Color.WHITE);
        kategoriaPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 0, 1, new Color(200, 200, 210)),
            new EmptyBorder(14, 12, 14, 12)
        ));
        kategoriaPanel.setPreferredSize(new Dimension(150, 0));

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

        // Jobb: kínálat
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

        JScrollPane kinalatScroll = new JScrollPane(kinalatPanel);
        kinalatScroll.setBorder(null);
        kinalatScroll.setBackground(Color.WHITE);
        kinalatTartaly.add(kinalatScroll, BorderLayout.CENTER);
        kozep.add(kinalatTartaly, BorderLayout.CENTER);

        add(kozep, BorderLayout.CENTER);

        // --- Alsó rész: részletek + gombok ---
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
        detailPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Hókotró selector (csak Fejek kategorianál látható)
        JPanel hokotroSorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        hokotroSorPanel.setOpaque(false);
        JLabel hokotroSorLabel = new JLabel("Felszerelendő hókotró:");
        hokotroSorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hokotroSorPanel.add(hokotroSorLabel);

        List<Hokotro> hokotrók = controller.getHokotrók();
        String[] hokotroCBOptions = new String[hokotrók.size()];
        for (int i = 0; i < hokotrók.size(); i++) hokotroCBOptions[i] = "Hókotró " + (i + 1);
        if (hokotroCBOptions.length == 0) hokotroCBOptions = new String[]{"Nincs hókotró"};

        hokotroSelectorCB = new JComboBox<>(hokotroCBOptions);
        hokotroSelectorCB.setBackground(new Color(230, 240, 255));
        hokotroSelectorCB.setPreferredSize(new Dimension(130, 26));
        hokotroSorPanel.add(hokotroSelectorCB);
        detailPanel.add(hokotroSorPanel);

        also.add(detailPanel, BorderLayout.CENTER);

        // Gombok
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
        megveszBtn.addActionListener(e -> {
            if (aktualisVasarlasAkcio != null) aktualisVasarlasAkcio.run();
        });

        JButton megseBtn = new JButton("Mégse");
        megseBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        megseBtn.setBackground(ZOLD_GOMB);
        megseBtn.setForeground(new Color(20, 60, 20));
        megseBtn.setBorderPainted(true);
        megseBtn.setFocusPainted(false);
        megseBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        megseBtn.setPreferredSize(new Dimension(110, 34));
        megseBtn.addActionListener(e -> dispose());

        gombokPanel.add(megveszBtn);
        gombokPanel.add(megseBtn);

        also.add(gombokPanel, BorderLayout.SOUTH);
        add(also, BorderLayout.SOUTH);

        betolKinalat("Fejek");
    }

    private JButton kategoriaGomb(String kat) {
        JButton btn = new JButton(kat);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(ROSA);
        btn.setForeground(new Color(60, 20, 30));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 140, 150), 1, true));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            kivalasztottKategoria = kat;
            betolKinalat(kat);
            frissitKategoriaGombok(kat);
        });
        return btn;
    }

    private void frissitKategoriaGombok(String aktiv) {
        for (Component c : kategoriaPanel.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                if (btn.getText().equals(aktiv)) {
                    btn.setBackground(ROSA.darker());
                } else {
                    btn.setBackground(ROSA);
                }
            }
        }
        kategoriaPanel.repaint();
    }

    private void betolKinalat(String kategoria) {
        kinalatPanel.removeAll();
        aktualisVasarlasAkcio = null;
        megveszBtn.setEnabled(false);
        kivalasztvaLabel.setText("Kiválasztva: -");
        leirasLabel.setText(" ");

        Bolt bolt = controller.getBolt();
        List<Hokotro> hokotrók = controller.getHokotrók();
        List<Busz> buszok = controller.getBuszok();
        boolean vanHokotro = !hokotrók.isEmpty();
        boolean vanBusz = !buszok.isEmpty();

        switch (kategoria) {
            case "Fejek":
                hokotroSelectorCB.setVisible(true);
                add("Söprő fej", bolt.getSeproAr() + " Ft",
                    "A havat és a feltört jeget a jobb oldali sávba tolja át.",
                    () -> {
                        Hokotro hk = kivalasztottHokotro();
                        if (hk != null) bolt.soproVasarol(hk);
                    }, vanHokotro);
                add("Hányó fej", bolt.getHanyoAr() + " Ft",
                    "A havat a levegőbe hajítja, tisztán hagyva az útegységet.",
                    () -> {
                        Hokotro hk = kivalasztottHokotro();
                        if (hk != null) bolt.hanyoVasarol(hk);
                    }, vanHokotro);
                add("Jégtörő fej", bolt.getJegtoroAr() + " Ft",
                    "A jeget feltöri, így az eltávolítható hányó fejjel.",
                    () -> {
                        Hokotro hk = kivalasztottHokotro();
                        if (hk != null) bolt.jegtoroVasarol(hk);
                    }, vanHokotro);
                add("Sószóró fej", bolt.getSoszoroAr() + " Ft",
                    "Sót szór az útegységre, amely megolvasztja a havat és a jeget.",
                    () -> {
                        Hokotro hk = kivalasztottHokotro();
                        if (hk != null) bolt.soszoroVasarol(hk);
                    }, vanHokotro);
                break;

            case "Erőforrás":
                hokotroSelectorCB.setVisible(false);
                add("Só (+10 egység)", (bolt.getSoAr() * 10) + " Ft",
                    "Só vásárlása. A sószóró fej sót használ a takarításhoz.",
                    () -> bolt.soVasarol(10), true);
                add("Biokerozin (+5 egység)", (bolt.getBiokerozinAr() * 5) + " Ft",
                    "Biokerozin vásárlása. A jégtörő fej biokerozint használ.",
                    () -> bolt.biokerozinVasarol(5), true);
                break;

            case "Buszfejlesztés":
                hokotroSelectorCB.setVisible(false);
                add("Sebességfejlesztés (+10)", "150 Ft",
                    "A busz sebességét 10 egységgel növeli.",
                    () -> { if (vanBusz) bolt.sebessegFejlesztes(buszok.get(0), 10); }, vanBusz);
                add("Tapadásfejlesztés (+10)", "120 Ft",
                    "A busz tapadását 10 egységgel növeli, csökkentve a megcsúszás esélyét.",
                    () -> { if (vanBusz) bolt.tapadasFejlesztes(buszok.get(0), 10); }, vanBusz);
                add("Hozamfejlesztés (+10)", "200 Ft",
                    "A busz hozamát 10 egységgel növeli, több bevételt termelve.",
                    () -> { if (vanBusz) bolt.hozamFejlesztes(buszok.get(0), 10); }, vanBusz);
                break;

            case "Új jármű":
                hokotroSelectorCB.setVisible(false);
                JLabel stub = new JLabel("  (hamarosan elérhető)");
                stub.setFont(new Font("SansSerif", Font.ITALIC, 12));
                stub.setForeground(new Color(140, 140, 160));
                kinalatPanel.add(stub);
                break;
        }

        kinalatPanel.revalidate();
        kinalatPanel.repaint();
    }

    private void add(String nev, String ar, String leiras, Runnable akcio, boolean elerheto) {
        JPanel sor = new JPanel(new BorderLayout(8, 0));
        sor.setBackground(KINALAT_BG);
        sor.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(220, 180, 185)),
            new EmptyBorder(6, 8, 6, 8)
        ));
        sor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        sor.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nevLabel = new JLabel(nev);
        nevLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        nevLabel.setForeground(elerheto ? new Color(40, 20, 30) : new Color(160, 140, 150));

        JLabel arLabel = new JLabel(ar);
        arLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        arLabel.setForeground(elerheto ? new Color(40, 20, 30) : new Color(160, 140, 150));
        arLabel.setPreferredSize(new Dimension(70, 20));
        arLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        sor.add(nevLabel, BorderLayout.CENTER);
        sor.add(arLabel, BorderLayout.EAST);

        if (elerheto) {
            sor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            sor.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    kivalasztvaLabel.setText("Kiválasztva: " + nev);
                    leirasLabel.setText("\"" + leiras + "\"");
                    aktualisVasarlasAkcio = akcio;
                    megveszBtn.setEnabled(true);
                }
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    sor.setBackground(KINALAT_BG.darker());
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    sor.setBackground(KINALAT_BG);
                }
            });
        }

        kinalatPanel.add(sor);
    }

    private Hokotro kivalasztottHokotro() {
        List<Hokotro> hokotrók = controller.getHokotrók();
        int idx = hokotroSelectorCB.getSelectedIndex();
        if (idx >= 0 && idx < hokotrók.size()) return hokotrók.get(idx);
        return null;
    }
}
