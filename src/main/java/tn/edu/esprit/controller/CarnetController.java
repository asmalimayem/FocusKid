package tn.edu.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.edu.esprit.entities.CarnetEducatif;
import tn.edu.esprit.services.ServiceCarnetEducatif;

import java.sql.Date;
import java.sql.Time;

public class CarnetController {

    /* =========================================================
     *  COLONNES DE LA TABLE
     * ========================================================= */
    @FXML private TableView<CarnetEducatif>            tableCarnet;
    @FXML private TableColumn<CarnetEducatif, Integer> colId;
    @FXML private TableColumn<CarnetEducatif, Date>    colDate;
    @FXML private TableColumn<CarnetEducatif, Time>    colHeureDebut;
    @FXML private TableColumn<CarnetEducatif, Time>    colHeureFin;
    @FXML private TableColumn<CarnetEducatif, Integer> colDuree;
    @FXML private TableColumn<CarnetEducatif, String>  colLieu;
    @FXML private TableColumn<CarnetEducatif, String>  colMatiere;
    @FXML private TableColumn<CarnetEducatif, String>  colTypeActivite;
    @FXML private TableColumn<CarnetEducatif, String>  colDifficulte;
    @FXML private TableColumn<CarnetEducatif, Integer> colConcentration;
    @FXML private TableColumn<CarnetEducatif, Integer> colAgitation;
    @FXML private TableColumn<CarnetEducatif, Integer> colInterruptions;
    @FXML private TableColumn<CarnetEducatif, Integer> colTempsPerte;
    @FXML private TableColumn<CarnetEducatif, Boolean> colTravailleSeul;
    @FXML private TableColumn<CarnetEducatif, Boolean> colDemandeAide;
    @FXML private TableColumn<CarnetEducatif, Integer> colAutonomie;
    @FXML private TableColumn<CarnetEducatif, Boolean> colTravailTermine;
    @FXML private TableColumn<CarnetEducatif, String>  colDifficultes;
    @FXML private TableColumn<CarnetEducatif, String>  colPointsPositifs;

    /* =========================================================
     *  CHAMPS DU FORMULAIRE
     * ========================================================= */
    @FXML private DatePicker        dateEtudeDP;
    @FXML private TextField         heureDebutTF;
    @FXML private TextField         heureFinTF;
    @FXML private TextField         dureeTotaleTF;
    @FXML private TextField         lieuTF;
    @FXML private TextField         matiereTF;
    @FXML private ComboBox<String>  typeActiviteCB;
    @FXML private ComboBox<String>  niveauDifficulteCB;
    @FXML private ComboBox<Integer> niveauConcentrationCB;
    @FXML private ComboBox<Integer> niveauAgitationCB;
    @FXML private ComboBox<Integer> niveauAutonomieCB;
    @FXML private TextField         nombreInterruptionsTF;
    @FXML private TextField         tempsAvantPerteTF;
    @FXML private CheckBox          travailleSeulCB;
    @FXML private CheckBox          demandeAideCB;
    @FXML private CheckBox          travailTermineCB;
    @FXML private TextArea          difficultesTA;
    @FXML private TextArea          pointsPositifsTA;

    /* =========================================================
     *  DATA
     * ========================================================= */
    private final ServiceCarnetEducatif    service = new ServiceCarnetEducatif();
    private ObservableList<CarnetEducatif> data;
    private CarnetEducatif                 carnetSelectionne;

