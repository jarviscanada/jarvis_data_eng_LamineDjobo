package ca.jrvs.apps.stockquote.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class QuoteHttpHelperTest {
    private static final Logger logger = LogManager.getLogger(QuoteHttpHelper.class);

    private QuoteHttpHelper quoteHttpHelper;

    @Before
    public void setup() {
        quoteHttpHelper = new QuoteHttpHelper();
    }

    @Test
    public void testFetchQuoteInfo() throws IOException {
        String symbol = "AAPL";
        Quote quote = quoteHttpHelper.fetchQuoteInfo(symbol);
        assertNotNull(quote);
        assertEquals("AAPL", quote.getTicker());
        assertTrue(quote.getPrice() > 0);
    }

    public static void main(String[] args) {
        QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper();
        String symbol = "AAPL";

        try {
            Quote quote = quoteHttpHelper.fetchQuoteInfo(symbol);
            System.out.println("Fetched quote for symbol: " + symbol);
            System.out.println("Price: " + quote.getPrice());
            System.out.println("Volume: " + quote.getVolume());
        } catch (Exception e) {
            logger.error("Error finding position: " + e);
        }
    }
}
