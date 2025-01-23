package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.repository.MarketDataDaoImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradingApplication.class)
public class MarketDataDaoImplIntegrationTest {

    @Autowired
    private MarketDataDaoImpl marketDataDaoImpl;

    @BeforeEach
    public void setUp() {
        System.out.println("Début du test...");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Fin du test.");
    }

    /**
     * Test pour `findById` avec un ticker valide.
     */
    @Test
    public void findQuote_validTicker() {
        String ticker = "AAPL";

        Optional<IexQuote> result = marketDataDaoImpl.findById(ticker);

        assertTrue(result.isPresent(), "Le résultat ne doit pas être vide pour un ticker valide.");
        assertEquals(ticker, result.get().getSymbol(), "Le symbole retourné doit correspondre au ticker demandé.");
        assertNotNull(result.get().getLatestPrice(), "Le dernier prix ne doit pas être nul.");
    }

    /**
     * Test pour `findById` avec un ticker invalide.
     */
    @Test
    public void findQuote_invalidTicker() {
        String invalidTicker = "FAKETICKER";

        Optional<IexQuote> result = marketDataDaoImpl.findById(invalidTicker);

        assertFalse(result.isPresent(), "Le résultat doit être vide pour un ticker invalide.");
    }

    /**
     * Test pour `findAllById` avec des tickers valides.
     */
    @Test
    public void findAllQuotes_validTickers() {
        List<String> tickers = List.of("AAPL", "MSFT", "GOOGL");

        List<IexQuote> resultList = marketDataDaoImpl.findAllById(tickers);

        System.out.println("Tickers récupérés : " + resultList.stream().map(IexQuote::getSymbol).toList());
        assertEquals(3, resultList.size(), "La taille de la liste doit être 3 pour ces tickers.");
    }


    /**
     * Test pour `findAllById` avec des tickers valides et invalides.
     */
    @Test
    public void findAllQuotes_mixedTickers() {
        List<String> tickers = List.of("AAPL", "FAKE_TICKER", "MSFT");

        List<IexQuote> resultList = marketDataDaoImpl.findAllById(tickers);

        assertTrue(resultList.size() >= 2, "La liste doit contenir au moins 2 cotations valides.");
        assertTrue(resultList.stream().anyMatch(quote -> "AAPL".equals(quote.getSymbol())),
                "La liste doit contenir une cotation pour 'AAPL'.");
        assertTrue(resultList.stream().anyMatch(quote -> "MSFT".equals(quote.getSymbol())),
                "La liste doit contenir une cotation pour 'MSFT'.");
    }

}
