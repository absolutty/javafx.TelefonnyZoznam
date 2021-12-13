module sk.uniza.fri.telefonnyzoznam {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens sk.uniza.fri.telefonnyzoznam to javafx.fxml;
    opens sk.uniza.fri.telefonnyzoznam.datamodel to javafx.base;

    exports sk.uniza.fri.telefonnyzoznam;
    exports sk.uniza.fri.telefonnyzoznam.controllers;
    opens sk.uniza.fri.telefonnyzoznam.controllers to javafx.fxml;
}