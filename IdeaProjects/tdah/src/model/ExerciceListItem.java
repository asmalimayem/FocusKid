package model;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

public class ExerciceListItem {

    public static class Cell extends ListCell<Exercice> {
        @Override
        protected void updateItem(Exercice exercice, boolean empty) {
            super.updateItem(exercice, empty);

            if (empty || exercice == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox hbox = new HBox(10);
                hbox.setPadding(new Insets(5));

                Circle circle = new Circle(5);
                switch (exercice.getDifficulte()) {
                    case "facile": circle.setFill(Color.GREEN); break;
                    case "moyen": circle.setFill(Color.ORANGE); break;
                    case "difficile": circle.setFill(Color.RED); break;
                    default: circle.setFill(Color.GRAY);
                }

                VBox vbox = new VBox(3);
                Label titreLabel = new Label(exercice.getTitre());
                titreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label detailsLabel = new Label(exercice.getPointsTotal() + " pts · " + exercice.getDuree() + "s");
                detailsLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");

                vbox.getChildren().addAll(titreLabel, detailsLabel);
                hbox.getChildren().addAll(circle, vbox);
                setGraphic(hbox);
            }
        }
    }

    public static Callback<ListView<Exercice>, ListCell<Exercice>> getCellFactory() {
        return list -> new Cell();
    }
}