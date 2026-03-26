package tn.edu.esprit.controller;

import javafx.collections.FXCollections;

import tn.edu.esprit.entities.Commentaire;
import tn.edu.esprit.services.ServiceCommentaire;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalTime;
import java.time.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.edu.esprit.entities.CarnetEducatif;
import tn.edu.esprit.services.ServiceCarnetEducatif;

import java.sql.Date;
import java.sql.Time;
import javafx.collections.transformation.FilteredList;

public class CarnetController {

    /* =========================================================
     *  COLONNES DE LA TABLE CARNET
     * ========================================================= */
    @FXML private TableView<CarnetEducatif>            tableCarnet;
   /* @FXML private TableColumn<CarnetEducatif, Integer> colId;*/
    @FXML private TableColumn<CarnetEducatif, Date>    colDate;
    @FXML private TableColumn<CarnetEducatif, String>  colHeureDebut;
    @FXML private TableColumn<CarnetEducatif, String>  colHeureFin;
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
    @FXML private TableColumn<CarnetEducatif, String>  colDifficultes;
    @FXML private TableColumn<CarnetEducatif, String>  colPointsPositifs;

    /* =========================================================
     *  TABLE ET COLONNES COMMENTAIRES
     * ========================================================= */
    @FXML private TableView<Commentaire>               tableCommentairesCarnet;
    @FXML private TableColumn<Commentaire, Integer>    colComId;
    @FXML private TableColumn<Commentaire, Date>       colComDate;
    @FXML private TableColumn<Commentaire, String>     colComType;
    @FXML private TableColumn<Commentaire, String>     colComTexte;

    /* ── Filtres ── */
    @FXML private DatePicker       filtreDateDP;
    @FXML private ComboBox<String> filtreTypeCB;
    @FXML private Label            filtreCountLabel;

    /* ── ✅ Bandeau mode formulaire ── */
    @FXML private Label formModeLabel;

    /* =========================================================
     *  CHAMPS DU FORMULAIRE
     * ========================================================= */
    @FXML private DatePicker        dateEtudeDP;

    // ✅ Spinners heure début / fin
    @FXML private Spinner<Integer>  heureDebutH;
    @FXML private Spinner<Integer>  heureDebutM;
    @FXML private Spinner<Integer>  heureFinH;
    @FXML private Spinner<Integer>  heureFinM;

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
    @FXML private TextArea          difficultesTA;
    @FXML private TextArea          pointsPositifsTA;

    /* =========================================================
     *  DATA
     * ========================================================= */
    private final ServiceCarnetEducatif    service            = new ServiceCarnetEducatif();
    private final ServiceCommentaire       serviceCommentaire = new ServiceCommentaire();

    private ObservableList<CarnetEducatif> data;
    private CarnetEducatif                 carnetSelectionne;
    private FilteredList<CarnetEducatif>   filteredData;

    private boolean filtreListenersInitialises = false;

