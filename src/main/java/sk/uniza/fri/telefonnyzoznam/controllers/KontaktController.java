package sk.uniza.fri.telefonnyzoznam.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sk.uniza.fri.telefonnyzoznam.datamodel.Kontakt;

import java.io.IOException;

/**
 * Trieda KontaktController je vytvorená pomocou FXML a slúži na inicializovanie UI elementov.
 * Ďalej umožňuje spracovavanie buď vytváraných alebo upravovaných kontaktov.
 *
 * @author Adam Hajro
 */
public class KontaktController {
    @FXML private TextField meno;
    @FXML private TextField priezvisko;
    @FXML private TextField telCislo;
    @FXML private TextField poznamka;

    /**
     * Dialog je prvotne zobrazovaný prázdny.
     *
     * @return vytvoreny objekt Kontakt na zaklade jednotlivych TextField-ov
     * (meno, priezvisko, telCislo, poznamka)
     */
    public Kontakt retKontakt() throws IOException {
        //overenie zadania POVINNYCH udajov (bez tychto udajov nie je mozne spravne vytvorenie Kontaktu)
        this.overenieKontaktu();

        return new Kontakt(
                meno.getText(),
                priezvisko.getText(),
                telCislo.getText(),
                poznamka.getText());
    }

    /**
     * Dialog je prvotne zobrazovany z atribútmi zadávaného Kontakt-u
     * @param upravovanyKontakt na základe jeho atribútov sú nastavené hodnoty jednotlivych TextField-ov
     */
    public void nastavKontakt(Kontakt upravovanyKontakt) {
        this.meno.setText(upravovanyKontakt.getKrstneMeno());
        this.priezvisko.setText(upravovanyKontakt.getPriezvisko());
        this.telCislo.setText(upravovanyKontakt.getTelCislo());
        this.poznamka.setText(upravovanyKontakt.getPoznamka());
    }

    /**
     * @param upravovanyKontakt: jeho vsetky atribúty sú upravované na základe hodnôt v TextField-och
     */
    public void editKontakt(Kontakt upravovanyKontakt) throws IOException {
        //overenie zadania POVINNYCH udajov (bez tychto udajov nie je mozne spravne upravenie Kontaktu)
        this.overenieKontaktu();

        upravovanyKontakt.setKrstneMeno(this.meno.getText());
        upravovanyKontakt.setPriezvisko(this.priezvisko.getText());
        upravovanyKontakt.setTelCislo(this.telCislo.getText());
        upravovanyKontakt.setPoznamka(this.poznamka.getText());
    }

    /**
     * Je potrebné:
     * - ak sú polia nepovinných údajov prázdne, nahradené sú medzerou " "
     * - zadané atribúty Kontakt-u je potrebné overiť pri každej externej manipulácií s ním (pridávanie, úprava, ...)
     */
    private void overenieKontaktu() throws IOException {
        //overenie NEPOVINNÝCH údajov (meno, telCislo)
        if (this.priezvisko.getText().equals("")) {
            this.priezvisko.setText(" ");
        } else if (this.poznamka.getText().equals("")) {
            this.poznamka.setText(" ");
        }

        //overenie POVINNÝCH údajov (meno, telCislo)
        if (this.meno.getText().isBlank()) {
            throw new IOException("Nezadanie povinneho pola [this.meno]");
        } else if (this.telCislo.getText().isBlank()) {
            throw new IOException("Nezadanie povinneho pola [this.telCislo]");
        }
    }
}
