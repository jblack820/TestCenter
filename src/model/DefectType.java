package model;

import java.io.Serializable;

/**
 *
 * @author Greg
 */
public enum DefectType implements Serializable {

    BLOKKOLO("Blokkoló"), SULYOS("Súlyos"), KOZEPES("Közepes"), ENYHE("Enyhe"), NEM_ERTEKELT("Nem értékelt");

    private String fullName;

    private DefectType(String fullName) {
        this.fullName = fullName;
    }

    public static DefectType getBLOKKOLO() {
        return BLOKKOLO;
    }

    public static DefectType getNEM_ERTEKELT() {
        return NEM_ERTEKELT;
    }

    public static DefectType getSULYOS() {
        return SULYOS;
    }

    public static DefectType getKOZEPES() {
        return KOZEPES;
    }

    public static DefectType getENYHE() {
        return ENYHE;
    }

    public String getFullName() {
        return fullName;
    }

}
