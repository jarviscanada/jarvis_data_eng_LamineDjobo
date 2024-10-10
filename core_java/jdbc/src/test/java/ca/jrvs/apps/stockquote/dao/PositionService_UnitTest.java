package ca.jrvs.apps.stockquote.dao;



import ca.jrvs.apps.stockquote.dao.PositionDao;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PositionService_UnitTest {

    private PositionService positionService;
    private PositionDao positionDao;
    private QuoteService quoteService;

    @Before
    public void setup() {
        positionDao = mock(PositionDao.class);
        quoteService = mock(QuoteService.class);
        positionService = new PositionService(positionDao, quoteService);
    }

    @Test
    public void testBuy() {
        Quote quote = new Quote();
        quote.setTicker("AAPL");
        quote.setPrice(150.0);
        quote.setVolume(10000);
        when(quoteService.fetchQuoteDataFromAPI("AAPL")).thenReturn(Optional.of(quote));

        Position position = new Position();
        position.setTicker("AAPL");
        position.setNumOfShares(50);
        position.setValuePaid(7500.0);
        when(positionDao.save(any(Position.class))).thenReturn(position);

        Position result = positionService.buy("AAPL", 50, 150.0);
        assertNotNull(result);
        assertEquals(50, result.getNumOfShares());
        assertEquals(7500.0, result.getValuePaid(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuyMoreThanVolume() {
        Quote quote = new Quote();
        quote.setTicker("AAPL");
        quote.setVolume(10);  // Low volume
        when(quoteService.fetchQuoteDataFromAPI("AAPL")).thenReturn(Optional.of(quote));

        positionService.buy("AAPL", 100, 150.0);  // Trying to buy more than available volume
    }

    @Test
    public void testSell() {
        positionService.sell("AAPL");
        verify(positionDao, times(1)).deleteById("AAPL");
    }
}
