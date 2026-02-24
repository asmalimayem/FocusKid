package tn.edu.esprit.services;

import org.junit.jupiter.api.*;
import tn.edu.esprit.entities.CarnetEducatif;
import tn.edu.esprit.services.ServiceCarnetEducatif;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceCarnetEducatifTest {

    static ServiceCarnetEducatif service;
    static int idCarnetTest;

    @BeforeAll
    static void setup() {
        service = new ServiceCarnetEducatif();
    }

    @Test
    @Order(1)
    void testAjouterCarnet() {

        CarnetEducatif c = new CarnetEducatif();
        c.setDateEtude(Date.valueOf("2026-02-22"));
        c.setHeureDebut(Time.valueOf("10:00:00"));
        c.setHeureFin(Time.valueOf("11:00:00"));
        c.setDureeTotale(60);
        c.setLieu("Maison");
        c.setMatiere("MathTest");
        c.setTypeActivite("Exercice");
        c.setNiveauDifficulte("Moyen");
        c.setNiveauConcentration(4);
        c.setNiveauAgitation(2);
        c.setNombreInterruptions(1);
        c.setTempsAvantPerteConcentration(30);
        c.setTravailleSeul(true);
        c.setDemandeAide(false);
        c.setNiveauAutonomie(4);
        c.setTravailTermine(true);
        c.setDifficultes("Test difficulté");
        c.setPointsPositifs("Test positif");

        service.ajouter(c);

        List<CarnetEducatif> list = service.getAll();

        assertFalse(list.isEmpty());

        CarnetEducatif last = list.get(list.size() - 1);
        idCarnetTest = last.getId();

        assertEquals("MathTest", last.getMatiere());
    }

    @Test
    @Order(2)
    void testModifierCarnet() {

        CarnetEducatif c = new CarnetEducatif();
        c.setId(idCarnetTest);
        c.setMatiere("MathModifie");
        c.setTypeActivite("Lecture");
        c.setNiveauDifficulte("Difficile");
        c.setDifficultes("Modif");
        c.setPointsPositifs("Modif positif");

        service.modifier(c);

        List<CarnetEducatif> list = service.getAll();

        boolean existe = list.stream()
                .anyMatch(x -> x.getMatiere().equals("MathModifie"));

        assertTrue(existe);
    }

    @Test
    @Order(3)
    void testSupprimerCarnet() {

        service.supprimer(idCarnetTest);

        List<CarnetEducatif> list = service.getAll();

        boolean existe = list.stream()
                .anyMatch(x -> x.getId() == idCarnetTest);

        assertFalse(existe);
    }
}