package com.projet.focuskid.mains;

import com.projet.focuskid.entities.Emotion;
import com.projet.focuskid.entities.Scenario;
import com.projet.focuskid.services.ServiceEmotion;
import com.projet.focuskid.services.ServiceScenario;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        ServiceEmotion serviceEmotion = new ServiceEmotion();
        ServiceScenario serviceScenario = new ServiceScenario();

        try {

            // Ajouter une Emotion
            Emotion emotion = new Emotion("Joie", "joie.png");
            serviceEmotion.ajouter(emotion);

            // Afficher toutes les emotions
            System.out.println("Liste des Emotions ");
            System.out.println(serviceEmotion.recuperer());

            //  Ajouter un Scenario (attention: emotionId doit exister)
            Scenario scenario = new Scenario(
                    "Félicitation",
                    "Un enfant reçoit un cadeau",
                    "animation1.mp4",
                    1   // ⚠️ id d'une emotion existante
            );

            serviceScenario.ajouter(scenario);

            //  Afficher tous les scenarios
            System.out.println(" Liste des Scenarios ");
            System.out.println(serviceScenario.recuperer());

            //  Modifier Emotion
            Emotion emotionModif = new Emotion(1, "Tristesse", "tristesse.png");
            serviceEmotion.modifier(emotionModif);

            //  Supprimer Scenario
            Scenario scenarioSupp = new Scenario();
            scenarioSupp.setId(1);
            serviceScenario.supprimer(scenarioSupp);

            System.out.println(" Après modifications ");
            System.out.println(serviceEmotion.recuperer());
            System.out.println(serviceScenario.recuperer());

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }
}