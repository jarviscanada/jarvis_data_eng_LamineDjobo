package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.Trader;
import ca.jrvs.apps.trading.repository.TraderDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:schema.sql"})
public class TraderDaoIntTest {

    @Autowired
    private TraderDao traderDao;

    private Trader trader1;
    private Trader trader2;

    @BeforeEach
    public void setup() {
        // Création de deux traders pour les tests
        trader1 = new Trader();
        trader1.setFirstName("John");
        trader1.setLastName("Doe");
        trader1.setDob(LocalDate.of(1985, 5, 15));
        trader1.setCountry("Canada");
        trader1.setEmail("johndoe@example.com");

        trader2 = new Trader();
        trader2.setFirstName("Jane");
        trader2.setLastName("Smith");
        trader2.setDob(LocalDate.of(1990, 8, 22));
        trader2.setCountry("USA");
        trader2.setEmail("janesmith@example.com");

        traderDao.saveAll(List.of(trader1, trader2));  // Insérer les traders
    }

    @AfterEach
    public void teardown() {
        traderDao.deleteAll();  // Nettoyer la base après chaque test
    }

    /**
     * Test de `insertOne()`
     */
    @Test
    public void testInsertOne() {
        Trader trader = new Trader();
        trader.setFirstName("Alice");
        trader.setLastName("Brown");
        trader.setDob(LocalDate.of(1992, 11, 10));
        trader.setCountry("UK");
        trader.setEmail("alicebrown@example.com");

        Trader savedTrader = traderDao.save(trader);
        assertNotNull(savedTrader.getId(), "L'ID du trader doit être généré.");
        assertEquals("Alice", savedTrader.getFirstName());
    }

    /**
     * Test de `findById()`
     */
    @Test
    public void testFindById() {
        Optional<Trader> foundTrader = traderDao.findById(trader1.getId());
        assertTrue(foundTrader.isPresent(), "Le trader doit être trouvé.");
        assertEquals("John", foundTrader.get().getFirstName());

        Optional<Trader> notFoundTrader = traderDao.findById(999);
        assertFalse(notFoundTrader.isPresent(), "Aucun trader ne doit être trouvé pour un ID inexistant.");
    }

    /**
     * Test de `findAll()`
     */
    @Test
    public void testFindAll() {
        List<Trader> traders = traderDao.findAll();
        assertEquals(2, traders.size(), "Il doit y avoir 2 traders dans la base.");
        assertTrue(traders.stream().anyMatch(trader -> "John".equals(trader.getFirstName())));
        assertTrue(traders.stream().anyMatch(trader -> "Jane".equals(trader.getFirstName())));
    }

    /**
     * Test de `deleteById()`
     */
    @Test
    public void testDeleteById() {
        // Delete an existing trader and verify it's deleted
        traderDao.deleteById(trader1.getId());
        Optional<Trader> deletedTrader = traderDao.findById(trader1.getId());
        assertFalse(deletedTrader.isPresent(), "Le trader doit être supprimé.");

        // Delete a non-existent ID and ensure it does not throw an exception
        assertDoesNotThrow(() -> traderDao.deleteById(999), "La suppression d'un ID inexistant ne doit pas lever d'exception.");
    }


    /**
     * Test de `existsById()`
     */
    @Test
    public void testExistsById() {
        assertTrue(traderDao.existsById(trader2.getId()), "Le trader 2 doit exister.");
        assertFalse(traderDao.existsById(999), "Aucun trader ne doit exister avec cet ID inexistant.");
    }

    /**
     * Test de `count()`
     */
    @Test
    public void testCount() {
        long count = traderDao.count();
        assertEquals(2, count, "Le nombre total de traders doit être 2.");
    }

    /**
     * Test de `deleteAll()`
     */
    @Test
    public void testDeleteAll() {
        traderDao.deleteAll();
        long count = traderDao.count();
        assertEquals(0, count, "La table doit être vide après `deleteAll()`.");
    }

    /**
     * Test de `saveAll()`
     */
    @Test
    public void testSaveAll() {
        Trader trader3 = new Trader();
        trader3.setFirstName("Mark");
        trader3.setLastName("Lee");
        trader3.setDob(LocalDate.of(1995, 3, 19));
        trader3.setCountry("Australia");
        trader3.setEmail("marklee@example.com");

        Trader trader4 = new Trader();
        trader4.setFirstName("Sophia");
        trader4.setLastName("White");
        trader4.setDob(LocalDate.of(1988, 12, 5));
        trader4.setCountry("France");
        trader4.setEmail("sophiawhite@example.com");

        traderDao.saveAll(List.of(trader3, trader4));
        List<Trader> traders = traderDao.findAll();
        assertEquals(4, traders.size(), "Il doit y avoir 4 traders après l'ajout.");
    }
}

