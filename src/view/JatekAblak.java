package view;

import controller.JatekController;
import controller.JatekController.Fazis;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A játék fő ablaka. CardLayout-tal vált a főmenü és a játékterület között.
 */
public class JatekAblak extends JFrame {

    private static final String CARD_MENU = "MENU";
    private static final String CARD_GAME = "GAME";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private JatekController controller;
    private TerkeploPanel terkeploPanel;
    private InfoPanel infoPanel;

    // Felső infosáv elemei (játéktér)
    private JLabel korLabel;
    private JLabel jatekosLabel;
    private JLabel jarmuLabel;

    // Alsó gombok
    private JButton utvonalKijelolBtn;
    private JButton takaritBtn;
    private JButton korVegeBtn;

    private Timer szimulacioTimer;

    public JatekAblak() {
        setTitle("Hókotrók – Zúzmaraváros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardPanel.add(buildFomenu(), CARD_MENU);
        // Játéktér panel (üres helytartó egyelőre, ujJatek() tölti fel)
        cardPanel.add(new JPanel(), CARD_GAME);

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, CARD_MENU);

        setSize(900, 620);
        setLocationRelativeTo(null);
    }

    private JPanel buildFomenu() {
        return new FomenuPanel(
            (nevek, szerepek) -> ujJatekIndit(nevek, szerepek),
            () -> System.exit(0)
        );
    }

    private void ujJatekIndit(String[] nevek, String[] szerepek) {
        if (controller == null) {
            controller = new JatekController();
        }
        if (szimulacioTimer != null) {
            szimulacioTimer.stop();
            szimulacioTimer = null;
        }
        controller.ujJatek(nevek.length, nevek);
        controller.addAllapotValtozoListener(this::frissitView);

        JPanel jatekPanel = buildJatekPanel();
        cardPanel.remove(1);
        cardPanel.add(jatekPanel, CARD_GAME);
        cardLayout.show(cardPanel, CARD_GAME);

        revalidate();
        repaint();
        frissitView();
    }