    /* =========================================================
     *  INITIALIZE
     * ========================================================= */
    @FXML
    public void initialize() {
    	/*colId.setVisible(false);
        /* -- Colonnes carnet -- */
        /*colId            .setCellValueFactory(new PropertyValueFactory<>("id"));*/
        colDate          .setCellValueFactory(new PropertyValueFactory<>("dateEtude"));
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
        colDifficultes   .setCellValueFactory(new PropertyValueFactory<>("difficultes"));
        colPointsPositifs.setCellValueFactory(new PropertyValueFactory<>("pointsPositifs"));

        /* -- Colonnes heure avec icônes -- */
        colHeureDebut.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        "🕒 " + cell.getValue().getHeureDebut().toString().substring(0, 5)
                )
        );
        colHeureFin.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        "→ " + cell.getValue().getHeureFin().toString().substring(0, 5)
                )
        );

        /* -- Colonnes commentaires -- */
        colComId   .setCellValueFactory(new PropertyValueFactory<>("id"));
        colComDate .setCellValueFactory(new PropertyValueFactory<>("dateCommentaire"));
        colComType .setCellValueFactory(new PropertyValueFactory<>("typeCommentaire"));
        colComTexte.setCellValueFactory(new PropertyValueFactory<>("texteCommentaire"));

        /* -- ComboBox formulaire -- */
        typeActiviteCB    .getItems().addAll("Lecture", "Exercice", "Quiz", "Jeu éducatif");
        niveauDifficulteCB.getItems().addAll("Facile", "Moyen", "Difficile");
        for (int i = 1; i <= 5; i++) {
            niveauConcentrationCB.getItems().add(i);
            niveauAgitationCB    .getItems().add(i);
            niveauAutonomieCB    .getItems().add(i);
        }

        /* -- ComboBox filtre type -- */
        filtreTypeCB.getItems().addAll("Lecture", "Exercice", "Quiz", "Jeu éducatif");

        /* ── Configuration des spinners heure ── */
        heureDebutH.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        heureDebutM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0, 5));
        heureFinH  .setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        heureFinM  .setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0, 5));
        heureDebutH.setEditable(true);
        heureDebutM.setEditable(true);
        heureFinH  .setEditable(true);
        heureFinM  .setEditable(true);

        /* -- Listeners durée auto -- */
        heureDebutH.valueProperty().addListener((obs, o, n) -> updateDuree());
        heureDebutM.valueProperty().addListener((obs, o, n) -> updateDuree());
        heureFinH  .valueProperty().addListener((obs, o, n) -> updateDuree());
        heureFinM  .valueProperty().addListener((obs, o, n) -> updateDuree());
        updateDuree();

        /* -- Listeners filtre -- */
        filtreDateDP.valueProperty().addListener((obs, o, n) -> { filtrer(); majBadge(); });
        filtreTypeCB.valueProperty().addListener((obs, o, n) -> { filtrer(); majBadge(); });
        filtreListenersInitialises = true;

        /* =========================================================
         *  Sélection ligne → remplir formulaire + mettre à jour bandeau
         * ========================================================= */
        tableCarnet.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, selected) -> {
                if (selected != null) {
                    carnetSelectionne = selected;

                    // ✅ Bandeau passe en mode "Modification"
                    if (formModeLabel != null)
                        formModeLabel.setText("📝  Modification du carnet #" + selected.getId());

                    LocalTime debut = selected.getHeureDebut().toLocalTime();
                    LocalTime fin   = selected.getHeureFin().toLocalTime();
                    heureDebutH.getValueFactory().setValue(debut.getHour());
                    heureDebutM.getValueFactory().setValue((debut.getMinute() / 5) * 5);
                    heureFinH  .getValueFactory().setValue(fin.getHour());
                    heureFinM  .getValueFactory().setValue((fin.getMinute() / 5) * 5);

                    dureeTotaleTF        .setText(String.valueOf(selected.getDureeTotale()));
                    lieuTF               .setText(selected.getLieu());
                    matiereTF            .setText(selected.getMatiere());
                    typeActiviteCB       .setValue(selected.getTypeActivite());
                    niveauDifficulteCB   .setValue(selected.getNiveauDifficulte());
                    niveauConcentrationCB.setValue(selected.getNiveauConcentration());
                    niveauAgitationCB    .setValue(selected.getNiveauAgitation());
                    niveauAutonomieCB    .setValue(selected.getNiveauAutonomie());
                    nombreInterruptionsTF.setText(String.valueOf(selected.getNombreInterruptions()));
                    tempsAvantPerteTF    .setText(String.valueOf(selected.getTempsAvantPerteConcentration()));
                    travailleSeulCB      .setSelected(selected.isTravailleSeul());
                    demandeAideCB        .setSelected(selected.isDemandeAide());
                    difficultesTA        .setText(selected.getDifficultes());
                    pointsPositifsTA     .setText(selected.getPointsPositifs());

                    chargerCommentairesDuCarnet(selected.getId());

                } else {
                    tableCommentairesCarnet.setItems(FXCollections.observableArrayList());
                }
            }
        );

        //chargerDonnees();
        tableCarnet.setItems(FXCollections.observableArrayList());
    }

    /* =========================================================
     *  ✅ NOUVEAU / VIDER FORMULAIRE
     *  Déclenché par le bouton "Vider / Nouveau" (bandeau)
     *  ET par le bouton rond "Nouveau" dans la sidebar
     * ========================================================= */
    @FXML
    public void nouveauCarnet() {
        clearForm();
        if (formModeLabel != null)
            formModeLabel.setText("✏️  Nouveau carnet");
    }

    /* =========================================================
     *  HELPERS SPINNERS → LocalTime
     * ========================================================= */
    private LocalTime getHeureDebut() {
        return LocalTime.of(heureDebutH.getValue(), heureDebutM.getValue());
    }

    private LocalTime getHeureFin() {
        return LocalTime.of(heureFinH.getValue(), heureFinM.getValue());
    }

    /* =========================================================
     *  CHARGER DONNÉES
     * ========================================================= */
    private void chargerDonnees() {
        data         = FXCollections.observableArrayList(service.getAll());
        filteredData = new FilteredList<>(data, b -> true);
        tableCarnet.setItems(filteredData);
        filtrer();
        majBadge();
    }

    @FXML
    private void consulterCarnets() {
        chargerDonnees();
    }

    /* =========================================================
     *  CHARGER COMMENTAIRES
     * ========================================================= */
    private void chargerCommentairesDuCarnet(int carnetId) {
        try {
            List<Commentaire> commentaires = serviceCommentaire.getByCarnetId(carnetId);
            tableCommentairesCarnet.setItems(FXCollections.observableArrayList(commentaires));
            tableCommentairesCarnet.refresh();
        } catch (Exception e) {
            System.out.println("❌ Erreur chargement commentaires : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ── Durée auto ── */
    private void updateDuree() {
        try {
            dureeTotaleTF.setText(String.valueOf(calculerDuree(getHeureDebut(), getHeureFin())));
        } catch (Exception e) {
            dureeTotaleTF.clear();
        }
    }

    /* =========================================================
     *  AJOUTER / MODIFIER / SUPPRIMER
     * ========================================================= */
    @FXML
    private void ajouterCarnet() {
        try {
            CarnetEducatif c = new CarnetEducatif();
            remplirDepuisFormulaire(c);
            service.ajouter(c);
            chargerDonnees();
            clearForm();
            alert("✔ Carnet ajouté avec succès !");
        } catch (Exception e) {
            alert("❌ Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void modifierCarnet() {
        if (carnetSelectionne == null) { alert("⚠ Sélectionnez d'abord une ligne !"); return; }
        try {
            remplirDepuisFormulaire(carnetSelectionne);
            service.modifier(carnetSelectionne);
            chargerDonnees();
            clearForm();
            alert("✔ Carnet modifié avec succès !");
        } catch (Exception e) {
            alert("❌ Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerCarnet() {
        CarnetEducatif sel = tableCarnet.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("⚠ Sélectionnez d'abord une ligne !"); return; }
        service.supprimer(sel.getId());
        chargerDonnees();
        clearForm();
        alert("✔ Carnet supprimé avec succès !");
    }

    /* =========================================================
     *  FILTRES
     * ========================================================= */
    private void filtrer() {
        if (filteredData == null) return;
        filteredData.setPredicate(carnet -> {
            if (filtreDateDP.getValue() != null
                    && !carnet.getDateEtude().toLocalDate().equals(filtreDateDP.getValue()))
                return false;
            if (filtreTypeCB.getValue() != null
                    && !carnet.getTypeActivite().equals(filtreTypeCB.getValue()))
                return false;
            return true;
        });
    }

    private void majBadge() {
        if (filtreCountLabel == null || filteredData == null) return;
        int n = filteredData.size();
        boolean aucunFiltre = filtreDateDP.getValue() == null && filtreTypeCB.getValue() == null;
        filtreCountLabel.setText(aucunFiltre ? "● Tous  (" + n + ")" : "● " + n + " résultat" + (n > 1 ? "s" : ""));
    }

    @FXML
    public void resetFiltre() {
        filtreDateDP.setValue(null);
        filtreTypeCB.setValue(null);
    }

    /* =========================================================
     *  HELPERS FORMULAIRE
     * ========================================================= */
    private void remplirDepuisFormulaire(CarnetEducatif c) {
        if (dateEtudeDP.getValue() == null) throw new RuntimeException("La date est obligatoire !");
        LocalTime debut  = getHeureDebut();
        LocalTime fin    = getHeureFin();
        int duree        = calculerDuree(debut, fin);
        verifierTexte(lieuTF   .getText(), "Lieu");
        verifierTexte(matiereTF.getText(), "Matière");
        int minutesPerte = verifierMinutes(tempsAvantPerteTF.getText().trim());

        c.setDateEtude                   (Date.valueOf(dateEtudeDP.getValue()));
        c.setHeureDebut                  (Time.valueOf(debut));
        c.setHeureFin                    (Time.valueOf(fin));
        c.setDureeTotale                 (duree);
        c.setLieu                        (lieuTF   .getText().trim());
        c.setMatiere                     (matiereTF.getText().trim());
        c.setTypeActivite                (typeActiviteCB       .getValue());
        c.setNiveauDifficulte            (niveauDifficulteCB   .getValue());
        c.setNiveauConcentration         (niveauConcentrationCB.getValue());
        c.setNiveauAgitation             (niveauAgitationCB    .getValue());
        c.setNombreInterruptions         (Integer.parseInt(nombreInterruptionsTF.getText().trim()));
        c.setTempsAvantPerteConcentration(minutesPerte);
        c.setTravailleSeul               (travailleSeulCB.isSelected());
        c.setDemandeAide                 (demandeAideCB  .isSelected());
        c.setNiveauAutonomie             (niveauAutonomieCB.getValue());
        c.setDifficultes                 (difficultesTA   .getText());
        c.setPointsPositifs              (pointsPositifsTA.getText());
    }

    private void clearForm() {
        dateEtudeDP          .setValue(null);
        heureDebutH.getValueFactory().setValue(8);
        heureDebutM.getValueFactory().setValue(0);
        heureFinH  .getValueFactory().setValue(9);
        heureFinM  .getValueFactory().setValue(0);
        dureeTotaleTF        .clear();
        lieuTF               .clear();
        matiereTF            .clear();
        typeActiviteCB       .setValue(null);
        niveauDifficulteCB   .setValue(null);
        niveauConcentrationCB.setValue(null);
        niveauAgitationCB    .setValue(null);
        niveauAutonomieCB    .setValue(null);
        nombreInterruptionsTF.clear();
        tempsAvantPerteTF    .clear();
        travailleSeulCB      .setSelected(false);
        demandeAideCB        .setSelected(false);
        difficultesTA        .clear();
        pointsPositifsTA     .clear();
        tableCarnet.getSelectionModel().clearSelection();
        tableCommentairesCarnet.setItems(FXCollections.observableArrayList());
        carnetSelectionne = null;
        updateDuree();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    private int calculerDuree(LocalTime debut, LocalTime fin) {
        if (fin.isBefore(debut)) throw new RuntimeException("L'heure de fin doit être après le début !");
        return (int) Duration.between(debut, fin).toMinutes();
    }

    private void verifierTexte(String t, String champ) {
        if (t == null || t.trim().isEmpty()) throw new RuntimeException(champ + " est obligatoire !");
        if (!t.matches("[a-zA-ZÀ-ÿ\\s]+"))  throw new RuntimeException(champ + " doit contenir seulement des lettres !");
    }

    private int verifierMinutes(String t) {
        if (t == null || t.trim().isEmpty()) throw new RuntimeException("Temps perte obligatoire !");
        if (!t.matches("\\d+"))              throw new RuntimeException("Temps perte doit être un nombre !");
        int m = Integer.parseInt(t);
        if (m <= 0)   throw new RuntimeException("Temps perte doit être > 0 !");
        if (m > 600)  throw new RuntimeException("Temps perte trop grand !");
        return m;
    }

    /* =========================================================
     *  OUVRIR COMMENTAIRE
     * ========================================================= */
    @FXML
    private void ouvrirAjouterCommentaire() {
        CarnetEducatif sel = tableCarnet.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("⚠ Sélectionnez d'abord un carnet !"); return; }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjouterCommentaire.fxml"));
            Parent root = loader.load();
            CommentaireController ctrl = loader.getController();
            ctrl.setCarnetId(sel.getId());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setWidth(1100);
            stage.setHeight(720);
            stage.setMaximized(false);
            stage.setFullScreen(false);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("Ajouter Commentaire");
            stage.setOnHidden(e -> chargerCommentairesDuCarnet(sel.getId()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            alert("❌ Impossible d'ouvrir AjouterCommentaire !");
        }
    }

    /* =========================================================
     *  IMPRIMER PDF
     * ========================================================= */
    @FXML
    private void imprimerCarnet() {
        CarnetEducatif sel = tableCarnet.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("⚠ Sélectionnez un carnet dans le tableau !"); return; }
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Enregistrer le PDF");
            fc.setInitialFileName("carnet_" + sel.getDateEtude() + ".pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File f = fc.showSaveDialog(tableCarnet.getScene().getWindow());
            if (f != null) {
                tn.edu.esprit.tools.PdfExporter.exporterCarnet(sel, f.getAbsolutePath());
                alert("✔ PDF stylé généré avec succès !");
            }
        } catch (Exception e) {
            alert("❌ Erreur PDF : " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void genererPDF(CarnetEducatif c, String chemin) {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(chemin));
            doc.open();

            Font titreFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

            // ===== TITRE =====
            Paragraph titre = new Paragraph("📘 Carnet Educatif", titreFont);
            titre.setAlignment(Element.ALIGN_CENTER);
            doc.add(titre);
            doc.add(new Paragraph(" "));

            // ===== INFOS CARNET =====
            doc.add(new Paragraph("Date : " + c.getDateEtude(), normalFont));
            doc.add(new Paragraph("Heure début : " + c.getHeureDebut(), normalFont));
            doc.add(new Paragraph("Heure fin : " + c.getHeureFin(), normalFont));
            doc.add(new Paragraph("Durée : " + c.getDureeTotale() + " min", normalFont));
            doc.add(new Paragraph("Lieu : " + c.getLieu(), normalFont));
            doc.add(new Paragraph("Matière : " + c.getMatiere(), normalFont));
            doc.add(new Paragraph("Type : " + c.getTypeActivite(), normalFont));
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("Difficultés : " + c.getDifficultes(), normalFont));
            doc.add(new Paragraph("Points positifs : " + c.getPointsPositifs(), normalFont));

            doc.add(new Paragraph(" "));

            // ======================================================
            // 💬 AJOUT DES COMMENTAIRES
            // ======================================================
            doc.add(new Paragraph("💬 Commentaires :", sectionFont));
            doc.add(new Paragraph(" "));

            List<Commentaire> commentaires = serviceCommentaire.getByCarnetId(c.getId());

            if (commentaires.isEmpty()) {
                doc.add(new Paragraph("Aucun commentaire.", normalFont));
            } else {
                for (Commentaire com : commentaires) {
                    doc.add(new Paragraph("📅 Date : " + com.getDateCommentaire(), normalFont));
                    doc.add(new Paragraph("📝 Type : " + com.getTypeCommentaire(), normalFont));
                    doc.add(new Paragraph("💭 " + com.getTexteCommentaire(), normalFont));
                    doc.add(new Paragraph("--------------------------------------------------"));
                }
            }

            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =========================================================
     *  GRAPHIQUES
     * ========================================================= */
    @FXML
    void ouvrirGraphiques() {
        try {
            java.net.URL fxmlUrl = getClass().getResource("/view/Graphique.fxml");
            if (fxmlUrl == null) { alert("❌ Fichier FXML introuvable !"); return; }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            GraphiqueController controller = loader.getController();
            controller.setData(tableCarnet.getItems());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("📊 Graphiques");
            stage.setWidth(950);
            stage.setHeight(600);
            stage.setMinWidth(950);
            stage.setMinHeight(700);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            alert("❌ Erreur ouverture graphiques : " + e.getMessage());
        }
    }
}
