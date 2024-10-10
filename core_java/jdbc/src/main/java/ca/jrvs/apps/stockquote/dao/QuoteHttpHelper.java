package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class QuoteHttpHelper {
    private static final Logger logger = LogManager.getLogger(QuoteDao.class);

    private static final String API_KEY = "97a17ce634msh9054341c3c37c6cp15ddb9jsn41edd7e0ae7c"; // Alpha Vantage API key
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private OkHttpClient client;

    public QuoteHttpHelper() {
        this.client = new OkHttpClient();
    }

    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException, IOException {
        // Build the request URL
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("function", "GLOBAL_QUOTE")
                .addQueryParameter("symbol", symbol)
                .addQueryParameter("apikey", API_KEY)
                .build();

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            logger.error("Failed to fetch data from API: " + response);
            throw new IOException("Failed to fetch data from API: " + response);
        }

        // Parse the response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body().string()).get("Global Quote");

        if (jsonNode == null || !jsonNode.has("01. symbol")) {
            logger.error("Error finding this symbol");
            throw new IllegalArgumentException("Invalid symbol: " + symbol);
        }

        // Map JSON response to Quote object
        return mapJsonToQuote(jsonNode);
    }

    private Quote mapJsonToQuote(JsonNode jsonNode) {
        Quote quote = new Quote();
        quote.setTicker(jsonNode.get("01. symbol").asText());
        quote.setOpen(jsonNode.get("02. open").asDouble());
        quote.setHigh(jsonNode.get("03. high").asDouble());
        quote.setLow(jsonNode.get("04. low").asDouble());
        quote.setPrice(jsonNode.get("05. price").asDouble());
        quote.setVolume(jsonNode.get("06. volume").asInt());
        quote.setLatestTradingDay(java.sql.Date.valueOf(jsonNode.get("07. latest trading day").asText()));
        quote.setPreviousClose(jsonNode.get("08. previous close").asDouble());
        quote.setChange(jsonNode.get("09. change").asDouble());
        quote.setChangePercent(jsonNode.get("10. change percent").asText());

        return quote;
    }
}