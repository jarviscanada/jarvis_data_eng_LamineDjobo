package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.AlphaVantageQuoteDOM;
import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
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
        String url = String.format("%s/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                marketDataConfig.getHost(), ticker, marketDataConfig.getToken());

        // Exécuter l'appel HTTP GET
        Optional<String> response = executeHttpGet(url);

        if (response.isPresent()) {
            try {
                // Désérialiser la réponse JSON en AlphaVantageQuoteDOM
                AlphaVantageQuoteDOM quoteDOM = objectMapper.readTree(response.get())
                        .path("Global Quote")
                        .traverse(objectMapper)
                        .readValueAs(AlphaVantageQuoteDOM.class);

                // Convertir en IexQuote et retourner
                return Optional.of(convertToIexQuote(quoteDOM));
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse Alpha Vantage response", e);
            }
        }

        return Optional.empty();
    }

    public List<IexQuote> findAllById(Iterable<String> tickers) {
        return ((List<String>) tickers).stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<String> executeHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("X-RapidAPI-Host", marketDataConfig.getHost());
        request.addHeader("X-RapidAPI-Key", marketDataConfig.getToken());

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return Optional.of(EntityUtils.toString(response.getEntity()));
            } else {
                System.err.println("Erreur HTTP : " + statusCode);
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Erreur lors de l'exécution de la requête HTTP : " + e.getMessage(), e);
        }
    }

    private IexQuote convertToIexQuote(AlphaVantageQuoteDOM alphaVantageQuoteDOM) {
        IexQuote iexQuote = new IexQuote();
        iexQuote.setSymbol(alphaVantageQuoteDOM.getSymbol());
        iexQuote.setLatestPrice(Double.parseDouble(alphaVantageQuoteDOM.getPrice()));
        iexQuote.setChangePercent(parseChangePercent(alphaVantageQuoteDOM.getChangePercent()));
        return iexQuote;
    }

    private double parseChangePercent(String changePercent) {
        return Double.parseDouble(changePercent.replace("%", ""));
    }
}
