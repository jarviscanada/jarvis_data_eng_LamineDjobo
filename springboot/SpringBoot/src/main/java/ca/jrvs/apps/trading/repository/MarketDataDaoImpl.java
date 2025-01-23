package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.AlphaVantageQuoteDOM;
import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MarketDataDaoImpl {
        private final MarketDataConfig marketDataConfig;
        private final ObjectMapper objectMapper;
        private final CloseableHttpClient httpClient;

        @Autowired
        public MarketDataDaoImpl(MarketDataConfig marketDataConfig, ObjectMapper objectMapper, CloseableHttpClient httpClient){
            this.marketDataConfig = marketDataConfig;
            this.objectMapper = objectMapper;
            this.httpClient = httpClient;
        }

    public Optional<IexQuote> findById(String ticker) {
        String url = String.format("%s/query?function=GLOBAL_QUOTE&symbol=%s&datatype=json",
                marketDataConfig.getHost(), ticker);

        // Exécuter l'appel HTTP GET
        Optional<String> response = executeHttpGet(url);

        if (response.isPresent() && response.get().contains("Error Message")) {
            System.out.println("Erreur de l'API pour ce ticker.");
            return Optional.empty();
        }

        if (response.isPresent()) {
            try {
                JsonNode rootNode = objectMapper.readTree(response.get());
                JsonNode globalQuoteNode = rootNode.path("Global Quote");

                if (globalQuoteNode.isEmpty()) {
                    System.err.println("No Global Quote found in the response.");
                    return Optional.empty();
                }

                AlphaVantageQuoteDOM quoteDOM = globalQuoteNode.traverse(objectMapper)
                        .readValueAs(AlphaVantageQuoteDOM.class);
                return Optional.of(convertToIexQuote(quoteDOM));
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse Alpha Vantage response", e);
            }
        }





        return Optional.empty();
    }

    public List<IexQuote> findAllById(Iterable<String> tickers) {
        return ((List<String>) tickers).stream()
                .map(ticker -> {
                    Optional<IexQuote> quote = findById(ticker);
                    if (quote.isEmpty()) {
                        System.err.println("Cotation introuvable pour le ticker : " + ticker);
                    } else {
                        System.out.println("Cotation récupérée : " + quote.get().getSymbol());
                    }
                    return quote;
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    private Optional<String> executeHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com");
        request.addHeader("X-RapidAPI-Key", marketDataConfig.getToken());

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Statut HTTP : " + statusCode);

            if (statusCode == 200) {
                String body = EntityUtils.toString(response.getEntity());
                System.out.println("Réponse API : " + body);

                if (body.contains("Error Message")) {
                    System.err.println("Erreur : requête non valide ou ticker invalide.");
                    return Optional.empty();
                }
                if (body.contains("Note")) {
                    System.err.println("Limitation d'appels atteinte.");
                    return Optional.empty();
                }

                return Optional.of(body);
            } else {
                System.out.println("Erreur HTTP pour l'URL : " + url);
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Erreur lors de l'exécution de la requête HTTP : " + e.getMessage(), e);
        }
    }


    private IexQuote convertToIexQuote(AlphaVantageQuoteDOM alphaVantageQuoteDOM) {
        IexQuote iexQuote = new IexQuote();
        iexQuote.setSymbol(alphaVantageQuoteDOM.getSymbol());



        // Vérification du champ "price"
        String priceStr = alphaVantageQuoteDOM.getPrice();
        if (priceStr == null || priceStr.isBlank()) {
            System.err.println("Le champ 'price' est null ou vide. Ticker peut être invalide.");
            iexQuote.setLatestPrice(0.0);  // Définir une valeur par défaut
        } else {
            iexQuote.setLatestPrice(Double.parseDouble(priceStr.trim()));  // Trim pour supprimer les espaces
        }

        // Vérification du champ "changePercent"
        String changePercentStr = alphaVantageQuoteDOM.getChangePercent();
        if (changePercentStr == null || changePercentStr.isBlank()) {
            System.err.println("Le champ 'changePercent' est null ou vide.");
            iexQuote.setChangePercent(0.0);  // Définir à 0 si absent
        } else {
            iexQuote.setChangePercent(parseChangePercent(changePercentStr.trim()));
        }

        return iexQuote;
    }



    private double parseChangePercent(String changePercent) {
        return Double.parseDouble(changePercent.replace("%", ""));
    }
}
