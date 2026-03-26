package tn.edu.esprit.services;

import org.junit.jupiter.api.*;
import tn.edu.esprit.entities.Commentaire;
import tn.edu.esprit.services.ServiceCommentaire;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceCommentaireTest {

    static ServiceCommentaire service;
    static int idCommentaireTest;

    @BeforeAll
    static void setup() {
        service = new ServiceCommentaire();
    }

    @Test
    @Order(1)
    void testAjouterCommentaire() {

        Commentaire c = new Commentaire();
        c.setCarnetId(1); // ⚠ assure-toi que carnet 1 existe
        c.setDateCommentaire(Date.valueOf("2026-02-22"));
        c.setTexteCommentaire("Test commentaire");
        c.setTypeCommentaire("Observation");

        service.ajouter(c);

        List<Commentaire> list = service.getAll();

        assertFalse(list.isEmpty());

        Commentaire last = list.get(list.size() - 1);
        idCommentaireTest = last.getId();

        assertEquals("Test commentaire", last.getTexteCommentaire());
    }

    @Test
    @Order(2)
    void testModifierCommentaire() {

        Commentaire c = new Commentaire();
        c.setId(idCommentaireTest);
        c.setTexteCommentaire("Commentaire modifié");
        c.setTypeCommentaire("Suggestion");
        c.setDateCommentaire(Date.valueOf("2026-02-22"));

        service.modifier(c);

        List<Commentaire> list = service.getAll();

        boolean existe = list.stream()
                .anyMatch(x -> x.getTexteCommentaire().equals("Commentaire modifié"));

        assertTrue(existe);
    }

    @Test
    @Order(3)
    void testSupprimerCommentaire() {

        service.supprimer(idCommentaireTest);

        List<Commentaire> list = service.getAll();

        boolean existe = list.stream()
                .anyMatch(x -> x.getId() == idCommentaireTest);

        assertFalse(existe);
    }
}