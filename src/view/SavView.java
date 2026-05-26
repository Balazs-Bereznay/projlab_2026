package view;

import model.Sav;
import model.Utegyseg;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class SavView {
    private final Sav sav;
    private final List<UtegysegView> utegysegViewk = new ArrayList<>();

    public SavView(Sav sav) {
        this.sav = sav;
    }

    public void addUtegysegView(UtegysegView uv) {
        utegysegViewk.add(uv);
    }

    public List<UtegysegView> getUtegysegViewk() {
        return utegysegViewk;
    }

    public void kirajzol(Graphics2D g) {
        for (UtegysegView uv : utegysegViewk) {
            uv.kirajzol(g);
        }
    }

    public Sav getSav() { return sav; }
}
