package tn.edu.esprit.controller;

import javafx.collections.FXCollections;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        heureDebutTF.textProperty().addListener((obs, oldVal, newVal) -> updateDuree());
        heureFinTF.textProperty().addListener((obs, oldVal, newVal) -> updateDuree());
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
    private void updateDuree() {
        try {
            if (!heureDebutTF.getText().isEmpty() && !heureFinTF.getText().isEmpty()) {

                LocalTime debut = parseHeure(heureDebutTF.getText().trim());
                LocalTime fin   = parseHeure(heureFinTF.getText().trim());

                int duree = calculerDuree(debut, fin);

                dureeTotaleTF.setText(String.valueOf(duree));

            }
        } catch (Exception e) {
            dureeTotaleTF.clear();
        }
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

        if (dateEtudeDP.getValue() == null) {
            throw new RuntimeException("La date est obligatoire !");
        }

        LocalTime debut = parseHeure(heureDebutTF.getText().trim());
        LocalTime fin   = parseHeure(heureFinTF.getText().trim());

        int duree = calculerDuree(debut, fin);

        verifierTexte(lieuTF.getText(), "Lieu");
        verifierTexte(matiereTF.getText(), "Matière");

        int minutesPerte = verifierMinutes(tempsAvantPerteTF.getText().trim());

        c.setDateEtude(Date.valueOf(dateEtudeDP.getValue()));
        c.setHeureDebut(Time.valueOf(debut));
        c.setHeureFin(Time.valueOf(fin));
        c.setDureeTotale(duree);
        c.setLieu(lieuTF.getText().trim());
        c.setMatiere(matiereTF.getText().trim());
        c.setTypeActivite(typeActiviteCB.getValue());
        c.setNiveauDifficulte(niveauDifficulteCB.getValue());
        c.setNiveauConcentration(niveauConcentrationCB.getValue());
        c.setNiveauAgitation(niveauAgitationCB.getValue());
        c.setNombreInterruptions(Integer.parseInt(nombreInterruptionsTF.getText().trim()));
        c.setTempsAvantPerteConcentration(minutesPerte);
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
    /* =========================================================
     *  OUVRIR INTERFACE AJOUTER COMMENTAIRE
     * ========================================================= */
    @FXML
    private void ouvrirAjouterCommentaire() {

        CarnetEducatif selected = tableCarnet.getSelectionModel().getSelectedItem();

        if (selected == null) {
            alert("⚠ Sélectionnez d'abord un carnet !");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/AjouterCommentaire.fxml")
            );

            Parent root = loader.load();

            // 🔥 Récupérer controller de Commentaire
            CommentaireController controller = loader.getController();

            // 🔥 Envoyer l'id du carnet sélectionné
            controller.setCarnetId(selected.getId());

            Stage stage = new Stage();
            stage.setTitle("Ajouter Commentaire");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            alert("❌ Impossible d'ouvrir l'interface AjouterCommentaire !");
        }
        
    }
    private LocalTime parseHeure(String heure) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            return LocalTime.parse(heure, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Format heure invalide ! Utilisez HH:mm (ex: 14:30)");
        }
    }
    private int calculerDuree(LocalTime debut, LocalTime fin) {

        if (fin.isBefore(debut)) {
            throw new RuntimeException("L'heure de fin doit être après l'heure de début !");
        }

        long minutes = Duration.between(debut, fin).toMinutes();
        return (int) minutes;
    }
    private void verifierTexte(String texte, String champ) {

        if (texte == null || texte.trim().isEmpty()) {
            throw new RuntimeException(champ + " est obligatoire !");
        }

        if (!texte.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new RuntimeException(champ + " doit contenir seulement des lettres !");
        }
    }
    private int verifierMinutes(String texte) {

        if (texte == null || texte.trim().isEmpty()) {
            throw new RuntimeException("Temps de perte de concentration est obligatoire !");
        }

        if (!texte.matches("\\d+")) {
            throw new RuntimeException("Temps perte doit être un nombre en minutes !");
        }

        int minutes = Integer.parseInt(texte);

        if (minutes <= 0) {
            throw new RuntimeException("Temps perte doit être supérieur à 0 !");
        }

        if (minutes > 600) {
            throw new RuntimeException("Temps perte trop grand !");
        }

        return minutes;
    }
}
