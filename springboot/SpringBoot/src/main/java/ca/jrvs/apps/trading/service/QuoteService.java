package ca.jrvs.apps.trading.service;


import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.repository.MarketDataDao;
import ca.jrvs.apps.trading.repository.MarketDataDaoImpl;
import ca.jrvs.apps.trading.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QuoteService {

    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
    private final MarketDataDaoImpl marketDataDaoImpl;  // DAO pour appeler l'API
    private final MarketDataDao marketDataDao;  // DAO pour accéder à la base de données
    private final Random random = new Random();  // Générateur de nombres aléatoires

    @Autowired
    public QuoteService(MarketDataDaoImpl marketDataDaoImpl, MarketDataDao marketDataDao) {
        this.marketDataDaoImpl = marketDataDaoImpl;
        this.marketDataDao = marketDataDao;
    }

    /**
     * Mettre à jour la table des cotations avec les données de l'API pour tous les tickers stockés dans la base.
     */
    public void updateMarketData() {
        logger.info("Mise à jour des données de marché...");
        List<Quote> quotes = marketDataDao.findAll();  // Récupère toutes les cotations de la base

        for (Quote quote : quotes) {
            try {
                IexQuote iexQuote = findIexQuoteByTicker(quote.getTicker());
                Quote updatedQuote = buildQuoteFromIexQuote(iexQuote);
                marketDataDao.save(updatedQuote);  // Sauvegarde dans la base
                logger.info("Cotation mise à jour : {}", updatedQuote);
            } catch (IllegalArgumentException | ResourceNotFoundException e) {
                logger.error("Erreur lors de la mise à jour du ticker {}: {}", quote.getTicker(), e.getMessage());
            }
        }
    }

    /**
     * Valider et sauvegarder les tickers donnés dans la base de données.
     *
     * @param tickers Liste de tickers à sauvegarder.
     * @return Liste des cotations sauvegardées.
     */
    public List<Quote> saveQuotes(List<String> tickers) {
        logger.info("Validation et sauvegarde des tickers : {}", tickers);
        List<Quote> savedQuotes = new ArrayList<>();

        for (String ticker : tickers) {
            try {
                savedQuotes.add(saveQuoteFromTicker(ticker));  // Utilise `saveQuote(String ticker)`
            } catch (IllegalArgumentException e) {
                logger.error("Erreur lors de la validation du ticker {}: {}", ticker, e.getMessage());
            }
        }

        return savedQuotes;
    }

    /**
     * Récupérer un IexQuote à partir d'un ticker.
     *
     * @param ticker Le symbole boursier (e.g., "AAPL").
     * @return L'objet `IexQuote` correspondant.
     */
    public IexQuote findIexQuoteByTicker(String ticker) {
        logger.info("Recherche de la cotation pour le ticker : {}", ticker);
        if (ticker == null || ticker.isEmpty()) {
            logger.error("Le ticker est null ou vide !");
            throw new IllegalArgumentException("Le ticker ne peut pas être null ou vide.");
        }

        return marketDataDaoImpl.findById(ticker)
                .orElseThrow(() -> new ResourceNotFoundException("Cotation introuvable pour le ticker : " + ticker));
    }

    /**
     * Sauvegarder une cotation en base de données directement avec un objet `Quote`.
     *
     * @param quote L'objet `Quote` à sauvegarder.
     * @return L'objet `Quote` sauvegardé.
     */
    public Quote saveQuoteEntity(Quote quote) {
        logger.info("Sauvegarde de l'objet Quote : {}", quote);
        return marketDataDao.save(quote);  // Sauvegarde directe de l'objet `Quote`
    }

    /**
     * Récupérer toutes les cotations stockées dans la base.
     *
     * @return Liste des objets `Quote`.
     */
    public List<Quote> findAllQuotes() {
        logger.info("Récupération de toutes les cotations stockées...");
        return marketDataDao.findAll();  // Retourne toutes les cotations
    }

    /**
     * Méthode helper pour convertir un IexQuote en Quote.
     *
     * @param iexQuote L'objet `IexQuote` provenant de l'API.
     * @return Un objet `Quote` prêt à être sauvegardé.
     */
    protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
        Quote quote = new Quote();
        quote.setTicker(iexQuote.getSymbol());

        // Si le marché est fermé, on utilise 0.0 comme valeur par défaut.
        quote.setLastPrice(Optional.ofNullable(iexQuote.getLatestPrice()).orElse(0.0));

        // Générer un spread aléatoire pour le bidPrice et le askPrice.
        double spread = Math.round(Math.random() * 5 * 100.0) / 100.0;  // Spread max de 5 points
        double lastPrice = quote.getLastPrice();
        quote.setBidPrice(Math.max(0.0, lastPrice - spread));  // Le bidPrice ne peut pas être négatif
        quote.setAskPrice(lastPrice + spread);  // Le askPrice est toujours supérieur au prix actuel

        // Générer des tailles aléatoires pour bidSize et askSize
        quote.setBidSize((int) (Math.random() * 10 + 1));  // Entre 1 et 10
        quote.setAskSize((int) (Math.random() * 10 + 1));  // Entre 1 et 10

        return quote;
    }

    /**
     * Méthode helper pour valider et sauvegarder un ticker.
     *
     * @param ticker Le symbole boursier.
     * @return L'objet `Quote` sauvegardé.
     */

    public Quote saveQuoteFromTicker(String ticker) {
        IexQuote iexQuote = findIexQuoteByTicker(ticker);  // Récupère l'IexQuote depuis l'API
        return saveQuoteEntity(buildQuoteFromIexQuote(iexQuote));  // Sauvegarde du `Quote` construit
    }
}
