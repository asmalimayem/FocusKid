module com.projet.fouckid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.projet.fouckid.controllers to javafx.fxml;
    opens com.projet.fouckid.entities to javafx.base;
    opens com.projet.fouckid to javafx.fxml;

    exports com.projet.fouckid;
    exports com.projet.fouckid.controllers;
    exports com.projet.fouckid.entities;
}