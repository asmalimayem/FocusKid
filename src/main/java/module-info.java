module com.projet.focuskid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    opens com.projet.focuskid.controllers to javafx.fxml;
    opens com.projet.focuskid.entities to javafx.base;
    opens com.projet.focuskid to javafx.fxml;
    exports com.projet.focuskid;
}