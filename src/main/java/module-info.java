module com.projet.fouckid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;
    requires java.sql;

    // Ouvrir les packages pour JavaFX
    opens com.projet.fouckid.controllers to javafx.fxml;

    // Exporter les packages utilisés
    exports com.projet.fouckid;
    exports com.projet.fouckid.controllers;
    exports com.projet.fouckid.entities;
    exports com.projet.fouckid.services;
    exports com.projet.fouckid.tools;
}