package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;

import java.io.IOException;
import java.util.Optional;

public class QuoteService {

    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    /**
     * Fetches the latest quote data from the Alpha Vantage API.
     * @param ticker
     * @return Latest quote information or empty optional if the ticker symbol is not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        // Validate ticker symbol
        if (ticker == null || ticker.isEmpty()) {
            throw new IllegalArgumentException("Invalid ticker symbol");
        }

        try {
            // Fetch data using QuoteHttpHelper
            Quote quote = httpHelper.fetchQuoteInfo(ticker);
            // Save the fetched quote data to the database
            dao.save(quote);
            return Optional.of(quote);
        } catch (IOException e) {
            // Return empty Optional if the quote is not found or if there was an error
            return Optional.empty();
        }
    }

    // Fetch all stock quotes from the database
    public Iterable<Quote> getAllQuotes() {
        return dao.findAll();
    }
}
