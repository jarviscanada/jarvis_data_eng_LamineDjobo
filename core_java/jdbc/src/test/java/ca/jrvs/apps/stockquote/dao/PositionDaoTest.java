package ca.jrvs.apps.stockquote.dao;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;



public class PositionDaoTest {

    private Connection connection;
    private PositionDao positionDao;
    private QuoteDao quoteDao;

    @Before
    public void setup() throws Exception {
        // Establish a connection to the test database
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "password");
        positionDao = new PositionDao(connection);
        quoteDao = new QuoteDao(connection);

        // Insert a quote that is necessary for adding a position (due to foreign key constraints)
        Quote quote = new Quote();
        quote.setTicker("AAPL12");
        quote.setOpen(150.0);
        quote.setHigh(155.0);
        quote.setLow(145.0);
        quote.setPrice(152.0);
        quote.setVolume(5000);
        quote.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
        quote.setPreviousClose(150.0);
        quote.setChange(2.0);
        quote.setChangePercent("1.33%");

        quoteDao.save(quote);  // Save the quote to ensure it exists in the database
    }

    @Test
    public void testSaveAndFindPosition() {
        // Now save a position for AAPL
        Position position = new Position();
        position.setTicker("AAPL12");
        position.setNumOfShares(100);
        position.setValuePaid(14500.0);

        // Save the position
        positionDao.save(position);

        // Find the saved position by ticker
        Optional<Position> result = positionDao.findById("AAPL12");
        assertTrue(result.isPresent());
        assertEquals(100, result.get().getNumOfShares());
        assertEquals(14500.0, result.get().getValuePaid(), 0);
    }

    @Test
    public void testFindAllPositions() {
        Iterable<Position> positions = positionDao.findAll();
        assertNotNull("The list of positions should not be null", positions);
        assertTrue("The list of positions should not be empty", positions.iterator().hasNext());
    }

    @Test
    public void testDeletePositionById() {
        String ticker = "AAPL12";

        // Delete the position by ticker
        positionDao.deleteById(ticker);

        // Try to find the deleted position
        Optional<Position> result = positionDao.findById(ticker);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteAllPositions() {
        positionDao.deleteAll();
        Iterable<Position> positions = positionDao.findAll();
        assertFalse(positions.iterator().hasNext());
    }
}

