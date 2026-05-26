package view;

import controller.BemenetKezelo;
import controller.Fazis;
import controller.JatekController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class JatekAblak extends JFrame {

    private static final String CARD_MENU = "MENU";
    private static final String CARD_GAME = "GAME";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final JatekController controller;
    private final MenuPanel menuPanel;
    private final PalyaPanel palyaPanel;
    private final HUDPanel hudPanel;
    private final AkcioSor akcioSor;
    private final BoltPanel boltPanel;
    private JDialog boltDialog;

    private JLabel korLabel;
    private JLabel jatekosLabel;
    private JLabel jarmuLabel;

    public JatekAblak(JatekController controller) {
        this.controller = controller;

        palyaPanel = new PalyaPanel();
        menuPanel  = new MenuPanel(controller);
        hudPanel   = new HUDPanel(controller, () -> controller.boltNyit());
        akcioSor   = new AkcioSor(controller);
        boltPanel  = new BoltPanel(controller);

        setTitle("Hókotrók – Zúzmaraváros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 580));

        cardPanel.add(menuPanel, CARD_MENU);
        cardPanel.add(buildJatekPanel(), CARD_GAME);
        add(cardPanel, BorderLayout.CENTER);

        setSize(1200, 760);
        setLocationRelativeTo(null);
    }

    private JPanel buildJatekPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(42, 44, 54));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(235, 238, 248));
        topBar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(180, 185, 210)),
            new EmptyBorder(6, 12, 6, 12)
        ));
        korLabel = new JLabel("Eltelt körök: 0");
        korLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        korLabel.setForeground(new Color(60, 60, 90));
        jatekosLabel = new JLabel("Aktív játékos: -", SwingConstants.CENTER);
        jatekosLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        jatekosLabel.setForeground(new Color(40, 40, 80));
        jarmuLabel = new JLabel("Járművek 0/0", SwingConstants.RIGHT);
        jarmuLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        jarmuLabel.setForeground(new Color(60, 60, 90));

        JButton menuBtn = new JButton("↩ Menü");
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        menuBtn.setForeground(new Color(80, 80, 120));
        menuBtn.setBackground(new Color(235, 238, 248));
        menuBtn.setBorderPainted(false);
        menuBtn.setFocusPainted(false);
        menuBtn.addActionListener(e -> mutatMenu());

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        topRight.setOpaque(false);
        topRight.add(jarmuLabel);
        topRight.add(menuBtn);

        topBar.add(korLabel, BorderLayout.WEST);
        topBar.add(jatekosLabel, BorderLayout.CENTER);
        topBar.add(topRight, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        panel.add(palyaPanel, BorderLayout.CENTER);
        panel.add(hudPanel,   BorderLayout.EAST);
        panel.add(akcioSor,   BorderLayout.SOUTH);

        return panel;
    }

    public void mutatMenu() {
        if (boltDialog != null) boltDialog.setVisible(false);
        cardLayout.show(cardPanel, CARD_MENU);
    }

    public void mutatJatek() {
        if (boltDialog != null) boltDialog.setVisible(false);
        cardLayout.show(cardPanel, CARD_GAME);
        Fazis f = controller.getAktualisFazis();
        akcioSor.fazisFrissites(f != null ? f : Fazis.TERVEZES);
        frissitTopBar();
        hudPanel.frissit();
        palyaPanel.repaint();
    }

    public void mutatBolt() {
        if (boltDialog == null || !boltDialog.isDisplayable()) {
            boltDialog = new JDialog(this, "Bolt", false);
            boltDialog.setContentPane(boltPanel);
            boltDialog.setSize(680, 520);
            boltDialog.setLocationRelativeTo(this);
        }
        boltPanel.frissit();
        boltDialog.setVisible(true);
    }

    public void frissitJatek() {
        frissitTopBar();
        Fazis f = controller.getAktualisFazis();
        if (f != null) akcioSor.fazisFrissites(f);
        hudPanel.frissit();
        palyaPanel.repaint();
    }

    private void frissitTopBar() {
        if (korLabel == null) return;
        int kor = controller.getAktualisKor();
        korLabel.setText("Eltelt körök: " + (kor > 0 ? kor - 1 : 0));
        jatekosLabel.setText("Aktív játékos: " + controller.getAktivJatekosNev());
        int osszes = controller.getJarmuekOsszesenSzama();
        jarmuLabel.setText("Járművek " + osszes + "/" + osszes);
    }

    public void regisztraljBemenetKezelo() {
        for (java.awt.event.MouseListener ml : palyaPanel.getMouseListeners()) {
            palyaPanel.removeMouseListener(ml);
        }
        for (java.awt.event.KeyListener kl : palyaPanel.getKeyListeners()) {
            palyaPanel.removeKeyListener(kl);
        }
        BemenetKezelo bk = new BemenetKezelo(controller, palyaPanel);
        palyaPanel.addMouseListener(bk);
        palyaPanel.addKeyListener(bk);
        palyaPanel.setFocusable(true);
        palyaPanel.requestFocusInWindow();
    }

    public void ujraRajzol() {
        frissitTopBar();
        palyaPanel.repaint();
    }

    public PalyaPanel getPalyaPanel() { return palyaPanel; }
    public HUDPanel   getHudPanel()   { return hudPanel; }
    public AkcioSor   getAkcioSor()   { return akcioSor; }
    public BoltPanel  getBoltPanel()  { return boltPanel; }
    public MenuPanel  getMenuPanel()  { return menuPanel; }
}
