package sk.uniza.fri.telefonnyzoznam.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import sk.uniza.fri.telefonnyzoznam.Main;

public class InfoController {

    @FXML public Label nazovAplikacie;

    public void initialize() {
        this.nazovAplikacie.setText(Main.NAZOV_APPLIKACIE);
        this.nazovAplikacie.setFont(new Font("Arial", 25));
    }
}
