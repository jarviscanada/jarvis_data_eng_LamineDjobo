package ca.jrvs.apps.stockquote.dao;


import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.*;

public class PositionService_IntTest {

    private PositionService positionService;
    private PositionDao positionDao;
    private QuoteService quoteService;
    private Connection connection;

    @Before
    public void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "password");
        positionDao = new PositionDao(connection);
        QuoteDao quoteDao = new QuoteDao(connection);
        QuoteHttpHelper httpHelper = new QuoteHttpHelper();
        quoteService = new QuoteService(quoteDao, httpHelper);
        positionService = new PositionService(positionDao, quoteService);
    }

    @Test
    public void testBuyAndSell() {
        Position position = positionService.buy("AAPL", 50, 150.0);
        assertNotNull(position);
        assertEquals(50, position.getNumOfShares());

        positionService.sell("AAPL");
    }
}
