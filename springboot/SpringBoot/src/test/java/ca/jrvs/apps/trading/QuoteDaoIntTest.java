package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.repository.QuoteDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradingApplication.class)
public class QuoteDaoIntTest {

    @Autowired
    private QuoteDao quoteDao;

    private Quote testQuote;



    @BeforeEach
    public void setup() {
        // Initialisation d'un objet Quote de test
        testQuote = new Quote();
        testQuote.setTicker("AAPL");
        testQuote.setLastPrice(150.00);
        testQuote.setAskPrice(151.00);
        testQuote.setBidPrice(149.00);
        testQuote.setAskSize(10);
        testQuote.setBidSize(20);
    }

    /**
     * Test pour la méthode save()
     */
    @Test
    public void testSaveQuote() {
        Quote savedQuote = quoteDao.save(testQuote);
        assertNotNull(savedQuote, "Le quote sauvegardé ne doit pas être null");
        assertEquals("AAPL", savedQuote.getTicker(), "Le ticker doit correspondre");
    }

    /**
     * Test pour findById()
     */
    @Test
    public void testFindById() {
        quoteDao.save(testQuote);
        Optional<Quote> retrievedQuote = quoteDao.findById("AAPL");
        assertTrue(retrievedQuote.isPresent(), "Le quote doit exister");
        assertEquals(150.00, retrievedQuote.get().getLastPrice(), "Le prix doit correspondre");
    }

    /**
     * Test pour findAll()
     */
    @Test
    public void testFindAll() {
        quoteDao.save(testQuote);
        Quote anotherQuote = new Quote();
        anotherQuote.setTicker("MSFT");
        anotherQuote.setLastPrice(280.00);
        anotherQuote.setAskPrice(282.00);
        anotherQuote.setBidPrice(278.00);
        anotherQuote.setAskSize(15);
        anotherQuote.setBidSize(25);
        quoteDao.save(anotherQuote);

        List<Quote> quotes = quoteDao.findAll();
        assertEquals(2, quotes.size(), "Il doit y avoir 2 quotes");
    }

    /**
     * Test pour deleteById()
     */
    @Test
    public void testDeleteById() {
        quoteDao.save(testQuote);
        quoteDao.deleteById("AAPL");
        Optional<Quote> deletedQuote = quoteDao.findById("AAPL");
        assertFalse(deletedQuote.isPresent(), "Le quote doit être supprimé");
    }

    /**
     * Test pour count()
     */
    @Test
    public void testCount() {
        quoteDao.save(testQuote);
        long count = quoteDao.count();
        assertEquals(1, count, "Le nombre de quotes doit être 1");
    }

    /**
     * Test pour deleteAll()
     */
    @Test
    public void testDeleteAll() {
        quoteDao.save(testQuote);
        quoteDao.deleteAll();
        assertEquals(0, quoteDao.count(), "Le nombre de quotes doit être 0 après suppression");
    }

    @AfterEach
    public void tearDown() {
        quoteDao.deleteAll(); // Nettoyer à la fin du test pour laisser la table vide pour les prochains tests
    }
}

