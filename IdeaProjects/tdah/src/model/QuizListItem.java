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

public class QuizListItem {

    public static class Cell extends ListCell<Quiz> {
        @Override
        protected void updateItem(Quiz quiz, boolean empty) {
            super.updateItem(quiz, empty);

            if (empty || quiz == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox hbox = new HBox(10);
                hbox.setPadding(new Insets(5));

                Circle circle = new Circle(5);
                switch (quiz.getDifficulte()) {
                    case "facile": circle.setFill(Color.GREEN); break;
                    case "moyen": circle.setFill(Color.ORANGE); break;
                    case "difficile": circle.setFill(Color.RED); break;
                    default: circle.setFill(Color.GRAY);
                }

                VBox vbox = new VBox(3);
                Label titreLabel = new Label(quiz.getTitre());
                titreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label detailsLabel = new Label(quiz.getPointsTotal() + " pts · " + quiz.getDureeTotale() + "s");
                detailsLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");

                vbox.getChildren().addAll(titreLabel, detailsLabel);
                hbox.getChildren().addAll(circle, vbox);
                setGraphic(hbox);
            }
        }
    }

    public static Callback<ListView<Quiz>, ListCell<Quiz>> getCellFactory() {
        return list -> new Cell();
    }
}