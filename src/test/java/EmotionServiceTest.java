import com.projet.focuskid.entities.Emotion;
import org.junit.jupiter.api.*;
import com.projet.focuskid.services.ServiceEmotion;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmotionServiceTest {

    static ServiceEmotion se;
    static int idEmotion;

    @BeforeAll
    static void setUp() {
        se = new ServiceEmotion();
    }

    @Test
    @Order(1)
    void testAjouterEmotion() throws SQLException {
        Emotion emotion = new Emotion("Joie", "joie.png");
        se.ajouter(emotion);

        List<Emotion> emotions = se.recuperer();
        assertTrue(emotions.stream()
                .anyMatch(e -> e.getNom().equals("Joie") && e.getIcone().equals("joie.png")));

        idEmotion = emotions.get(emotions.size() - 1).getId();
        System.out.println("ID Emotion ajouté : " + idEmotion);
    }

    @Test
    @Order(2)
    void testModifierEmotion() throws SQLException {
        Emotion emotion = new Emotion();
        emotion.setId(idEmotion);
        emotion.setNom("Tristesse");
        emotion.setIcone("tristesse.png");

        se.modifier(emotion);

        List<Emotion> emotions = se.recuperer();
        assertTrue(emotions.stream()
                .anyMatch(e -> e.getId() == idEmotion &&
                        e.getNom().equals("Tristesse") &&
                        e.getIcone().equals("tristesse.png")));
    }

    @Test
    @Order(3)
    void testSupprimerEmotion() throws SQLException {
        Emotion emotion = new Emotion();
        emotion.setId(idEmotion);

        se.supprimer(emotion);

        List<Emotion> emotions = se.recuperer();
        assertFalse(emotions.stream()
                .anyMatch(e -> e.getId() == idEmotion));
    }
}