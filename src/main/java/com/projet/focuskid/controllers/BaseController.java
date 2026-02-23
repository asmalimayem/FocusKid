package com.projet.focuskid.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public abstract class BaseController {

    @FXML
    protected Button btnAdd;
    @FXML
    protected Button btnList;
    @FXML
    protected Button btnRefresh;
    @FXML
    protected Button btnDelete;

    @FXML
    public void initialize() {
        System.out.println("BaseController.initialize() appelé");
        System.out.println("btnAdd = " + (btnAdd != null ? "✅" : "❌"));
        System.out.println("btnList = " + (btnList != null ? "✅" : "❌"));
        System.out.println("btnRefresh = " + (btnRefresh != null ? "✅" : "❌"));
        System.out.println("btnDelete = " + (btnDelete != null ? "✅" : "❌"));

        setupNavigation();
    }

    protected void setupNavigation() {
        System.out.println("BaseController.setupNavigation() appelé");

        if (btnAdd != null) {
            btnAdd.setOnAction(e -> {
                System.out.println("🟢 Clic sur btnAdd");
                navigateTo("addSession.fxml");
            });
        } else {
            System.err.println("btnAdd est NULL !");
        }

        if (btnList != null) {
            btnList.setOnAction(e -> {
                System.out.println("📋 Clic sur btnList");
                navigateTo("listSessions.fxml");
            });
        } else {
            System.err.println("btnList est NULL !");
        }

        if (btnRefresh != null) {
            btnRefresh.setOnAction(e -> {
                System.out.println("🔄 Clic sur btnRefresh");
                navigateTo("updateSession.fxml");
            });
        } else {
            System.err.println("btnRefresh est NULL !");
        }

        if (btnDelete != null) {
            btnDelete.setOnAction(e -> {
                System.out.println("🗑️ Clic sur btnDelete");
                navigateTo("deleteSession.fxml");
            });
        } else {
            System.err.println("btnDelete est NULL !");
        }
    }

    protected void navigateTo(String fxmlFileName) {
        try {
            String fullPath = "/com/projet/focuskid/" + fxmlFileName;
            System.out.println("Tentative de chargement : " + fullPath);

            URL resourceUrl = getClass().getResource(fullPath);
            if (resourceUrl == null) {
                throw new IOException("Fichier non trouvé : " + fullPath);
            }

            System.out.println("Fichier trouvé : " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();

            // Récupérer la scène à partir d'un bouton qui devrait exister
            Stage stage = null;
            if (btnAdd != null) {
                stage = (Stage) btnAdd.getScene().getWindow();
            } else if (btnList != null) {
                stage = (Stage) btnList.getScene().getWindow();
            } else {
                // Fallback: utiliser n'importe quel bouton
                stage = (Stage) btnDelete.getScene().getWindow();
            }

            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                System.err.println("Impossible de trouver la stage !");
            }

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur de navigation",
                    "Impossible de charger : " + fxmlFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected abstract void showMessage(String message, boolean success);
}