    private JPanel buildJatekPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // --- Felső infosáv ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(235, 238, 248));
        topBar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(180, 185, 210)),
            new EmptyBorder(6, 12, 6, 12)
        ));

        korLabel = new JLabel("Eltelt körök száma: 1");
        korLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        korLabel.setForeground(new Color(60, 60, 90));

        jatekosLabel = new JLabel("Aktív játékos: -");
        jatekosLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        jatekosLabel.setForeground(new Color(40, 40, 80));
        jatekosLabel.setHorizontalAlignment(SwingConstants.CENTER);

        jarmuLabel = new JLabel("Járművek 0/0");
        jarmuLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        jarmuLabel.setForeground(new Color(60, 60, 90));
        jarmuLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topBar.add(korLabel, BorderLayout.WEST);
        topBar.add(jatekosLabel, BorderLayout.CENTER);
        topBar.add(jarmuLabel, BorderLayout.EAST);

        // X gomb – visszalépés főmenübe
        JButton menuBtn = new JButton("x");
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
        menuBtn.setForeground(new Color(150, 150, 180));
        menuBtn.setBackground(new Color(235, 238, 248));
        menuBtn.setBorderPainted(false);
        menuBtn.setFocusPainted(false);
        menuBtn.setPreferredSize(new Dimension(22, 22));
        menuBtn.setToolTipText("Visszalépés a főmenübe");
        menuBtn.addActionListener(e -> visszaMenube());

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        topRight.setOpaque(false);
        topRight.add(jarmuLabel);
        topRight.add(menuBtn);
        topBar.add(topRight, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // --- Térkép ---
        terkeploPanel = new TerkeploPanel(controller);
        panel.add(terkeploPanel, BorderLayout.CENTER);

        // --- Jobb oldalsáv ---
        infoPanel = new InfoPanel(controller, this::boltMegnyit);
        panel.add(infoPanel, BorderLayout.EAST);

        // --- Alsó gombok ---
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        bottomBar.setBackground(new Color(235, 238, 248));
        bottomBar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(180, 185, 210)));

        utvonalKijelolBtn = alsogomb("Útvonal kijelölés");
        takaritBtn = alsogomb("takarít");
        korVegeBtn = alsogomb("Kör vége");

        utvonalKijelolBtn.addActionListener(e -> utvonalKijelolToggle());
        takaritBtn.addActionListener(e -> takaritAkcio());
        korVegeBtn.addActionListener(e -> korVegeAkcio());

        bottomBar.add(utvonalKijelolBtn);
        bottomBar.add(takaritBtn);
        bottomBar.add(korVegeBtn);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    private void visszaMenube() {
        if (szimulacioTimer != null) { szimulacioTimer.stop(); szimulacioTimer = null; }
        cardLayout.show(cardPanel, CARD_MENU);
    }

    private void utvonalKijelolToggle() {
        // Az útvonal-kijelölés mindig aktív tervezési fázisban (kattintás a térképen).
        // Ez a gomb csak figyelmeztet / visszajelzést ad.
        if (controller.getAktualisFazis() == Fazis.TERVEZES) {
            JOptionPane.showMessageDialog(this,
                "Kattints az útegységekre a térképen a kijelölt jármű útvonalának megtervezéséhez.",
                "Útvonal kijelölés", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void takaritAkcio() {
        Fazis fazis = controller.getAktualisFazis();
        if (fazis == Fazis.SZIMULACIO) {
            controller.szimulacioTick();
        } else if (fazis == Fazis.TERVEZES) {
            // Autorun: teljesíti az egy kört szimulációval
            controller.korBefejezes();
            autoSzimulacioStart();
        }
    }

    private void korVegeAkcio() {
        Fazis fazis = controller.getAktualisFazis();
        if (fazis == Fazis.TERVEZES) {
            controller.korBefejezes();
        } else if (fazis == Fazis.SZIMULACIO) {
            if (szimulacioTimer != null && szimulacioTimer.isRunning()) {
                szimulacioTimer.stop();
                szimulacioTimer = null;
            } else {
                autoSzimulacioStart();
            }
        }
    }

    private void autoSzimulacioStart() {
        if (szimulacioTimer != null && szimulacioTimer.isRunning()) return;
        szimulacioTimer = new Timer(500, (ActionEvent e) -> {
            if (controller.getAktualisFazis() != Fazis.SZIMULACIO) {
                szimulacioTimer.stop();
                szimulacioTimer = null;
                frissitGombok();
                return;
            }
            controller.szimulacioTick();
        });
        szimulacioTimer.start();
        frissitGombok();
    }

    private void boltMegnyit() {
        if (controller.getAktualisFazis() == Fazis.JATEK_VEGE) return;
        BoltDialog dialog = new BoltDialog(this, controller);
        dialog.setVisible(true);
        frissitView();
    }

    private void frissitView() {
        if (controller == null) return;

        if (terkeploPanel != null) terkeploPanel.repaint();
        if (infoPanel != null) infoPanel.frissit();
        frissitTopBar();
        frissitGombok();

        if (controller.getAktualisFazis() == Fazis.JATEK_VEGE) {
            if (szimulacioTimer != null) { szimulacioTimer.stop(); szimulacioTimer = null; }
            JOptionPane.showMessageDialog(this,
                "A játék végetért!\nTúl sok NPC nem ért el a célba.",
                "Játék vége", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void frissitTopBar() {
        if (korLabel == null) return;
        korLabel.setText("Eltelt körök száma: " + (controller.getAktualisKor() - 1));
        jatekosLabel.setText("Aktív játékos: " + controller.getAktivJatekosNev());
        int osszes = controller.getJarmuekOsszesenSzama();
        Fazis fazis = controller.getAktualisFazis();
        String fazisRovid = fazis == Fazis.TERVEZES ? " [TERVEZÉS]"
                          : fazis == Fazis.SZIMULACIO ? " [SZIMULÁCIÓ]" : " [VÉGE]";
        jarmuLabel.setText("Járművek " + osszes + "/" + osszes + fazisRovid);
    }

    private void frissitGombok() {
        if (korVegeBtn == null) return;
        Fazis fazis = controller.getAktualisFazis();
        boolean tervezes = fazis == Fazis.TERVEZES;
        boolean szimulacio = fazis == Fazis.SZIMULACIO;

        utvonalKijelolBtn.setEnabled(tervezes);

        if (szimulacio && szimulacioTimer != null && szimulacioTimer.isRunning()) {
            takaritBtn.setText("⏹ Stop");
            takaritBtn.setBackground(new Color(230, 130, 130));
            korVegeBtn.setEnabled(false);
        } else {
            takaritBtn.setText("takarít");
            takaritBtn.setBackground(new Color(150, 230, 150));
            korVegeBtn.setEnabled(tervezes || szimulacio);
        }
        takaritBtn.setEnabled(fazis != Fazis.JATEK_VEGE);
    }

    private JButton alsogomb(String szoveg) {
        JButton btn = new JButton(szoveg);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBackground(new Color(150, 230, 150));
        btn.setForeground(new Color(20, 60, 20));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        btn.setPreferredSize(new Dimension(160, 34));
        return btn;
    }
}
