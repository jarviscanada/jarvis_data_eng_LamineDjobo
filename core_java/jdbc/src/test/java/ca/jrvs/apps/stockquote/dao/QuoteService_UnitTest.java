package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QuoteService_UnitTest {

    private QuoteService quoteService;
    private QuoteDao quoteDao;
    private QuoteHttpHelper httpHelper;

    @Before
    public void setup() {
        quoteDao = mock(QuoteDao.class);
        httpHelper = mock(QuoteHttpHelper.class);
        quoteService = new QuoteService(quoteDao, httpHelper);
    }

    @Test
    public void testFetchQuoteDataFromAPI() throws IOException {
        Quote quote = new Quote();
        quote.setTicker("AAPL");
        quote.setPrice(150.0);
        when(httpHelper.fetchQuoteInfo("AAPL")).thenReturn(quote);

        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("AAPL");
        assertTrue(result.isPresent());
        assertEquals("AAPL", result.get().getTicker());
        assertEquals(150.0, result.get().getPrice(), 0);

        verify(quoteDao, times(1)).save(quote);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTicker() {
        quoteService.fetchQuoteDataFromAPI("");
    }

    @Test
    public void testFetchQuoteDataFromAPINotFound() throws IOException {
        when(httpHelper.fetchQuoteInfo("XYZ")).thenThrow(IOException.class);

        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("XYZ");
        assertFalse(result.isPresent());
    }
}
