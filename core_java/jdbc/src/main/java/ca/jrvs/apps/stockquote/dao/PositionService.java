package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.dao.PositionDao;

import java.util.Optional;

public class PositionService {

    private PositionDao dao;
    private QuoteService quoteService;

    public PositionService(PositionDao dao, QuoteService quoteService) {
        this.dao = dao;
        this.quoteService = quoteService;
    }

    /**
     * Processes a buy order and updates the database accordingly.
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        // Validate input
        if (numberOfShares <= 0 || price <= 0) {
            throw new IllegalArgumentException("Invalid number of shares or price");
        }

        // Fetch the latest quote
        Quote latestQuote = quoteService.fetchQuoteDataFromAPI(ticker)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticker symbol"));

        // Ensure there is enough volume to buy
        if (numberOfShares > latestQuote.getVolume()) {
            throw new IllegalArgumentException("Cannot buy more than available volume");
        }

        // Fetch current position or create a new one
        Position position = dao.findById(ticker).orElse(new Position());
        position.setTicker(ticker);
        position.setNumOfShares(position.getNumOfShares() + numberOfShares);
        position.setValuePaid(position.getValuePaid() + (numberOfShares * price));

        // Save the updated position
        return dao.save(position);
    }

    public Optional<Position> getPositionForStock(String ticker) {
        return dao.findById(ticker);  // Fetch position from the database
    }


    /**
     * Sells all shares of the given ticker symbol.
     * @param ticker
     */
    public void sell(String ticker) {
        // Delete the position by ticker
        dao.deleteById(ticker);
    }
}
