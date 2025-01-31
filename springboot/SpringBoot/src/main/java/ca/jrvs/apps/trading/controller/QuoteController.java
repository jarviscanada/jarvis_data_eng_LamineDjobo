package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quote")
public class QuoteController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    /**
     * Get quote details for a specific ticker.
     *
     * @param ticker The stock ticker symbol (e.g., "AAPL").
     * @return ResponseEntity containing the retrieved quote.
     * URL: GET http://localhost:8080/quote/iex/ticker/{ticker}
     */
    @GetMapping("/iex/ticker/{ticker}")
    public ResponseEntity<Quote> getQuote(@PathVariable String ticker) {
        logger.info("Request received for ticker: {}", ticker);
        try {
            Quote savedQuote = quoteService.saveQuoteFromTicker(ticker);
            return new ResponseEntity<>(savedQuote, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Error: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve all stored quotes.
     *
     * @return ResponseEntity containing a list of all quotes.
     * URL: GET http://localhost:8080/quote/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<Quote>> getAllQuotes() {
        logger.info("Request received to retrieve all quotes.");
        List<Quote> quotes = quoteService.findAllQuotes();
        return new ResponseEntity<>(quotes, HttpStatus.OK);
    }

    /**
     * Update all market data quotes from an external API.
     *
     * @return ResponseEntity indicating the update status.
     * URL: PUT http://localhost:8080/quote/iexMarketData
     */
    @PutMapping("/iexMarketData")
    public ResponseEntity<String> updateMarketData() {
        logger.info("Request received to update market data.");
        try {
            quoteService.updateMarketData();
            return new ResponseEntity<>("All market data successfully updated.", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating market data: {}", e.getMessage());
            return new ResponseEntity<>("Error updating market data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a specific `Quote` in the quotes table without validation.
     *
     * @param quote The `Quote` object to be updated.
     * @return ResponseEntity containing the updated quote.
     * URL: PUT http://localhost:8080/quote/
     */
    @PutMapping("/")
    public ResponseEntity<Quote> putQuote(@RequestBody Quote quote) {
        logger.info("Request received to update quote: {}", quote);
        try {
            Quote updatedQuote = quoteService.saveQuoteEntity(quote);
            return new ResponseEntity<>(updatedQuote, HttpStatus.OK);  // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during update: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Add a new ticker to the quotes table.
     *
     * @param tickerId The stock ticker symbol to be added.
     * @return ResponseEntity containing the newly created quote.
     * URL: POST http://localhost:8080/quote/tickerId/{tickerId}
     */

    @PostMapping("/tickerId/{tickerId}")
    public ResponseEntity<Quote> createQuote(@PathVariable String tickerId) {
        logger.info("Request received to add a new ticker: {}", tickerId);
        try {
            // Calls the method that retrieves an `IexQuote` from the API and converts it to `Quote`.
            Quote newQuote = quoteService.saveQuoteFromTicker(tickerId);
            return new ResponseEntity<>(newQuote, HttpStatus.CREATED);  // 201 CREATED
        } catch (IllegalArgumentException e) {
            logger.error("Validation error for ticker {}: {}", tickerId, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Retrieve the daily list of stored quotes.
     *
     * @return ResponseEntity containing a list of quotes stored for daily tracking.
     * URL: GET http://localhost:8080/quote/dailyList
     */
    @GetMapping("/dailyList")
    public ResponseEntity<List<Quote>> getDailyList() {
        logger.info("Request received to fetch the daily quote list.");
        try {
            List<Quote> quotes = quoteService.findAllQuotes();
            return new ResponseEntity<>(quotes, HttpStatus.OK);  // 200 OK
        } catch (Exception e) {
            logger.error("Error retrieving the daily list: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }
}
