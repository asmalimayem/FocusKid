package tn.edu.esprit.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import tn.edu.esprit.entities.CarnetEducatif;
import tn.edu.esprit.services.ServiceCarnetEducatif;
import tn.edu.esprit.tools.PdfExporter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphiqueController {

    @FXML private LineChart<String, Number> chartConcentration;
    @FXML private BarChart<String, Number>  chartMatiere;
    @FXML private StackPane                 chartContainer;

    @FXML private Button btnConcentration;
    @FXML private Button btnMatiere;
    @FXML private Button refreshButton;
    @FXML private Button exportButton;
    @FXML private Button statsButton;
    @FXML private Button closeButton;

    private ObservableList<CarnetEducatif> currentData;
    private final ServiceCarnetEducatif service = new ServiceCarnetEducatif();

    /* =========================================================
     *  INITIALIZE
     * ========================================================= */
    @FXML
    public void initialize() {
        animerApparition(refreshButton,    0);
        animerApparition(exportButton,   120);
        animerApparition(statsButton,    240);
        animerApparition(closeButton,    360);
        animerApparition(btnConcentration, 80);
        animerApparition(btnMatiere,      180);

        for (Button b : new Button[]{refreshButton, exportButton, statsButton, closeButton,
                                     btnConcentration, btnMatiere}) {
            configurerHoverAnimation(b);
        }

        fairePulser(refreshButton);
    }

    private void animerApparition(Button btn, int delayMs) {
        btn.setOpacity(0);
        btn.setScaleX(0.75);
        btn.setScaleY(0.75);
        btn.setTranslateY(18);

        FadeTransition      fade  = new FadeTransition(Duration.millis(420), btn);
        ScaleTransition     scale = new ScaleTransition(Duration.millis(420), btn);
        TranslateTransition slide = new TranslateTransition(Duration.millis(420), btn);

        fade.setToValue(1);
        scale.setToX(1); scale.setToY(1);
        scale.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition pt = new ParallelTransition(fade, scale, slide);
        pt.setDelay(Duration.millis(delayMs));
        pt.play();
    }

    private void configurerHoverAnimation(Button btn) {
        ScaleTransition  up     = new ScaleTransition(Duration.millis(180), btn);
        ScaleTransition  down   = new ScaleTransition(Duration.millis(180), btn);
        RotateTransition rotIn  = new RotateTransition(Duration.millis(180), btn);
        RotateTransition rotOut = new RotateTransition(Duration.millis(180), btn);

        up  .setToX(1.09); up  .setToY(1.09);
        down.setToX(1.0);  down.setToY(1.0);
        rotIn .setToAngle(3);
        rotOut.setToAngle(0);

        ParallelTransition hoverIn  = new ParallelTransition(up,   rotIn);
        ParallelTransition hoverOut = new ParallelTransition(down, rotOut);

        btn.setOnMouseEntered(e -> { hoverOut.stop(); hoverIn.playFromStart(); });
        btn.setOnMouseExited (e -> { hoverIn.stop();  hoverOut.playFromStart(); });

        ScaleTransition press   = new ScaleTransition(Duration.millis(90), btn);
        ScaleTransition release = new ScaleTransition(Duration.millis(90), btn);
        press  .setToX(0.93); press  .setToY(0.93);
        release.setToX(1.0);  release.setToY(1.0);

        btn.setOnMousePressed (e -> { press.stop();   press.playFromStart(); });
        btn.setOnMouseReleased(e -> { release.stop(); release.playFromStart(); });
    }

    private void fairePulser(Button btn) {
        ScaleTransition grow   = new ScaleTransition(Duration.millis(700), btn);
        ScaleTransition shrink = new ScaleTransition(Duration.millis(700), btn);
        grow  .setToX(1.06); grow  .setToY(1.06);
        shrink.setToX(1.0);  shrink.setToY(1.0);
        grow  .setInterpolator(Interpolator.EASE_BOTH);
        shrink.setInterpolator(Interpolator.EASE_BOTH);

        SequentialTransition pulse = new SequentialTransition(grow, shrink);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();
    }

    /* =========================================================
     *  TOGGLE graphiques
     * ========================================================= */
    @FXML
    void afficherConcentration() {
        if (chartConcentration.isVisible()) return;
        switcherGraphique(chartMatiere, chartConcentration);
        btnConcentration.getStyleClass().setAll("toggle-btn-active");
        btnMatiere      .getStyleClass().setAll("toggle-btn");
    }

    @FXML
    void afficherMatiere() {
        if (chartMatiere.isVisible()) return;
        switcherGraphique(chartConcentration, chartMatiere);
        btnMatiere      .getStyleClass().setAll("toggle-btn-active");
        btnConcentration.getStyleClass().setAll("toggle-btn");
    }

    private void switcherGraphique(XYChart<?,?> sortant, XYChart<?,?> entrant) {
        FadeTransition      fadeOut  = new FadeTransition(Duration.millis(250), sortant);
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(250), sortant);
        fadeOut .setToValue(0);
        slideOut.setToX(-30);

        ParallelTransition out = new ParallelTransition(fadeOut, slideOut);
        out.setOnFinished(e -> {
            sortant.setVisible(false);
            sortant.setTranslateX(0);
            sortant.setOpacity(1);

            entrant.setOpacity(0);
            entrant.setTranslateX(30);
            entrant.setVisible(true);

            FadeTransition      fadeIn  = new FadeTransition(Duration.millis(280), entrant);
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(280), entrant);
            fadeIn .setToValue(1);
            slideIn.setToX(0);
            slideIn.setInterpolator(Interpolator.EASE_OUT);

            new ParallelTransition(fadeIn, slideIn).play();
        });
        out.play();
    }

    /* =========================================================
     *  DONNÉES
     * ========================================================= */
    public void setData(ObservableList<CarnetEducatif> liste) {
        this.currentData = liste;
        chargerGraphiqueConcentration(liste);
        chargerGraphiqueMatiere(liste);
    }

    private void chargerGraphiqueConcentration(ObservableList<CarnetEducatif> liste) {
        chartConcentration.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Concentration");
        for (CarnetEducatif c : liste)
            serie.getData().add(new XYChart.Data<>(
                c.getDateEtude().toString(), c.getNiveauConcentration()));
        chartConcentration.getData().add(serie);
    }

    private void chargerGraphiqueMatiere(ObservableList<CarnetEducatif> liste) {
        chartMatiere.getData().clear();
        Map<String, Integer> map = new HashMap<>();
        for (CarnetEducatif c : liste)
            map.put(c.getMatiere(),
                    map.getOrDefault(c.getMatiere(), 0) + c.getDureeTotale());

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Durée totale (min)");
        for (String m : map.keySet())
            serie.getData().add(new XYChart.Data<>(m, map.get(m)));
        chartMatiere.getData().add(serie);
}
    @FXML
    void rafraichir() {
        if (currentData != null) {
            chargerGraphiqueConcentration(currentData);
            chargerGraphiqueMatiere(currentData);
        }
    }
    @FXML
    void exporterPDF() {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Exporter PDF");
            fc.setInitialFileName("rapport.pdf");

            File file = fc.showSaveDialog(chartContainer.getScene().getWindow());

            if (file != null) {
                PdfExporter.exporterRapport(new ArrayList<>(currentData), file.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void fermerFenetre() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    void ouvrirStats() {
        System.out.println("Bouton Statistiques cliqué !");
    }

}