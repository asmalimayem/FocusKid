package com.projet.focuskid.services;
import com.projet.focuskid.entities.SessionDeCalme;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceSessionDeCalmeTest {

    static ServiceSessionDeCalme service;
    static int idSessionTest; // pour stocker l'ID de la session ajoutée

    @BeforeAll
    static void setup() {
        service = new ServiceSessionDeCalme();
    }

    @Test
    @Order(1)
    void testAjouterSession() throws SQLException {
        SessionDeCalme session = new SessionDeCalme();
        session.setEnfantId(1);
        session.setTypeActivite("respiration");
        session.setDeclencheur("parent");
        session.setDureePrevue(10);
        session.setDureeReelle(8);
        session.setFeedbackEnfant(4);
        session.setNoteParent("Bien");

        service.ajouter(session);

        List<SessionDeCalme> sessions = service.getAll(null);
        assertFalse(sessions.isEmpty());

        // Récupérer l'ID de la dernière session ajoutée pour les tests suivants
        SessionDeCalme derniere = sessions.get(sessions.size() - 1);
        idSessionTest = derniere.getId();

        assertTrue(
                sessions.stream().anyMatch(s ->
                        s.getTypeActivite().equals("respiration") &&
                                s.getDeclencheur1().equals("parent")
                )
        );
    }

    @Test
    @Order(2)
    void testModifierSession() throws SQLException {
        assertTrue(idSessionTest > 0, "ID de session invalide");

        SessionDeCalme session = new SessionDeCalme();
        session.setId(idSessionTest);
        session.setTypeActivite("yoga");
        session.setDureeReelle(12);
        session.setFeedbackEnfant(5);
        session.setNoteParent("Très bien");

        service.modifier(session);

        List<SessionDeCalme> sessions = service.getAll(null);
        boolean trouve = sessions.stream()
                .anyMatch(s -> s.getId() == idSessionTest &&
                        s.getTypeActivite().equals("yoga") &&
                        s.getNoteParent().equals("Très bien"));

        assertTrue(trouve);
    }

    @Test
    @Order(3)
    void testSupprimerSession() throws SQLException {
        assertTrue(idSessionTest > 0, "ID de session invalide");

        service.supprimer(idSessionTest);

        List<SessionDeCalme> sessions = service.getAll(null);
        boolean existe = sessions.stream().anyMatch(s -> s.getId() == idSessionTest);

        assertFalse(existe);
    }


    @AfterEach
    void cleanUp() throws SQLException {
        // Ne supprimer que la session créée pendant le test si elle existe encore
        if (idSessionTest > 0) {
            List<SessionDeCalme> sessions = service.getAll(null);
            boolean existe = sessions.stream().anyMatch(s -> s.getId() == idSessionTest);
            if (existe) {
                service.supprimer(idSessionTest);
                System.out.println("Nettoyage : session avec ID " + idSessionTest + " supprimée");
            }
        }
    }
}