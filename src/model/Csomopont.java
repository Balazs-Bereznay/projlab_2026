package model;

import java.util.ArrayList;

/**
 * Az úthálózat szakaszait (Ut) összekötő csatlakozási pontok. Kezeli a járművek be és
 * kilépését a csomópontba és a hozzá csatlakozó utakra.
 */
public class Csomopont {
    private ArrayList<Ut> utLista;
    private boolean celpont;
    private boolean buszmegallo;
    private String azonosito;

    private static int azonositoSzamlalo = 0;   ///új

    ///Konstruktorok
    public Csomopont(ArrayList<Ut> utLista, boolean celpont, boolean buszmegallo){
        this.utLista = (utLista != null) ? new ArrayList<>(utLista) : new ArrayList<>();
        this.celpont = celpont;
        this.buszmegallo = buszmegallo;

        azonositoSzamlalo++;
        this.azonosito = String.valueOf(azonositoSzamlalo);
    }

    public Csomopont(){
        this(new ArrayList<>(), false, false);
    }

    ///Getterek és setterek
    public boolean getCelpont(){ return celpont; }
    public void setCelpont(boolean celpont){ this.celpont = celpont; }

    public boolean getBuszmegallo(){ return buszmegallo; }
    public void setBuszmegallo(boolean buszmegallo){ this.buszmegallo = buszmegallo; }

    public String getAzonosito(){ return azonosito; }
    public void setAzonosito(String azonosito){ this.azonosito = azonosito; }

    public ArrayList<Ut> getUtLista(){ return savok; }
    public void setSavok(ArrayList<Ut> utLista){ this.utLista = utLista};

    ///További metódusok
    /**
     * Feljegyzi egy jármű belépését a csomópontba az előző  útszakaszról.
     * @param jarmu Az a jármű, ami megérkezett a csomópontba.
     */
    public void jarmuErkezik (Jarmu jarmu){
        System.out.println("Jármű érkezett a csomópontba.");
        if(jarmu.getClass().getSimpleName().equals("Auto") && celpont){
            System.out.println("Ez a célpontja az autónak.");

        }else if(jarmu.getClass().getSimpleName().equals("Busz") && buszmegallo)
            System.out.println("Ez egy megállója a busznak.");
    }

    /**
     * Kezeli egy jármű kilépését a csomópontból és áthelyezi a következő útszakaszra.
     * @param jarmu Az a jármű, ami tovább megy, átlépi a csomópontot.
     */
    public void jarmuTavozik(Jarmu jarmu){
        System.out.println("Jármű áttéve a következő útegységre.");
    }
}
