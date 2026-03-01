package tn.edu.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.edu.esprit.entities.Commentaire;
import tn.edu.esprit.services.ServiceCommentaire;

import java.sql.Date;

public class CommentaireController {

    @FXML private TableView<Commentaire> tableCommentaire;
    @FXML private TableColumn<Commentaire, Integer> colId;
    @FXML private TableColumn<Commentaire, Date> colDate;
    @FXML private TableColumn<Commentaire, String> colType;
    @FXML private TableColumn<Commentaire, String> colTexte;

    @FXML private TextField carnetIdTF;
    @FXML private DatePicker dateCommentaireDP;
    @FXML private ComboBox<String> typeCommentaireCB;
    @FXML private TextArea texteCommentaireTA;

    private final ServiceCommentaire service = new ServiceCommentaire();
    private ObservableList<Commentaire> data;
    private Commentaire commentaireSelectionne;

    @FXML
    public void initialize() {
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCommentaire"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeCommentaire"));
        colTexte.setCellValueFactory(new PropertyValueFactory<>("texteCommentaire"));

        typeCommentaireCB.getItems().addAll(
                "Observation", "Problème", "Suggestion", "Amélioration"
        );

        tableCommentaire.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, selected) -> {
                    if (selected != null) {
                        commentaireSelectionne = selected;
                        carnetIdTF.setText(String.valueOf(selected.getCarnetId()));
                        dateCommentaireDP.setValue(
                                selected.getDateCommentaire().toLocalDate()
                        );
                        typeCommentaireCB.setValue(selected.getTypeCommentaire());
                        texteCommentaireTA.setText(selected.getTexteCommentaire());
                    }
                }
        );
    }

    private void chargerCommentaires() {
        data = FXCollections.observableArrayList(service.getAll());
        tableCommentaire.setItems(data);
    }

    @FXML
    private void ouvrirListe() {

        chargerCommentaires();

        tableCommentaire.setVisible(true);
        tableCommentaire.setManaged(true);
    }

    @FXML
    private void ajouterCommentaire() {
        try {

            Commentaire c = new Commentaire();
            c.setCarnetId(Integer.parseInt(carnetIdTF.getText()));
            c.setDateCommentaire(Date.valueOf(dateCommentaireDP.getValue()));
            c.setTypeCommentaire(typeCommentaireCB.getValue());
            c.setTexteCommentaire(texteCommentaireTA.getText());

            service.ajouter(c);
            chargerCommentaires();
            clearForm();

            alert("Ajout réussi");

        } catch (Exception e) {
            alert("Erreur ajout");
        }
    }

    @FXML
    private void modifierCommentaire() {

        if (commentaireSelectionne == null) {
            alert("Sélectionnez un commentaire !");
            return;
        }

        commentaireSelectionne.setTexteCommentaire(
                texteCommentaireTA.getText()
        );
        commentaireSelectionne.setTypeCommentaire(
                typeCommentaireCB.getValue()
        );
        commentaireSelectionne.setDateCommentaire(
                Date.valueOf(dateCommentaireDP.getValue())
        );

        service.modifier(commentaireSelectionne);
        chargerCommentaires();
        clearForm();

        alert("Modification réussie");
    }

    @FXML
    private void supprimerCommentaire() {

        Commentaire selected =
                tableCommentaire.getSelectionModel().getSelectedItem();

        if (selected == null) {
            alert("Sélectionnez un commentaire !");
            return;
        }

        service.supprimer(selected.getId());
        chargerCommentaires();
        clearForm();

        alert("Suppression réussie");
    }

    private void clearForm() {
        carnetIdTF.clear();
        dateCommentaireDP.setValue(null);
        typeCommentaireCB.setValue(null);
        texteCommentaireTA.clear();
        tableCommentaire.getSelectionModel().clearSelection();
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
    public void setCarnetId(int id) {

        carnetIdTF.setText(String.valueOf(id));

        // 🔒 optionnel : rendre le champ non modifiable
        carnetIdTF.setEditable(false);
        data = FXCollections.observableArrayList(service.getByCarnetId(id));
        tableCommentaire.setItems(data);
    }
}