    /* =========================================================
     *  INITIALIZE
     * ========================================================= */
    @FXML
    public void initialize() {

        // ✅ Noms corrects des PropertyValueFactory (= noms des getters)
        colId            .setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate          .setCellValueFactory(new PropertyValueFactory<>("dateEtude"));
        colHeureDebut    .setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colHeureFin      .setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colDuree         .setCellValueFactory(new PropertyValueFactory<>("dureeTotale"));
        colLieu          .setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colMatiere       .setCellValueFactory(new PropertyValueFactory<>("matiere"));
        colTypeActivite  .setCellValueFactory(new PropertyValueFactory<>("typeActivite"));
        colDifficulte    .setCellValueFactory(new PropertyValueFactory<>("niveauDifficulte"));
        colConcentration .setCellValueFactory(new PropertyValueFactory<>("niveauConcentration"));
        colAgitation     .setCellValueFactory(new PropertyValueFactory<>("niveauAgitation"));
        colInterruptions .setCellValueFactory(new PropertyValueFactory<>("nombreInterruptions"));
        colTempsPerte    .setCellValueFactory(new PropertyValueFactory<>("tempsAvantPerteConcentration"));
        colTravailleSeul .setCellValueFactory(new PropertyValueFactory<>("travailleSeul"));
        colDemandeAide   .setCellValueFactory(new PropertyValueFactory<>("demandeAide"));
        colAutonomie     .setCellValueFactory(new PropertyValueFactory<>("niveauAutonomie"));
        colTravailTermine.setCellValueFactory(new PropertyValueFactory<>("travailTermine"));
        colDifficultes   .setCellValueFactory(new PropertyValueFactory<>("difficultes"));
        colPointsPositifs.setCellValueFactory(new PropertyValueFactory<>("pointsPositifs"));

        // ✅ PAS de chargerCarnets() ici → table vide au démarrage
        //    Données affichées SEULEMENT au clic sur "Consulter"

        // Remplir les ComboBox
        typeActiviteCB.getItems().addAll("Lecture", "Exercice", "Quiz", "Jeu éducatif");
        niveauDifficulteCB.getItems().addAll("Facile", "Moyen", "Difficile");
        for (int i = 1; i <= 5; i++) {
            niveauConcentrationCB.getItems().add(i);
            niveauAgitationCB.getItems().add(i);
            niveauAutonomieCB.getItems().add(i);
        }

        // Remplir le formulaire quand on clique sur une ligne du tableau
        tableCarnet.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, selected) -> {
                if (selected != null) {
                    carnetSelectionne = selected;
                    dateEtudeDP.setValue(selected.getDateEtude().toLocalDate());
                    heureDebutTF.setText(selected.getHeureDebut().toString().substring(0, 5));
                    heureFinTF.setText(selected.getHeureFin().toString().substring(0, 5));
                    dureeTotaleTF.setText(String.valueOf(selected.getDureeTotale()));
                    lieuTF.setText(selected.getLieu());
                    matiereTF.setText(selected.getMatiere());
                    typeActiviteCB.setValue(selected.getTypeActivite());
                    niveauDifficulteCB.setValue(selected.getNiveauDifficulte());
                    niveauConcentrationCB.setValue(selected.getNiveauConcentration());
                    niveauAgitationCB.setValue(selected.getNiveauAgitation());
                    niveauAutonomieCB.setValue(selected.getNiveauAutonomie());
                    nombreInterruptionsTF.setText(String.valueOf(selected.getNombreInterruptions()));
                    tempsAvantPerteTF.setText(String.valueOf(selected.getTempsAvantPerteConcentration()));
                    travailleSeulCB.setSelected(selected.isTravailleSeul());
                    demandeAideCB.setSelected(selected.isDemandeAide());
                    travailTermineCB.setSelected(selected.isTravailTermine());
                    difficultesTA.setText(selected.getDifficultes());
                    pointsPositifsTA.setText(selected.getPointsPositifs());
                }
            }
        );
    }

    /* =========================================================
     *  CONSULTER
     * ========================================================= */
    @FXML
    private void consulterCarnets() {
        chargerCarnets();
    }

    private void chargerCarnets() {
        data = FXCollections.observableArrayList(service.getAll());
        tableCarnet.setItems(data);
        tableCarnet.refresh();
    }

    /* =========================================================
     *  AJOUTER
     * ========================================================= */
    @FXML
    private void ajouterCarnet() {
        try {
            CarnetEducatif c = new CarnetEducatif();
            remplirDepuisFormulaire(c);
            service.ajouter(c);
            chargerCarnets();
            clearForm();
            alert("✔ Carnet ajouté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            alert("❌ Erreur : vérifiez les champs !\n" + e.getMessage());
        }
    }

    /* =========================================================
     *  MODIFIER
     * ========================================================= */
    @FXML
    private void modifierCarnet() {
        if (carnetSelectionne == null) {
            alert("⚠ Sélectionnez d'abord une ligne dans le tableau !");
            return;
        }
        try {
            remplirDepuisFormulaire(carnetSelectionne);
            service.modifier(carnetSelectionne);
            chargerCarnets();
            clearForm();
            alert("✔ Carnet modifié avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            alert("❌ Erreur : vérifiez les champs !\n" + e.getMessage());
        }
    }

    /* =========================================================
     *  SUPPRIMER
     * ========================================================= */
    @FXML
    private void supprimerCarnet() {
        CarnetEducatif selected = tableCarnet.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("⚠ Sélectionnez d'abord une ligne dans le tableau !");
            return;
        }
        service.supprimer(selected.getId());
        chargerCarnets();
        clearForm();
        alert("✔ Carnet supprimé avec succès !");
    }

    /* =========================================================
     *  HELPER : remplir CarnetEducatif depuis le formulaire
     * ========================================================= */
    private void remplirDepuisFormulaire(CarnetEducatif c) {

        c.setDateEtude(Date.valueOf(dateEtudeDP.getValue()));
        c.setHeureDebut(Time.valueOf(heureDebutTF.getText().trim() + ":00"));
        c.setHeureFin(Time.valueOf(heureFinTF.getText().trim() + ":00"));
        c.setDureeTotale(Integer.parseInt(dureeTotaleTF.getText().trim()));
        c.setLieu(lieuTF.getText().trim());
        c.setMatiere(matiereTF.getText().trim());
        c.setTypeActivite(typeActiviteCB.getValue());
        c.setNiveauDifficulte(niveauDifficulteCB.getValue());
        c.setNiveauConcentration(niveauConcentrationCB.getValue());
        c.setNiveauAgitation(niveauAgitationCB.getValue());
        c.setNombreInterruptions(Integer.parseInt(nombreInterruptionsTF.getText().trim()));
        c.setTempsAvantPerteConcentration(Integer.parseInt(tempsAvantPerteTF.getText().trim()));
        c.setTravailleSeul(travailleSeulCB.isSelected());
        c.setDemandeAide(demandeAideCB.isSelected());
        c.setNiveauAutonomie(niveauAutonomieCB.getValue());
        c.setTravailTermine(travailTermineCB.isSelected());
        c.setDifficultes(difficultesTA.getText());
        c.setPointsPositifs(pointsPositifsTA.getText());
    }
    /* =========================================================
     *  VIDER LE FORMULAIRE
     * ========================================================= */
    private void clearForm() {
        dateEtudeDP.setValue(null);
        heureDebutTF.clear();
        heureFinTF.clear();
        dureeTotaleTF.clear();
        lieuTF.clear();
        matiereTF.clear();
        typeActiviteCB.setValue(null);
        niveauDifficulteCB.setValue(null);
        niveauConcentrationCB.setValue(null);
        niveauAgitationCB.setValue(null);
        niveauAutonomieCB.setValue(null);
        nombreInterruptionsTF.clear();
        tempsAvantPerteTF.clear();
        travailleSeulCB.setSelected(false);
        demandeAideCB.setSelected(false);
        travailTermineCB.setSelected(false);
        difficultesTA.clear();
        pointsPositifsTA.clear();
        tableCarnet.getSelectionModel().clearSelection();
        carnetSelectionne = null;
    }

    /* =========================================================
     *  ALERTE
     * ========================================================= */
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
