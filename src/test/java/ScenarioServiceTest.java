import com.projet.focuskid.entities.Scenario;
import org.junit.jupiter.api.*;
import com.projet.focuskid.services.ServiceScenario;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScenarioServiceTest {

    static ServiceScenario ss;
    static int idScenario;

    @BeforeAll
    static void setUp() {
        ss = new ServiceScenario();
    }

    @Test
    @Order(1)
    void testAjouterScenario() throws SQLException {
        // ⚠️ Assure-toi que l'ID d'emotion 1 existe dans la BDD
        Scenario scenario = new Scenario("Titre test", "Description test", "anim.mp4", 1);
        ss.ajouter(scenario);

        List<Scenario> scenarios = ss.recuperer();
        assertTrue(scenarios.stream()
                .anyMatch(s -> s.getTitre().equals("Titre test") &&
                        s.getDescription().equals("Description test") &&
                        s.getAnimationUrl().equals("anim.mp4") &&
                        s.getEmotionId() == 1));

        idScenario = scenarios.get(scenarios.size() - 1).getId();
        System.out.println("ID Scenario ajouté : " + idScenario);
    }

    @Test
    @Order(2)
    void testModifierScenario() throws SQLException {
        Scenario scenario = new Scenario();
        scenario.setId(idScenario);
        scenario.setTitre("Titre modifié");
        scenario.setDescription("Description modifiée");
        scenario.setAnimationUrl("anim2.mp4");
        scenario.setEmotionId(1);

        ss.modifier(scenario);

        List<Scenario> scenarios = ss.recuperer();
        assertTrue(scenarios.stream()
                .anyMatch(s -> s.getId() == idScenario &&
                        s.getTitre().equals("Titre modifié") &&
                        s.getDescription().equals("Description modifiée") &&
                        s.getAnimationUrl().equals("anim2.mp4") &&
                        s.getEmotionId() == 1));
    }

    @Test
    @Order(3)
    void testSupprimerScenario() throws SQLException {
        Scenario scenario = new Scenario();
        scenario.setId(idScenario);

        ss.supprimer(scenario);

        List<Scenario> scenarios = ss.recuperer();
        assertFalse(scenarios.stream()
                .anyMatch(s -> s.getId() == idScenario));
    }
}