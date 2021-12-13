package sk.uniza.fri.telefonnyzoznam.datamodel;

import javafx.beans.property.SimpleStringProperty;

/**
 * trieda Kontakt slúži ako predloha (datamodel) pre vytvorenie jednotlivého kontaktu,
 * kt. bude možné ukladať v databáze a zobrazovať v aplikácií.
 * Pozostáva z týchto atribútov: krstneMeno, priezvisko, telCislo, poznamka
 *
 * @author Adam Hajro
 */
public class Kontakt {
    private final SimpleStringProperty krstneMeno = new SimpleStringProperty("");
    private final SimpleStringProperty priezvisko = new SimpleStringProperty("");
    private final SimpleStringProperty telCislo = new SimpleStringProperty("");
    private final SimpleStringProperty poznamka = new SimpleStringProperty("");

    public Kontakt() {}

    public Kontakt(String krstneMeno, String priezvisko, String telCislo, String poznamka) {
        this.krstneMeno.set(krstneMeno);
        this.priezvisko.set(priezvisko);
        this.telCislo.set(telCislo);
        this.poznamka.set(poznamka);
    }

    public String getKrstneMeno() {
        return krstneMeno.get();
    }
    public void setKrstneMeno(String krstneMeno) {
        this.krstneMeno.set(krstneMeno);
    }

    public String getPriezvisko() {
        return priezvisko.get();
    }
    public void setPriezvisko(String priezvisko) {
        this.priezvisko.set(priezvisko);
    }

    public String getTelCislo() {
        return telCislo.get();
    }
    public void setTelCislo(String telCislo) {
        this.telCislo.set(telCislo);
    }

    public String getPoznamka() {
        return poznamka.get();
    }
    public void setPoznamka(String poznamka) {
        this.poznamka.set(poznamka);
    }

    @Override
    public String toString() {
        return "Kontakt{" +
                "krstneMeno=" + krstneMeno +
                ", priezvisko=" + priezvisko +
                ", telCislo=" + telCislo +
                ", poznamka=" + poznamka +
                '}';
    }
}
