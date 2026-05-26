package view;

import common.Megfigyelheto;
import common.Megfigyelo;
import java.awt.Graphics2D;

public abstract class View implements Megfigyelo {
    protected Megfigyelheto modellElem;
    protected PalyaPanel palyaPanel;

    public View(Megfigyelheto elem, PalyaPanel panel) {
        this.modellElem = elem;
        this.palyaPanel = panel;
    }

    @Override
    public void frissit() {
        if (palyaPanel != null) palyaPanel.repaint();
    }

    public abstract void kirajzol(Graphics2D g);
}
