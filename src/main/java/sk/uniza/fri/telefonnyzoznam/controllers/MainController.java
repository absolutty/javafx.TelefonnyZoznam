package sk.uniza.fri.telefonnyzoznam.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sk.uniza.fri.telefonnyzoznam.Main;
import sk.uniza.fri.telefonnyzoznam.datamodel.ContactData;
import sk.uniza.fri.telefonnyzoznam.datamodel.Kontakt;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Trieda MainController je vytvorená pomocou FXML a slúži na inicializovanie UI elementov.
 * Ďalej umožňuje komunikáciu (príjmanie a posielanie správ) od jednolivých prvkov.
 * Slúži na:
 * - načítanie príslušnej databázy kontaktov
 * - vytvorenie, upravenie alebo vymazanie kontaktu z databázy
 *
 * @author Adam Hajro
 */
public class MainController {
    @FXML private BorderPane mainBorderPane;
    @FXML private TableView<Kontakt> contactsTable;

    private ContactData data;

    public void initialize() {
        data = new ContactData();
        data.loadContacts();
        contactsTable.setItems(data.getContacts());
    }

    @FXML public void pridajKontakt() {
        this.showContactDialog(txt_PRIDAJ);
    }

    @FXML public void upravKontakt() {
        this.showContactDialog(txt_UPRAV);
    }

    @FXML public void vymazKontakt() {
        Kontakt zvolenyKontakt = this.zvolenyItem();
        if (zvolenyKontakt == null) return; //overi ci je nejaky kontakt zakliknuty, aby ho bolo mozne vymazat

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vymaž existujúci kontakt");
        alert.setHeaderText(String.format("Praješ si vymazať kontakt %s %s?",
                zvolenyKontakt.getKrstneMeno(), zvolenyKontakt.getPriezvisko()));
        alert.setContentText("Po tejto akcií nebude možné k nemu pristupovať natrvalo!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            data.deleteContact(zvolenyKontakt);
            data.saveContacts();
        }
    }

    private static final URL fxml_CONTACT_DIALOG = Main.class.getResource("layouts/res_contact-dialog.fxml");

    private static final String txt_PRIDAJ = "Pridaj nový kontakt";
    private static final String txt_UPRAV = "Uprav existujúci kontakt";
    /**
     * zobrazí ContactDialog, kt. umožňuje pridávať alebo upravovať kontakty.
     * To že či chcem PRIDAŤ alebo UPRAVIŤ kontakt, je rozhodnuté na základe vstupného parametra.
     * @param vypisovanyText môže byť:
     *                       - txt_PRIDAJ --> pridávanie Kontakt-u
     *                       - txt_UPRAV --> upravovanie Kontakt-u
     */
    private void showContactDialog(String vypisovanyText) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxml_CONTACT_DIALOG);

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Nie je mozne nacitat dialog");
            e.printStackTrace();
            return;
        }
        dialog.setTitle(vypisovanyText);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        KontaktController kontaktController = fxmlLoader.getController();
        switch (vypisovanyText) {
            case txt_PRIDAJ -> {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        Kontakt novyKontakt = kontaktController.retKontakt();
                        data.addContact(novyKontakt);
                    } catch (IOException ex) {
                        this.chybovyVypis(vypisovanyText);
                        return;
                    }
                }
            }
            case txt_UPRAV -> {
                Kontakt zvolenyKontakt = this.zvolenyItem();
                if (zvolenyKontakt == null) return; //overi ci je nejaky kontakt zakliknuty, aby ho bolo mozne editovat
                kontaktController.nastavKontakt(zvolenyKontakt);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        kontaktController.editKontakt(zvolenyKontakt);
                    } catch (IOException ex) {
                        this.chybovyVypis(vypisovanyText);
                        return;
                    }
                }
                contactsTable.refresh();
            }
            default -> throw new IllegalArgumentException("Zadany nespravny text na urcenie vyvorenia ContactDialog-u");
        }
        data.saveContacts();
    }

    /**
     * vráti aktuálne zvolený Kontakt v this.contactsTable
     */
    private Kontakt zvolenyItem() {
        return this.contactsTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Vytvorí ERROR Alert kt. sa zobrazuje pri nesprávnom zadaní údajov.
     * @param vypisovanyText na základe neho sa nastavuje obsah vypisovaného textu
     */
    private void chybovyVypis(String vypisovanyText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (vypisovanyText) {
            case txt_PRIDAJ -> {
                alert.setTitle("Chyba pri pridávaní kontaktu!");
            }
            case txt_UPRAV -> {
                alert.setTitle("Chyba pri upravovaní kontaktu!");
            }
        }
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(Main.img_LOGO_APLIKACIE);
        alert.initOwner(mainBorderPane.getScene().getWindow());

        alert.setHeaderText("Je potrebné zadať všetky POVINNÉ údaje!");
        alert.showAndWait();

        this.showContactDialog(vypisovanyText);
    }

    @FXML public MenuItem btn_uprav;
    @FXML public MenuItem btn_vymaz;
    /**
     * metóda je invokovaná pri pri kliknutí na Menu Kontakt.
     * Urci ci je nejaky z kontaktov zvoleny:
     * - ak ÁNO: povolí MenuItem-y btn_uprav,btn_vymaz
     * - ak NIE: zakáže MenuItem-y btn_uprav,btn_vymaz
     */
    @FXML public void jeZakliknutyKontakt() {
        if (zvolenyItem() != null) { //nejaky z kontaktov JE zakliknuty
            this.btn_uprav.setDisable(false);
            this.btn_vymaz.setDisable(false);
        } else { //ziadny z kontaktov NIE JE zakliknuty
            this.btn_uprav.setDisable(true);
            this.btn_vymaz.setDisable(true);
        }
    }

    private static final URL fxml_INFO_DIALOG = Main.class.getResource("layouts/res_info-dialog.fxml");
    @FXML public void otvorInfo() {
        System.out.println("hahaahahaha");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxml_INFO_DIALOG);

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Nie je mozne nacitat dialog");
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Info o aplikácií");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.showAndWait();
    }
}
