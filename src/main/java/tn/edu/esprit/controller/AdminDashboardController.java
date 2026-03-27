package tn.edu.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.edu.esprit.entities.CarnetEducatif;
import tn.edu.esprit.entities.Commentaire;
import tn.edu.esprit.services.ServiceCarnetEducatif;
import tn.edu.esprit.services.ServiceCommentaire;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    // Services
    private final ServiceCarnetEducatif carnetService = new ServiceCarnetEducatif();
    private final ServiceCommentaire commentaireService = new ServiceCommentaire();

    // ✅ Navigation — UNIQUEMENT les vues centrales, plus de navCarnets/navCommentaires/navStats
    @FXML private VBox carnetsView;
    @FXML private VBox commentairesView;
    @FXML private VBox statsView;

    // Boutons sidebar — pour highlight actif
    @FXML private Button btnNavCarnets;
    @FXML private Button btnNavCommentaires;
    @FXML private Button btnNavStats;

    // Carnets - Table et colonnes
    @FXML private TableView<CarnetRow> carnetsTable;
    @FXML private TableColumn<CarnetRow, Integer> colCarnetId;
    @FXML private TableColumn<CarnetRow, String>  colCarnetDate;
    @FXML private TableColumn<CarnetRow, String>  colCarnetHeureDebut;
    @FXML private TableColumn<CarnetRow, String>  colCarnetHeureFin;
    @FXML private TableColumn<CarnetRow, String>  colCarnetDuree;
    @FXML private TableColumn<CarnetRow, String>  colCarnetLieu;
    @FXML private TableColumn<CarnetRow, String>  colCarnetMatiere;
    @FXML private TableColumn<CarnetRow, String>  colCarnetTypeActivite;
    @FXML private TableColumn<CarnetRow, String>  colCarnetDifficulte;
    @FXML private TableColumn<CarnetRow, Integer> colCarnetConcentration;
    @FXML private TableColumn<CarnetRow, Integer> colCarnetAgitation;
    @FXML private TableColumn<CarnetRow, Integer> colCarnetAutonomie;
    @FXML private TableColumn<CarnetRow, Integer> colCarnetInterruptions;
    @FXML private TableColumn<CarnetRow, Integer> colCarnetTempsPerte;
    @FXML private TableColumn<CarnetRow, Boolean> colCarnetTravailleSeul;
    @FXML private TableColumn<CarnetRow, Boolean> colCarnetDemandeAide;
    @FXML private TableColumn<CarnetRow, String>  colCarnetDifficultes;
    @FXML private TableColumn<CarnetRow, String>  colCarnetPointsPositifs;
    @FXML private TableColumn<CarnetRow, Void>    colCarnetActions;

    // Filtres carnets
    @FXML private TextField          carnetsSearchField;
    @FXML private DatePicker         carnetsDateDebut;
    @FXML private DatePicker         carnetsDateFin;
    @FXML private ComboBox<String>   carnetsTypeActivite;
    @FXML private Label              carnetsCountBadge;

    // Formulaire Carnet
    @FXML private VBox             carnetFormPane;
    @FXML private Label            carnetFormModeLabel;
    @FXML private DatePicker       dateEtudeDP;
    @FXML private TextField        matiereTF;
    @FXML private Spinner<Integer> heureDebutH;
    @FXML private Spinner<Integer> heureDebutM;
    @FXML private Spinner<Integer> heureFinH;
    @FXML private Spinner<Integer> heureFinM;
    @FXML private TextField        dureeTotaleTF;
    @FXML private TextField        lieuTF;
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

    private final ObservableList<CarnetRow> allCarnets = FXCollections.observableArrayList();
    private FilteredList<CarnetRow> filteredCarnets;
    private CarnetRow selectedCarnet = null;
    private boolean isEditMode = false;

    // Commentaires
    @FXML private TableView<CommentRow>          commentsTable;
    @FXML private TableColumn<CommentRow, Integer> colCommId;
    @FXML private TableColumn<CommentRow, String>  colCommDate;
    @FXML private TableColumn<CommentRow, String>  colCommUtilisateur;
    @FXML private TableColumn<CommentRow, String>  colCommCarnet;
    @FXML private TableColumn<CommentRow, String>  colCommContenu;
    @FXML private TableColumn<CommentRow, String>  colCommStatut;
    @FXML private TableColumn<CommentRow, Void>    colCommActions;

    @FXML private TextField        commSearchField;
    @FXML private DatePicker       commDateDebut;
    @FXML private DatePicker       commDateFin;
    @FXML private ComboBox<String> commStatutFilter;
    @FXML private Label            commCountBadge;
    @FXML private Label            commInfoLabel;
    @FXML private VBox             commDetailPane;
    @FXML private TextArea         commDetailText;

    private final ObservableList<CommentRow> allComments = FXCollections.observableArrayList();
    private FilteredList<CommentRow> filteredComments;

    // Stats
    @FXML private Label kpiTotalCarnets;
    @FXML private Label kpiTotalComm;
    @FXML private Label kpiTotalUsers;
   // @FXML private Label kpiSignalements;

    // =========================================================
    //  INITIALIZE
    // =========================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCarnets();
        initCommentaires();
        chargerDonneesDepuisBase();
        updateStats();
        // Vue par défaut = Carnets, bouton Carnets actif
        setView(carnetsView, btnNavCarnets);
    }

    // =========================================================
    //  NAVIGATION — sidebar seulement, navbar du bas est fixe
    // =========================================================
    @FXML public void showCarnets()      { setView(carnetsView,      btnNavCarnets);      }
    @FXML public void showCommentaires() { setView(commentairesView, btnNavCommentaires); }
    @FXML public void showStats()        { setView(statsView,        btnNavStats); updateStats(); }

    /**
     * Affiche la vue cible et met le bouton sidebar actif en surbrillance.
     * La navbar du bas n'est PAS touchée — elle reste toujours fixe.
     */
    private void setView(VBox targetView, Button activeBtn) {
        // 1. Masquer toutes les vues centrales
        for (VBox v : new VBox[]{carnetsView, commentairesView, statsView}) {
            v.setVisible(false);
            v.setManaged(false);
        }
        // 2. Afficher la vue cible
        targetView.setVisible(true);
        targetView.setManaged(true);

        // 3. Remettre tous les boutons sidebar à l'état normal
        for (Button btn : new Button[]{btnNavCarnets, btnNavCommentaires, btnNavStats}) {
            btn.getStyleClass().remove("round-button-active");
            btn.getStyleClass().add("round-button");
        }
        // 4. Mettre le bouton actif en surbrillance
        activeBtn.getStyleClass().remove("round-button");
        activeBtn.getStyleClass().add("round-button-active");
    }

    // =========================================================
    //  CARNETS
    // =========================================================
    private void initCarnets() {
        carnetsTypeActivite.setItems(FXCollections.observableArrayList(
                "Tous", "Lecture", "Dessin", "Sport", "Musique", "Jeux éducatifs", "Autre"));
        carnetsTypeActivite.getSelectionModel().selectFirst();

        typeActiviteCB.setItems(FXCollections.observableArrayList(
                "Lecture", "Dessin", "Sport", "Musique", "Jeux éducatifs", "Autre"));
        niveauDifficulteCB.setItems(FXCollections.observableArrayList("Facile", "Moyen", "Difficile"));
        niveauConcentrationCB.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));
        niveauAgitationCB.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));
        niveauAutonomieCB.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));

        initSpinner(heureDebutH, 0, 23, 8);
        initSpinner(heureDebutM, 0, 59, 0);
        initSpinner(heureFinH,   0, 23, 9);
        initSpinner(heureFinM,   0, 59, 0);

        heureDebutH.valueProperty().addListener((obs, o, v) -> updateDuree());
        heureDebutM.valueProperty().addListener((obs, o, v) -> updateDuree());
        heureFinH.valueProperty().addListener((obs, o, v)   -> updateDuree());
        heureFinM.valueProperty().addListener((obs, o, v)   -> updateDuree());

        colCarnetId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCarnetDate.setCellValueFactory(new PropertyValueFactory<>("dateStr"));
        colCarnetHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebutStr"));
        colCarnetHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFinStr"));
        colCarnetDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colCarnetLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colCarnetMatiere.setCellValueFactory(new PropertyValueFactory<>("matiere"));
        colCarnetTypeActivite.setCellValueFactory(new PropertyValueFactory<>("typeActivite"));
        colCarnetDifficulte.setCellValueFactory(new PropertyValueFactory<>("difficulte"));
        colCarnetConcentration.setCellValueFactory(new PropertyValueFactory<>("concentration"));
        colCarnetAgitation.setCellValueFactory(new PropertyValueFactory<>("agitation"));
        colCarnetAutonomie.setCellValueFactory(new PropertyValueFactory<>("autonomie"));
        colCarnetInterruptions.setCellValueFactory(new PropertyValueFactory<>("interruptions"));
        colCarnetTempsPerte.setCellValueFactory(new PropertyValueFactory<>("tempsPerte"));
        colCarnetTravailleSeul.setCellValueFactory(new PropertyValueFactory<>("travailleSeul"));
        colCarnetDemandeAide.setCellValueFactory(new PropertyValueFactory<>("demandeAide"));
        colCarnetDifficultes.setCellValueFactory(new PropertyValueFactory<>("difficultes"));
        colCarnetPointsPositifs.setCellValueFactory(new PropertyValueFactory<>("pointsPositifs"));
        buildCarnetActionsColumn();

        filteredCarnets = new FilteredList<>(allCarnets, p -> true);
        carnetsTable.setItems(filteredCarnets);
        carnetsTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, sel) -> { if (sel != null) selectedCarnet = sel; });
        updateCarnetBadge();
    }

    private void initSpinner(Spinner<Integer> sp, int min, int max, int init) {
        sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, init));
        sp.setEditable(true);
    }

    private void updateDuree() {
        int duree = (heureFinH.getValue() * 60 + heureFinM.getValue())
                  - (heureDebutH.getValue() * 60 + heureDebutM.getValue());
        dureeTotaleTF.setText(duree > 0 ? String.valueOf(duree) : "0");
    }

    private void buildCarnetActionsColumn() {
        colCarnetActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnView = new Button("👁️");
            private final Button btnEdit = new Button("✏️");
            private final Button btnDel  = new Button("🗑️");
            private final HBox box = new HBox(5, btnView, btnEdit, btnDel);
            {
                box.setAlignment(Pos.CENTER);
                btnView.getStyleClass().add("action-icon-view");
                btnEdit.getStyleClass().add("action-icon-edit");
                btnDel .getStyleClass().add("action-icon-del");
                btnView.setTooltip(new Tooltip("Consulter"));
                btnEdit.setTooltip(new Tooltip("Modifier"));
                btnDel .setTooltip(new Tooltip("Supprimer"));
                btnView.setOnAction(e -> consulterCarnet(getTableView().getItems().get(getIndex())));
                btnEdit.setOnAction(e -> openEditCarnetForm(getTableView().getItems().get(getIndex())));
                btnDel .setOnAction(e -> confirmDeleteCarnet(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void consulterCarnet(CarnetRow row) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Détail du carnet");
        dialog.setHeaderText("Carnet #" + row.getId() + " - " + row.getTypeActivite());
        StringBuilder sb = new StringBuilder();
        sb.append("📅 Date : ").append(row.getDateStr()).append("\n");
        sb.append("🕐 Début : ").append(row.getHeureDebutStr()).append("\n");
        sb.append("🕐 Fin : ").append(row.getHeureFinStr()).append("\n");
        sb.append("⏱️ Durée : ").append(row.getDuree()).append("\n");
        sb.append("📍 Lieu : ").append(row.getLieu()).append("\n");
        sb.append("📚 Matière : ").append(row.getMatiere()).append("\n");
        sb.append("🎯 Type : ").append(row.getTypeActivite()).append("\n");
        sb.append("⚡ Difficulté : ").append(row.getDifficulte()).append("\n");
        sb.append("🧠 Concentration : ").append(row.getConcentration()).append("/10\n");
        sb.append("🌀 Agitation : ").append(row.getAgitation()).append("/10\n");
        sb.append("🦋 Autonomie : ").append(row.getAutonomie()).append("/10\n");
        sb.append("🔔 Interruptions : ").append(row.getInterruptions()).append("\n");
        sb.append("⏳ Temps perte : ").append(row.getTempsPerte()).append(" min\n");
        sb.append("👤 Travaille seul : ").append(row.isTravailleSeul() ? "Oui" : "Non").append("\n");
        sb.append("🤝 Demande aide : ").append(row.isDemandeAide() ? "Oui" : "Non").append("\n");
        sb.append("⚠️ Difficultés : ").append(row.getDifficultes()).append("\n");
        sb.append("✅ Points positifs : ").append(row.getPointsPositifs());
        dialog.setContentText(sb.toString());
        dialog.showAndWait();
    }

    @FXML public void onCarnetsSearch() { applyCarnetFilters(); }

    @FXML
    public void applyCarnetFilters() {
        String txt   = carnetsSearchField.getText().toLowerCase().trim();
        LocalDate d1 = carnetsDateDebut.getValue();
        LocalDate d2 = carnetsDateFin.getValue();
        String type  = carnetsTypeActivite.getValue();
        filteredCarnets.setPredicate(row -> {
            boolean okText = txt.isEmpty() ||
                    row.getTypeActivite().toLowerCase().contains(txt) ||
                    row.getLieu().toLowerCase().contains(txt) ||
                    row.getMatiere().toLowerCase().contains(txt);
            LocalDate d = row.getDate();
            boolean okD1   = d1 == null || !d.isBefore(d1);
            boolean okD2   = d2 == null || !d.isAfter(d2);
            boolean okType = type == null || type.equals("Tous") ||
                    row.getTypeActivite().equalsIgnoreCase(type);
            return okText && okD1 && okD2 && okType;
        });
        updateCarnetBadge();
    }

    @FXML
    public void resetCarnetFilters() {
        carnetsSearchField.clear();
        carnetsDateDebut.setValue(null);
        carnetsDateFin.setValue(null);
        carnetsTypeActivite.getSelectionModel().selectFirst();
        filteredCarnets.setPredicate(p -> true);
        updateCarnetBadge();
    }

    private void updateCarnetBadge() {
        int n = filteredCarnets.size();
        carnetsCountBadge.setText(n + " résultat" + (n > 1 ? "s" : ""));
    }

    /*@FXML
    public void handleNewCarnet() {
        isEditMode = false;
        selectedCarnet = null;
        carnetFormModeLabel.setText("✏️  Nouveau Carnet");
        clearCarnetForm();
        toggleCarnetForm(true);
    }*/

    private void openEditCarnetForm(CarnetRow row) {
        isEditMode = true;
        selectedCarnet = row;
        carnetFormModeLabel.setText("✏️  Modifier Carnet #" + row.getId());
        populateCarnetForm(row);
        toggleCarnetForm(true);
    }

    @FXML public void hideCarnetForm() { toggleCarnetForm(false); }

    private void toggleCarnetForm(boolean show) {
        carnetFormPane.setVisible(show);
        carnetFormPane.setManaged(show);
    }

    private void clearCarnetForm() {
        dateEtudeDP.setValue(LocalDate.now());
        matiereTF.clear();
        heureDebutH.getValueFactory().setValue(8);
        heureDebutM.getValueFactory().setValue(0);
        heureFinH.getValueFactory().setValue(9);
        heureFinM.getValueFactory().setValue(0);
        lieuTF.clear();
        typeActiviteCB.getSelectionModel().clearSelection();
        niveauDifficulteCB.getSelectionModel().clearSelection();
        niveauConcentrationCB.getSelectionModel().clearSelection();
        niveauAgitationCB.getSelectionModel().clearSelection();
        niveauAutonomieCB.getSelectionModel().clearSelection();
        nombreInterruptionsTF.clear();
        tempsAvantPerteTF.clear();
        travailleSeulCB.setSelected(false);
        demandeAideCB.setSelected(false);
        difficultesTA.clear();
        pointsPositifsTA.clear();
        updateDuree();
    }

    private void populateCarnetForm(CarnetRow row) {
        dateEtudeDP.setValue(row.getDate());
        matiereTF.setText(row.getMatiere());
        String[] hDeb = row.getHeureDebutStr().split(":");
        String[] hFin = row.getHeureFinStr().split(":");
        heureDebutH.getValueFactory().setValue(Integer.parseInt(hDeb[0]));
        heureDebutM.getValueFactory().setValue(Integer.parseInt(hDeb[1]));
        heureFinH.getValueFactory().setValue(Integer.parseInt(hFin[0]));
        heureFinM.getValueFactory().setValue(Integer.parseInt(hFin[1]));
        lieuTF.setText(row.getLieu());
        typeActiviteCB.setValue(row.getTypeActivite());
        niveauDifficulteCB.setValue(row.getDifficulte());
        niveauConcentrationCB.setValue(row.getConcentration());
        niveauAgitationCB.setValue(row.getAgitation());
        niveauAutonomieCB.setValue(row.getAutonomie());
        nombreInterruptionsTF.setText(String.valueOf(row.getInterruptions()));
        tempsAvantPerteTF.setText(String.valueOf(row.getTempsPerte()));
        travailleSeulCB.setSelected(row.isTravailleSeul());
        demandeAideCB.setSelected(row.isDemandeAide());
        difficultesTA.setText(row.getDifficultes());
        pointsPositifsTA.setText(row.getPointsPositifs());
        updateDuree();
    }

    @FXML
    public void saveCarnet() {
        if (dateEtudeDP.getValue() == null) {
            showAlert("Validation", "Veuillez choisir une date.", Alert.AlertType.WARNING);
            return;
        }
        if (typeActiviteCB.getValue() == null) {
            showAlert("Validation", "Veuillez choisir un type d'activité.", Alert.AlertType.WARNING);
            return;
        }
        int hD = heureDebutH.getValue(), mD = heureDebutM.getValue();
        int hF = heureFinH.getValue(),   mF = heureFinM.getValue();
        if (hF * 60 + mF <= hD * 60 + mD) {
            showAlert("Validation", "L'heure de fin doit être après l'heure de début.", Alert.AlertType.WARNING);
            return;
        }

        LocalDate date      = dateEtudeDP.getValue();
        String matiere      = matiereTF.getText().trim().isEmpty() ? "Général" : matiereTF.getText().trim();
        String lieu         = lieuTF.getText().trim().isEmpty() ? "Maison" : lieuTF.getText().trim();
        String typeActivite = typeActiviteCB.getValue();
        String difficulte   = niveauDifficulteCB.getValue() != null ? niveauDifficulteCB.getValue() : "Moyen";
        int concentration   = niveauConcentrationCB.getValue() != null ? niveauConcentrationCB.getValue() : 5;
        int agitation       = niveauAgitationCB.getValue() != null ? niveauAgitationCB.getValue() : 3;
        int autonomie       = niveauAutonomieCB.getValue() != null ? niveauAutonomieCB.getValue() : 5;
        int dureeTotale     = (hF * 60 + mF) - (hD * 60 + mD);
        int interruptions;
        try { interruptions = Integer.parseInt(nombreInterruptionsTF.getText().trim()); }
        catch (NumberFormatException e) { interruptions = 0; }
        int tempsPerte;
        try { tempsPerte = Integer.parseInt(tempsAvantPerteTF.getText().trim()); }
        catch (NumberFormatException e) { tempsPerte = 30; }

        Time heureDebut = Time.valueOf(LocalTime.of(hD, mD));
        Time heureFin   = Time.valueOf(LocalTime.of(hF, mF));

        CarnetEducatif c = new CarnetEducatif();
        c.setDateEtude(Date.valueOf(date));
        c.setHeureDebut(heureDebut);
        c.setHeureFin(heureFin);
        c.setDureeTotale(dureeTotale);
        c.setLieu(lieu);
        c.setMatiere(matiere);
        c.setTypeActivite(typeActivite);
        c.setNiveauDifficulte(difficulte);
        c.setNiveauConcentration(concentration);
        c.setNiveauAgitation(agitation);
        c.setNiveauAutonomie(autonomie);
        c.setNombreInterruptions(interruptions);
        c.setTempsAvantPerteConcentration(tempsPerte);
        c.setTravailleSeul(travailleSeulCB.isSelected());
        c.setDemandeAide(demandeAideCB.isSelected());
        c.setDifficultes(difficultesTA.getText());
        c.setPointsPositifs(pointsPositifsTA.getText());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean success = false;

        if (isEditMode && selectedCarnet != null) {
            c.setId(selectedCarnet.getId());
            // Conserver TravailTermine existant
            for (CarnetEducatif ce : carnetService.getAll()) {
                if (ce.getId() == selectedCarnet.getId()) {
                    c.setTravailTermine(ce.isTravailTermine());
                    break;
                }
            }
            try { carnetService.modifier(c); success = true; }
            catch (Exception e) { e.printStackTrace(); }

            if (success) {
                selectedCarnet.setDate(date);
                selectedCarnet.setDateStr(date.format(fmt));
                selectedCarnet.setHeureDebutStr(String.format("%02d:%02d", hD, mD));
                selectedCarnet.setHeureFinStr(String.format("%02d:%02d", hF, mF));
                selectedCarnet.setDuree(dureeTotale + " min");
                selectedCarnet.setLieu(lieu);
                selectedCarnet.setMatiere(matiere);
                selectedCarnet.setTypeActivite(typeActivite);
                selectedCarnet.setDifficulte(difficulte);
                selectedCarnet.setConcentration(concentration);
                selectedCarnet.setAgitation(agitation);
                selectedCarnet.setAutonomie(autonomie);
                selectedCarnet.setInterruptions(interruptions);
                selectedCarnet.setTempsPerte(tempsPerte);
                selectedCarnet.setTravailleSeul(travailleSeulCB.isSelected());
                selectedCarnet.setDemandeAide(demandeAideCB.isSelected());
                selectedCarnet.setDifficultes(difficultesTA.getText());
                selectedCarnet.setPointsPositifs(pointsPositifsTA.getText());
                carnetsTable.refresh();
                showAlert("Succès", "Carnet modifié avec succès !", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "La modification a échoué.", Alert.AlertType.ERROR);
            }
        } else {
            c.setTravailTermine(true);
            try { carnetService.ajouter(c); success = true; }
            catch (Exception e) { e.printStackTrace(); }
            if (success) {
                rechargerCarnets();
                showAlert("Succès", "Carnet créé avec succès !", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "La création a échoué.", Alert.AlertType.ERROR);
            }
        }

        toggleCarnetForm(false);
        updateCarnetBadge();
        updateStats();
    }

    private void confirmDeleteCarnet(CarnetRow row) {
        buildAlert(Alert.AlertType.CONFIRMATION, "Supprimer le Carnet",
                "Supprimer le carnet #" + row.getId() + " (" + row.getTypeActivite() + ") ?\nAction irréversible.")
                .showAndWait().ifPresent(resp -> {
                    if (resp == ButtonType.OK) {
                        try {
                            carnetService.supprimer(row.getId());
                            allCarnets.remove(row);
                            updateCarnetBadge();
                            updateStats();
                            showAlert("Supprimé", "Carnet supprimé.", Alert.AlertType.INFORMATION);
                        } catch (Exception e) {
                            showAlert("Erreur", "La suppression a échoué.", Alert.AlertType.ERROR);
                        }
                    }
                });
    }

    private void rechargerCarnets() {
        allCarnets.clear();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (CarnetEducatif c : carnetService.getAll()) {
            allCarnets.add(carnetToRow(c, fmt));
        }
    }

    private CarnetRow carnetToRow(CarnetEducatif c, DateTimeFormatter fmt) {
        LocalDate date = c.getDateEtude().toLocalDate();
        return new CarnetRow(
                c.getId(), date, date.format(fmt),
                c.getHeureDebut().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                c.getHeureFin().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                c.getDureeTotale() + " min",
                c.getLieu(), c.getMatiere(), c.getTypeActivite(), c.getNiveauDifficulte(),
                c.getNiveauConcentration(), c.getNiveauAgitation(), c.getNiveauAutonomie(),
                c.getNombreInterruptions(), c.getTempsAvantPerteConcentration(),
                c.isTravailleSeul(), c.isDemandeAide(),
                c.getDifficultes(), c.getPointsPositifs()
        );
    }

    // =========================================================
    //  COMMENTAIRES
    // =========================================================
    private void initCommentaires() {
        commStatutFilter.setItems(FXCollections.observableArrayList("Tous", "Normal", "Signalé", "Supprimé"));
        commStatutFilter.getSelectionModel().selectFirst();

        colCommId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCommDate.setCellValueFactory(new PropertyValueFactory<>("dateStr"));
        colCommUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        colCommCarnet.setCellValueFactory(new PropertyValueFactory<>("carnetRef"));
        colCommContenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colCommStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        buildCommentActionsColumn();

        colCommStatut.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                if ("Signalé".equals(item))
                    setStyle("-fx-text-fill:#e74c3c; -fx-font-weight:bold;");
                else if ("Supprimé".equals(item))
                    setStyle("-fx-text-fill:#95a5a6; -fx-font-style:italic;");
                else
                    setStyle("-fx-text-fill:#27ae60;");
            }
        });

        filteredComments = new FilteredList<>(allComments, p -> true);
        commentsTable.setItems(filteredComments);
        updateCommBadge();
    }

    private void buildCommentActionsColumn() {
        colCommActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnView = new Button("👁️");
            private final Button btnEdit = new Button("✏️");
            private final Button btnDel  = new Button("🗑️");
            private final HBox box = new HBox(5, btnView, btnEdit, btnDel);
            {
                box.setAlignment(Pos.CENTER);
                btnView.getStyleClass().add("action-icon-view");
                btnEdit.getStyleClass().add("action-icon-edit");
                btnDel .getStyleClass().add("action-icon-del");
                btnView.setTooltip(new Tooltip("Consulter"));
                btnEdit.setTooltip(new Tooltip("Modifier"));
                btnDel .setTooltip(new Tooltip("Supprimer"));
                btnView.setOnAction(e -> consulterCommentaire(getTableView().getItems().get(getIndex())));
                btnEdit.setOnAction(e -> openEditCommentDialog(getTableView().getItems().get(getIndex())));
                btnDel .setOnAction(e -> confirmDeleteComment(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void consulterCommentaire(CommentRow row) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Détail du commentaire");
        dialog.setHeaderText("Commentaire #" + row.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("👤 Utilisateur : ").append(row.getUtilisateur()).append("\n");
        sb.append("📅 Date : ").append(row.getDateStr()).append("\n");
        sb.append("📘 Carnet associé : ").append(row.getCarnetRef()).append("\n");
        sb.append("📝 Contenu :\n").append(row.getContenu()).append("\n");
        sb.append("🏷️ Statut : ").append(row.getStatut());
        dialog.setContentText(sb.toString());
        dialog.showAndWait();
    }

    private void openEditCommentDialog(CommentRow row) {
        TextInputDialog dialog = new TextInputDialog(row.getContenu());
        dialog.setTitle("Modifier le commentaire");
        dialog.setHeaderText("Commentaire de " + row.getUtilisateur());
        dialog.setContentText("Nouveau contenu :");
        dialog.showAndWait().ifPresent(newText -> {
            if (!newText.trim().isEmpty()) {
                try {
                    Commentaire comment = new Commentaire();
                    comment.setId(row.getId());
                    comment.setDateCommentaire(Date.valueOf(row.getDate()));
                    comment.setTexteCommentaire(newText);
                    comment.setCarnetId(row.getCarnetId());
                    comment.setTypeCommentaire(row.getStatut());
                    commentaireService.modifier(comment);
                    row.setContenu(newText);
                    commentsTable.refresh();
                    showAlert("Succès", "Commentaire modifié.", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Erreur", "La modification a échoué.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML public void onCommSearch() { applyCommFilters(); }

    @FXML
    public void applyCommFilters() {
        String txt   = commSearchField.getText().toLowerCase().trim();
        LocalDate d1 = commDateDebut.getValue();
        LocalDate d2 = commDateFin.getValue();
        String statut = commStatutFilter.getValue();
        filteredComments.setPredicate(row -> {
            boolean okText = txt.isEmpty() ||
                    row.getUtilisateur().toLowerCase().contains(txt) ||
                    row.getContenu().toLowerCase().contains(txt);
            LocalDate d = row.getDate();
            boolean okD1 = d1 == null || !d.isBefore(d1);
            boolean okD2 = d2 == null || !d.isAfter(d2);
            boolean okStatut = statut == null || statut.equals("Tous") ||
                    row.getStatut().equalsIgnoreCase(statut);
            return okText && okD1 && okD2 && okStatut;
        });
        updateCommBadge();
    }

    @FXML
    public void resetCommFilters() {
        commSearchField.clear();
        commDateDebut.setValue(null);
        commDateFin.setValue(null);
        commStatutFilter.getSelectionModel().selectFirst();
        filteredComments.setPredicate(p -> true);
        updateCommBadge();
    }

    private void updateCommBadge() {
        int n = filteredComments.size();
        long sig = allComments.stream().filter(c -> "Signalé".equals(c.getStatut())).count();
        commCountBadge.setText(n + " résultat" + (n > 1 ? "s" : ""));
        commInfoLabel.setText(sig > 0 ? sig + " signalé(s)" : "");
    }

    private void confirmDeleteComment(CommentRow row) {
        String apercu = row.getContenu().length() > 60
                ? row.getContenu().substring(0, 60) + "..." : row.getContenu();
        buildAlert(Alert.AlertType.CONFIRMATION, "Supprimer le Commentaire",
                "Supprimer le commentaire de " + row.getUtilisateur() + " ?\n\"" + apercu + "\"")
                .showAndWait().ifPresent(resp -> {
                    if (resp == ButtonType.OK) {
                        try {
                            commentaireService.supprimer(row.getId());
                            allComments.remove(row);
                            if (commDetailPane != null) {
                                commDetailPane.setVisible(false);
                                commDetailPane.setManaged(false);
                            }
                            updateCommBadge();
                            updateStats();
                            showAlert("Supprimé", "Commentaire supprimé.", Alert.AlertType.INFORMATION);
                        } catch (Exception e) {
                            showAlert("Erreur", "La suppression a échoué.", Alert.AlertType.ERROR);
                        }
                    }
                });
    }

    // =========================================================
    //  STATISTIQUES
    // =========================================================
    @FXML public void refreshStats() { updateStats(); }

    private void updateStats() {
        kpiTotalCarnets.setText(String.valueOf(allCarnets.size()));
        kpiTotalComm.setText(String.valueOf(allComments.size()));
        long users = allCarnets.stream().map(CarnetRow::getLieu).distinct().count();
        kpiTotalUsers.setText(String.valueOf(users));
       // long sig = allComments.stream().filter(c -> "Signalé".equals(c.getStatut())).count();
       // kpiSignalements.setText(String.valueOf(sig));
    }

    // =========================================================
    //  CHARGEMENT DONNÉES
    // =========================================================
    private void chargerDonneesDepuisBase() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        allCarnets.clear();
        for (CarnetEducatif c : carnetService.getAll()) {
            allCarnets.add(carnetToRow(c, fmt));
        }
        allComments.clear();
        for (Commentaire cm : commentaireService.getAll()) {
            String statut = cm.getTypeCommentaire() != null ? cm.getTypeCommentaire() : "Normal";
            allComments.add(new CommentRow(
                    cm.getId(),
                    cm.getDateCommentaire().toLocalDate(),
                    cm.getDateCommentaire().toLocalDate().format(fmt),
                    "Utilisateur " + cm.getCarnetId(),
                    "Carnet #" + cm.getCarnetId(),
                    cm.getTexteCommentaire(),
                    statut,
                    cm.getCarnetId()
            ));
        }
    }

    // =========================================================
    //  UTILITAIRES
    // =========================================================
    private void showAlert(String titre, String msg, Alert.AlertType type) {
        buildAlert(type, titre, msg).showAndWait();
    }

    private Alert buildAlert(Alert.AlertType type, String titre, String msg) {
        Alert a = new Alert(type);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a;
    }

    // =========================================================
    //  CLASSES INTERNES
    // =========================================================
    public static class CarnetRow {
        private int id;
        private LocalDate date;
        private String dateStr, heureDebutStr, heureFinStr, duree;
        private String lieu, matiere, typeActivite, difficulte;
        private int concentration, agitation, autonomie, interruptions, tempsPerte;
        private boolean travailleSeul, demandeAide;
        private String difficultes, pointsPositifs;

        public CarnetRow(int id, LocalDate date, String dateStr,
                         String heureDebutStr, String heureFinStr, String duree,
                         String lieu, String matiere, String typeActivite, String difficulte,
                         int concentration, int agitation, int autonomie,
                         int interruptions, int tempsPerte,
                         boolean travailleSeul, boolean demandeAide,
                         String difficultes, String pointsPositifs) {
            this.id = id; this.date = date; this.dateStr = dateStr;
            this.heureDebutStr = heureDebutStr; this.heureFinStr = heureFinStr;
            this.duree = duree; this.lieu = lieu; this.matiere = matiere;
            this.typeActivite = typeActivite; this.difficulte = difficulte;
            this.concentration = concentration; this.agitation = agitation;
            this.autonomie = autonomie; this.interruptions = interruptions;
            this.tempsPerte = tempsPerte; this.travailleSeul = travailleSeul;
            this.demandeAide = demandeAide; this.difficultes = difficultes;
            this.pointsPositifs = pointsPositifs;
        }

        public int getId() { return id; }
        public LocalDate getDate() { return date; }
        public String getDateStr() { return dateStr; }
        public String getHeureDebutStr() { return heureDebutStr; }
        public String getHeureFinStr() { return heureFinStr; }
        public String getDuree() { return duree; }
        public String getLieu() { return lieu; }
        public String getMatiere() { return matiere; }
        public String getTypeActivite() { return typeActivite; }
        public String getDifficulte() { return difficulte; }
        public int getConcentration() { return concentration; }
        public int getAgitation() { return agitation; }
        public int getAutonomie() { return autonomie; }
        public int getInterruptions() { return interruptions; }
        public int getTempsPerte() { return tempsPerte; }
        public boolean isTravailleSeul() { return travailleSeul; }
        public boolean isDemandeAide() { return demandeAide; }
        public String getDifficultes() { return difficultes; }
        public String getPointsPositifs() { return pointsPositifs; }

        public void setDate(LocalDate v)        { date = v; }
        public void setDateStr(String v)        { dateStr = v; }
        public void setHeureDebutStr(String v)  { heureDebutStr = v; }
        public void setHeureFinStr(String v)    { heureFinStr = v; }
        public void setDuree(String v)          { duree = v; }
        public void setLieu(String v)           { lieu = v; }
        public void setMatiere(String v)        { matiere = v; }
        public void setTypeActivite(String v)   { typeActivite = v; }
        public void setDifficulte(String v)     { difficulte = v; }
        public void setConcentration(int v)     { concentration = v; }
        public void setAgitation(int v)         { agitation = v; }
        public void setAutonomie(int v)         { autonomie = v; }
        public void setInterruptions(int v)     { interruptions = v; }
        public void setTempsPerte(int v)        { tempsPerte = v; }
        public void setTravailleSeul(boolean v) { travailleSeul = v; }
        public void setDemandeAide(boolean v)   { demandeAide = v; }
        public void setDifficultes(String v)    { difficultes = v; }
        public void setPointsPositifs(String v) { pointsPositifs = v; }
    }

    public static class CommentRow {
        private int id, carnetId;
        private LocalDate date;
        private String dateStr, utilisateur, carnetRef, contenu, statut;

        public CommentRow(int id, LocalDate date, String dateStr, String utilisateur,
                          String carnetRef, String contenu, String statut, int carnetId) {
            this.id = id; this.date = date; this.dateStr = dateStr;
            this.utilisateur = utilisateur; this.carnetRef = carnetRef;
            this.contenu = contenu; this.statut = statut; this.carnetId = carnetId;
        }

        public int getId() { return id; }
        public LocalDate getDate() { return date; }
        public String getDateStr() { return dateStr; }
        public String getUtilisateur() { return utilisateur; }
        public String getCarnetRef() { return carnetRef; }
        public String getContenu() { return contenu; }
        public String getStatut() { return statut; }
        public int getCarnetId() { return carnetId; }
        public void setStatut(String v) { statut = v; }
        public void setContenu(String v) { contenu = v; }
    }
 // =========================================================
//  RAFRAÎCHISSEMENT GLOBAL
// =========================================================
    @FXML
    public void refreshAll() {
        // 1. Fermer le formulaire Carnet s'il est ouvert
        if (carnetFormPane != null && carnetFormPane.isVisible()) {
            hideCarnetForm(); // cette méthode existe déjà
        }

        // 2. Réinitialiser tous les filtres de la vue Carnets
        carnetsSearchField.clear();
        carnetsDateDebut.setValue(null);
        carnetsDateFin.setValue(null);
        carnetsTypeActivite.getSelectionModel().selectFirst(); // "Tous"
        resetCarnetFilters(); // cette méthode existe déjà et remet la liste complète

        // 3. Réinitialiser tous les filtres de la vue Commentaires
        commSearchField.clear();
        commDateDebut.setValue(null);
        commDateFin.setValue(null);
        commStatutFilter.getSelectionModel().selectFirst(); // "Tous"
        resetCommFilters(); // cette méthode existe déjà

        // 4. Optionnel : forcer l'affichage sur la vue Carnets
        //    (si vous voulez revenir systématiquement aux carnets)
        // showCarnets();

        // 5. Notification légère (facultative)
        showAlert("Info", "Interface réinitialisée", Alert.AlertType.INFORMATION);
    }
}