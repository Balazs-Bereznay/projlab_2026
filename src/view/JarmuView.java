package view;

import model.Jarmu;
import java.awt.*;
import java.util.Map;

public abstract class JarmuView extends View {
    protected final Jarmu jarmu;
    protected final Map<model.Utegyseg, int[]> utegysegPoziciok;

    public JarmuView(Jarmu jarmu, PalyaPanel panel, Map<model.Utegyseg, int[]> poz) {
        super(jarmu, panel);
        this.jarmu = jarmu;
        this.utegysegPoziciok = poz;
    }

    protected int[] getPozicio() {
        if (jarmu.getUtegyseg() == null) return null;
        return utegysegPoziciok.get(jarmu.getUtegyseg());
    }

    @Override
    public abstract void kirajzol(Graphics2D g);
}
