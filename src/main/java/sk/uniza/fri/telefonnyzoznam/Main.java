package sk.uniza.fri.telefonnyzoznam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static final String NAZOV_APPLIKACIE = "Telefónny zoznam";

    private static final int SIRKA = 600;
    private static final int VYSKA = 400;

    private static final int min_SIRKA = 500;
    private static final int min_VYSKA = 250;

    public static final Image img_LOGO_APLIKACIE = new Image("https://img.icons8.com/material-outlined/24/000000/phone.png");
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                Main.class.getResource("layouts/res_main.fxml")));

        primaryStage.getIcons().add(img_LOGO_APLIKACIE);
        primaryStage.setTitle(NAZOV_APPLIKACIE);
        primaryStage.setScene(new Scene(root, SIRKA, VYSKA));
        primaryStage.setMinWidth(min_SIRKA);
        primaryStage.setMinHeight(min_VYSKA);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
