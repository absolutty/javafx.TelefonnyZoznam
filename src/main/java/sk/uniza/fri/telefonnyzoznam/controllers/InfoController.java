package sk.uniza.fri.telefonnyzoznam.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import sk.uniza.fri.telefonnyzoznam.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InfoController {
    @FXML public Label nazovAplikacie;
    @FXML public TextArea vypisovaneInfo;

    public void initialize() {
        this.nazovAplikacie.setText(Main.NAZOV_APPLIKACIE);
        this.nazovAplikacie.setFont(new Font("Arial", 25));
        this.vypisovaneInfo.setFont(new Font("Arial", 12));

        this.vypisovaneInfo.setText(nacitajText());
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String zaciatok_ODDELENIE = "&-";
    private static final String koniec_ODDELENIE = "-&";
    /**
     * Táto metóda slúži načítanie README.md a vrátenie len toho textu, kt. sa nachádza medzi
     * znakmi zaciatok_ODDELENIE "&-" a koniec_ODDELENIE "-&"
     * Napríklad:
     *
     * nejaky text bla bla bla <br>
     *
     * &-
     * Tento text bude zobrazený v informáciach o aplikácií
     * -&
     *
     * @return String vytvoreného textu, môže sa priamo zobraziť v vypisovaneInfo
     */
    private static String nacitajText() {
        boolean citajText = false;
        File file = new File("README.md");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder contents = new StringBuilder();
            String text;

            while ((text = reader.readLine()) != null) {
                if (text.equals(zaciatok_ODDELENIE)) { //najdeny ZACIATOK citania textu
                    citajText = true;
                } else if (text.equals(koniec_ODDELENIE)) { //najdeny KONIEC citania textu
                    break;
                } else {
                    if (citajText) {
                        contents.append(text).append(LINE_SEPARATOR);
                    }
                }
            } return contents.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
