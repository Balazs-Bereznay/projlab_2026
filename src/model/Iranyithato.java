package model;

import java.util.List;

/**
 * Azokat az objektumokat jelolo interfesz, amelyeket a jatekos iranyit.
 */
public interface Iranyithato extends ProtoEntitas {

    /**
     * Beallitja az objektum altal kovetendo utegysegeket.
     *
     * @param utegysegLista a kivalasztott utvonal utegysegei
     */
    void setKijeloltUtegysegek(List<Utegyseg> utegysegLista);
}
