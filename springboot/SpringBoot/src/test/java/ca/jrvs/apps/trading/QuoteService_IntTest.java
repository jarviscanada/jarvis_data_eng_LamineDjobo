package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import ca.jrvs.apps.trading.repository.QuoteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:schema.sql"})  // Charger le script pour recréer les tables avant les tests
class QuoteServiceIntTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;

    @BeforeEach
    public void setup() {
        quoteDao.deleteAll();  // Supprimer toutes les entrées pour partir d'une base propre
        Quote q1 = new Quote("AAPL", 150.0, 100, 149.5, 100, 151.0);
        Quote q2 = new Quote("MSFT", 250.0, 100, 249.5, 100, 251.0);
        quoteDao.save(q1);
        quoteDao.save(q2);
    }

    @Test
    public void findIexQuoteByTicker() {
        String ticker = "AAPL";
        Optional<Quote> foundQuote = quoteDao.findById(ticker);

        assertTrue(foundQuote.isPresent(), "La cotation doit exister pour ce ticker");
        assertEquals(ticker, foundQuote.get().getTicker(), "Le ticker retourné doit correspondre à la recherche");
        assertEquals(150.0, foundQuote.get().getAskPrice(), "Le prix de l'offre doit correspondre à celui sauvegardé");
    }

    @Test
    public void updateMarketData() {
        Quote updatedQuote = new Quote("AAPL", 160.0, 200, 159.5, 200, 161.0);
        quoteService.saveQuoteEntity(updatedQuote);

        Optional<Quote> retrievedQuote = quoteDao.findById("AAPL");
        assertTrue(retrievedQuote.isPresent(), "La cotation mise à jour doit être trouvée");
        assertEquals(160.0, retrievedQuote.get().getAskPrice(), "Le prix de l'offre doit être mis à jour");
    }

    @Test
    public void saveQuotes() {
        List<String> tickers = List.of("GOOGL", "AMZN");
        quoteService.saveQuotes(tickers);

        List<Quote> savedQuotes = quoteDao.findAll();
        assertTrue(savedQuotes.size() >= 4, "Il doit y avoir au moins 4 cotations après ajout des nouvelles");
        assertTrue(savedQuotes.stream().anyMatch(quote -> "GOOGL".equals(quote.getTicker())), "La cotation pour GOOGL doit exister");
        assertTrue(savedQuotes.stream().anyMatch(quote -> "AMZN".equals(quote.getTicker())), "La cotation pour AMZN doit exister");
    }

    @Test
    public void saveQuote() {
        Quote newQuote = new Quote("NFLX", 500.0, 50, 495.0, 50, 505.0);
        quoteService.saveQuoteEntity(newQuote);

        Optional<Quote> savedQuote = quoteDao.findById("NFLX");
        assertTrue(savedQuote.isPresent(), "La cotation pour NFLX doit être sauvegardée");
        assertEquals(500.0, savedQuote.get().getAskPrice(), "Le prix de l'offre pour NFLX doit correspondre à celui sauvegardé");
    }

    @Test
    public void findAllQuotes() {
        List<Quote> allQuotes = quoteDao.findAll();
        assertEquals(2, allQuotes.size(), "Il doit y avoir 2 cotations dans la base initiale");
    }
}

