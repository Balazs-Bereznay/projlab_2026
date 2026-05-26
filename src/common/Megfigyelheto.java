package common;

public interface Megfigyelheto {
    void addObserver(Megfigyelo m);
    void removeObserver(Megfigyelo m);
    void ertesit();
}
