package tn.edu.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.edu.esprit.entities.Commentaire;
import tn.edu.esprit.services.ServiceCommentaire;
import javafx.collections.transformation.FilteredList;
import java.sql.Date;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CommentaireController {

    /* =========================================================
     *  COLONNES DE LA TABLE COMMENTAIRE
     * ========================================================= */
    @FXML private TableView<Commentaire>            tableCommentaire;
    @FXML private TableColumn<Commentaire, Integer> colId;
    @FXML private TableColumn<Commentaire, Date>    colDate;
    @FXML private TableColumn<Commentaire, String>  colType;
    @FXML private TableColumn<Commentaire, String>  colTexte;

    /* =========================================================
     *  CHAMPS DU FORMULAIRE
     * ========================================================= */
    @FXML private TextField        carnetIdTF;
    @FXML private DatePicker       dateCommentaireDP;
    @FXML private ComboBox<String> typeCommentaireCB;
    @FXML private TextArea         texteCommentaireTA;

    /* =========================================================
     *  FILTRES
     * ========================================================= */
    @FXML private DatePicker       filtreDateDP;
    @FXML private ComboBox<String> filtreTypeCB;
    @FXML private Label            filtreCountLabel;

    /* =========================================================
     *  BANDEAU MODE FORMULAIRE
     * ========================================================= */
    @FXML private Label formModeLabel;

    /* =========================================================
     *  DATA
     * ========================================================= */
    private final ServiceCommentaire            service      = new ServiceCommentaire();
    private ObservableList<Commentaire>         data;
    private Commentaire                         commentaireSelectionne;
    private FilteredList<Commentaire>           filteredData;
    private boolean filtreListenersInitialises  = false;

    /* =========================================================
     *  INITIALIZE
     * ========================================================= */
    @FXML
    public void initialize() {

        /* -- Colonnes -- */
    	colId.setVisible(false);
        colId   .setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate .setCellValueFactory(new PropertyValueFactory<>("dateCommentaire"));
        colType .setCellValueFactory(new PropertyValueFactory<>("typeCommentaire"));
        colTexte.setCellValueFactory(new PropertyValueFactory<>("texteCommentaire"));

        /* -- ComboBox formulaire -- */
        typeCommentaireCB.getItems().addAll("Observation", "Problème", "Suggestion", "Amélioration");

        /* -- ComboBox filtre -- */
        filtreTypeCB.getItems().addAll("Observation", "Problème", "Suggestion", "Amélioration");

        /* -- Listeners filtres -- */
        filtreDateDP.valueProperty().addListener((obs, o, n) -> { filtrer(); majBadge(); });
        filtreTypeCB.valueProperty().addListener((obs, o, n) -> { filtrer(); majBadge(); });
        filtreListenersInitialises = true;

        /* =========================================================
         *  Sélection ligne → remplir formulaire + bandeau
         * ========================================================= */
        tableCommentaire.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, selected) -> {
                if (selected != null) {
                    commentaireSelectionne = selected;

                    // ✅ Bandeau passe en mode "Modification"
                    if (formModeLabel != null)
                        formModeLabel.setText("📝  Modification du commentaire #" + selected.getId());

                    carnetIdTF        .setText(String.valueOf(selected.getCarnetId()));
                    dateCommentaireDP .setValue(selected.getDateCommentaire().toLocalDate());
                    typeCommentaireCB .setValue(selected.getTypeCommentaire());
                    texteCommentaireTA.setText(selected.getTexteCommentaire());
                }
            }
        );

        chargerCommentaires();
    }

    /* =========================================================
     *  NOUVEAU / VIDER FORMULAIRE
     * ========================================================= */
    @FXML
    public void nouveauCommentaire() {
        clearForm();
        if (formModeLabel != null)
            formModeLabel.setText("✏️  Nouveau commentaire");
    }

    /* =========================================================
     *  CHARGER DONNÉES
     * ========================================================= */
    private void chargerCommentaires() {
        data         = FXCollections.observableArrayList(service.getAll());
        filteredData = new FilteredList<>(data, b -> true);
        tableCommentaire.setItems(filteredData);
        filtrer();
        majBadge();
    }

    /* =========================================================
     *  AJOUTER
     * ========================================================= */
    @FXML
    private void ajouterCommentaire() {
        try {
            if (carnetIdTF.getText() == null || carnetIdTF.getText().trim().isEmpty()
                    || dateCommentaireDP.getValue() == null
                    || typeCommentaireCB.getValue() == null
                    || texteCommentaireTA.getText() == null
                    || texteCommentaireTA.getText().trim().isEmpty()) {
                alert("❌ Tous les champs doivent être remplis !");
                return;
            }

            Commentaire c = new Commentaire();
            c.setCarnetId         (Integer.parseInt(carnetIdTF.getText().trim()));
            c.setDateCommentaire  (Date.valueOf(dateCommentaireDP.getValue()));
            c.setTypeCommentaire  (typeCommentaireCB.getValue());
            c.setTexteCommentaire (texteCommentaireTA.getText().trim());

            service.ajouter(c);
            chargerCommentaires();
            clearForm();
            alert("✔ Commentaire ajouté avec succès !");

        } catch (NumberFormatException e) {
            alert("❌ L'ID du carnet doit être un nombre !");
        } catch (Exception e) {
            alert("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    /* =========================================================
     *  MODIFIER
     * ========================================================= */
    @FXML
    private void modifierCommentaire() {
        if (commentaireSelectionne == null) {
            alert("⚠ Sélectionnez d'abord un commentaire !");
            return;
        }
        if (dateCommentaireDP.getValue() == null
                || typeCommentaireCB.getValue() == null
                || texteCommentaireTA.getText().trim().isEmpty()) {
            alert("❌ Tous les champs doivent être remplis !");
            return;
        }
        commentaireSelectionne.setDateCommentaire  (Date.valueOf(dateCommentaireDP.getValue()));
        commentaireSelectionne.setTypeCommentaire  (typeCommentaireCB.getValue());
        commentaireSelectionne.setTexteCommentaire (texteCommentaireTA.getText().trim());

        service.modifier(commentaireSelectionne);
        chargerCommentaires();
        clearForm();
        alert("✔ Commentaire modifié avec succès !");
    }

    /* =========================================================
     *  SUPPRIMER
     * ========================================================= */
    @FXML
    private void supprimerCommentaire() {
        Commentaire sel = tableCommentaire.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alert("⚠ Sélectionnez d'abord un commentaire !");
            return;
        }
        service.supprimer(sel.getId());
        chargerCommentaires();
        clearForm();
        alert("✔ Commentaire supprimé avec succès !");
    }

    /* =========================================================
     *  RAFRAÎCHIR  ← NOUVEAU
     * ========================================================= */
    @FXML
    private void rafraichirCommentaires() {
        chargerCommentaires();
        clearForm();
        alert("✔ Liste rafraîchie !");
    }

    /* =========================================================
     *  LISTER
     * ========================================================= */
    @FXML
    private void ouvrirListe() {
        chargerCommentaires();
        tableCommentaire.setVisible(true);
        tableCommentaire.setManaged(true);
    }

    /* =========================================================
     *  FILTRES
     * ========================================================= */
    private void filtrer() {
        if (filteredData == null) return;
        filteredData.setPredicate(commentaire -> {
            if (filtreDateDP.getValue() != null
                    && !commentaire.getDateCommentaire().toLocalDate().equals(filtreDateDP.getValue()))
                return false;
            if (filtreTypeCB.getValue() != null
                    && !commentaire.getTypeCommentaire().equals(filtreTypeCB.getValue()))
                return false;
            return true;
        });
    }

    private void majBadge() {
        if (filtreCountLabel == null || filteredData == null) return;
        int n = filteredData.size();
        boolean aucunFiltre = filtreDateDP.getValue() == null && filtreTypeCB.getValue() == null;
        filtreCountLabel.setText(aucunFiltre
                ? "● Tous  (" + n + ")"
                : "● " + n + " résultat" + (n > 1 ? "s" : ""));
    }

    @FXML
    public void resetFiltre() {
        filtreDateDP.setValue(null);
        filtreTypeCB.setValue(null);
    }

    /* =========================================================
     *  SETTER CARNET ID (appelé depuis CarnetController)
     * ========================================================= */
    public void setCarnetId(int id) {
        carnetIdTF.setText(String.valueOf(id));
        carnetIdTF.setEditable(false);

        data         = FXCollections.observableArrayList(service.getByCarnetId(id));
        filteredData = new FilteredList<>(data, b -> true);
        tableCommentaire.setItems(filteredData);
        filtrer();
        majBadge();
    }

    /* =========================================================
     *  HELPERS
     * ========================================================= */
    private void clearForm() {
        if (!carnetIdTF.isEditable()) {
            // on garde l'ID carnet verrouillé si on est en mode "par carnet"
        } else {
            carnetIdTF.clear();
        }
        dateCommentaireDP .setValue(null);
        typeCommentaireCB .setValue(null);
        texteCommentaireTA.clear();
        tableCommentaire.getSelectionModel().clearSelection();
        commentaireSelectionne = null;
        if (formModeLabel != null)
            formModeLabel.setText("✏️  Nouveau commentaire");
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
    @FXML
    private void retourCarnet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjouterCarnet.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) carnetIdTF.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}