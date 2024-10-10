package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

import static org.junit.Assert.*;

public class QuoteService_IntTest {

    private QuoteService quoteService;
    private QuoteDao quoteDao;
    private QuoteHttpHelper httpHelper;
    private Connection connection;

    @Before
    public void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "password");
        quoteDao = new QuoteDao(connection);
        httpHelper = new QuoteHttpHelper();
        quoteService = new QuoteService(quoteDao, httpHelper);
    }

    @Test
    public void testFetchQuoteDataFromAPI() {
        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("AAPL");
        assertTrue(result.isPresent());
        assertEquals("AAPL", result.get().getTicker());
    }
}
