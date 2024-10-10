package ca.jrvs.apps.stockquote.dao;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;



public class QuoteDaoTest {

    private Connection connection;
    private QuoteDao quoteDao;

    @Before
    public void setup() throws Exception {
        // Establish a connection to the test database
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "password");
        quoteDao = new QuoteDao(connection);
    }

    @Test
    public void testSaveAndFindQuote() {
        // Create a new quote
        Quote quote = new Quote();
        quote.setTicker("AAPL");
        quote.setOpen(150.0);
        quote.setHigh(155.0);
        quote.setLow(145.0);
        quote.setPrice(152.0);
        quote.setVolume(5000);
        quote.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
        quote.setPreviousClose(150.0);
        quote.setChange(2.0);
        quote.setChangePercent("1.33%");

        // Save the quote in the database
        quoteDao.save(quote);

        // Find the saved quote by ticker
        Optional<Quote> result = quoteDao.findById("AAPL");
        assertTrue(result.isPresent());
        assertEquals("AAPL", result.get().getTicker());
        assertEquals(152.0, result.get().getPrice(), 0);
    }

    @Test
    public void testFindAllQuotes() {
        Iterable<Quote> quotes = quoteDao.findAll();
        assertNotNull("The list of quotes should not be null", quotes);
        assertTrue("The list of quotes should not be empty", quotes.iterator().hasNext());
    }

    @Test
    public void testDeleteQuoteById() {
        String ticker = "AAPL";

        // Delete the quote by ticker
        quoteDao.deleteById(ticker);

        // Try to find the deleted quote
        Optional<Quote> result = quoteDao.findById(ticker);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteAllQuotes() {
        quoteDao.deleteAll();
        Iterable<Quote> quotes = quoteDao.findAll();
        assertFalse(quotes.iterator().hasNext());
    }
}

