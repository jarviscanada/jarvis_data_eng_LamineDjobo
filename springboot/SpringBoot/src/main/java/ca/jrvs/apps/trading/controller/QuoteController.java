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
     * Récupérer une cotation depuis l'API et la sauvegarder dans la base de données.
     * URL : GET http://localhost:8080/quote/iex/ticker/{ticker}
     */
    @GetMapping("/iex/ticker/{ticker}")
    public ResponseEntity<Quote> getQuote(@PathVariable String ticker) {
        logger.info("Requête reçue pour le ticker : {}", ticker);
        try {
            Quote savedQuote = quoteService.saveQuoteFromTicker(ticker);  // Appelle la méthode pour sauvegarder le ticker
            return new ResponseEntity<>(savedQuote, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Récupérer toutes les cotations stockées dans la base de données.
     * URL : GET http://localhost:8080/quote/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<Quote>> getAllQuotes() {
        logger.info("Requête reçue pour récupérer toutes les cotations.");
        List<Quote> quotes = quoteService.findAllQuotes();
        return new ResponseEntity<>(quotes, HttpStatus.OK);
    }

    /**
     * Mettre à jour toutes les cotations en base avec les données de l'API.
     * URL : PUT http://localhost:8080/quote/iexMarketData
     */
    @PutMapping("/iexMarketData")
    public ResponseEntity<String> updateMarketData() {
        logger.info("Requête reçue pour mettre à jour les données de marché.");
        try {
            quoteService.updateMarketData();  // Met à jour toutes les cotations
            return new ResponseEntity<>("Toutes les données de marché ont été mises à jour avec succès.", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour des données de marché : {}", e.getMessage());
            return new ResponseEntity<>("Erreur lors de la mise à jour des données de marché.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Mettre à jour un `Quote` spécifique dans la table des cotations sans validation.
     * URL : PUT http://localhost:8080/quote/
     */
    @PutMapping("/")
    public ResponseEntity<Quote> putQuote(@RequestBody Quote quote) {
        logger.info("Requête reçue pour mettre à jour le quote : {}", quote);
        try {
            Quote updatedQuote = quoteService.saveQuoteEntity(quote);
            return new ResponseEntity<>(updatedQuote, HttpStatus.OK);  // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Erreur de validation lors de la mise à jour : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Ajouter un nouveau ticker dans la table des cotations.
     * URL : POST http://localhost:8080/quote/tickerId/{tickerId}
     */
    @PostMapping("/tickerId/{tickerId}")
    public ResponseEntity<Quote> createQuote(@PathVariable String tickerId) {
        logger.info("Requête reçue pour ajouter un nouveau ticker : {}", tickerId);
        try {
            // Appelle la méthode qui prend un `ticker` pour récupérer l'`IexQuote` et le convertir en `Quote`.
            Quote newQuote = quoteService.saveQuoteFromTicker(tickerId);
            return new ResponseEntity<>(newQuote, HttpStatus.CREATED);  // 201 CREATED
        } catch (IllegalArgumentException e) {
            logger.error("Erreur de validation pour le ticker {} : {}", tickerId, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Afficher la liste quotidienne des cotations stockées.
     * URL : GET http://localhost:8080/quote/dailyList
     */
    @GetMapping("/dailyList")
    public ResponseEntity<List<Quote>> getDailyList() {
        logger.info("Requête reçue pour afficher la liste quotidienne des cotations.");
        try {
            List<Quote> quotes = quoteService.findAllQuotes();
            return new ResponseEntity<>(quotes, HttpStatus.OK);  // 200 OK
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage de la liste quotidienne : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }
}
