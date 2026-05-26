package controller;

import model.Iranyithato;
import model.Jarmu;
import model.Utegyseg;
import view.PalyaPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BemenetKezelo extends MouseAdapter implements KeyListener {

    private final JatekController controller;
    private final PalyaPanel palyaPanel;

    public BemenetKezelo(JatekController controller, PalyaPanel palyaPanel) {
        this.controller = controller;
        this.palyaPanel = palyaPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (controller.getAktualisFazis() == Fazis.BOLT) return;

        int x = e.getX(), y = e.getY();

        if (e.getClickCount() == 2) {
            controller.utvonalVeglegesit();
            return;
        }

        Jarmu j = palyaPanel.jarmuKattintas(x, y);
        if (j instanceof Iranyithato) {
            return;
        }

        Utegyseg ue = palyaPanel.utegysegKattintas(x, y);
        if (ue != null) {
            controller.utegysegValasztva(ue);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                if (controller.getAktualisFazis() == Fazis.TERVEZES)
                    controller.korVegeKattintas();
                break;
            case KeyEvent.VK_B:
                if (controller.getAktualisFazis() == Fazis.BOLT)
                    controller.boltBezar();
                else
                    controller.boltNyit();
                break;
            case KeyEvent.VK_ESCAPE:
                if (controller.getAktualisFazis() == Fazis.BOLT) {
                    controller.boltBezar();
                } else {
                    controller.tervTorol(controller.getKivalasztottJarmu());
                }
                break;
            case KeyEvent.VK_F:
                controller.utvonalVeglegesit();
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
