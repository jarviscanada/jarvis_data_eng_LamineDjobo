package ca.jrvs.apps.stockquote.dao;


import okhttp3.OkHttpClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger(QuoteDao.class);


    public static void main(String[] args) {
        logger.info("Reading properties from properties.txt");

        // Step 1: Parse properties.txt
        Map<String, String> properties = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                properties.put(tokens[0], tokens[1]);
            }
            logger.info("Properties loaded successfully");

        } catch (IOException e) {
            logger.error("Error reading properties file", e);
            return;
        }

        // Step 2: Load database driver
        try {
            logger.info("Loading database driver: {}");

            Class.forName(properties.get("db-class"));
            logger.info("Database driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.error("Error loading database driver", e);
            return;
        }

        // Step 3: Set up database connection and services
        OkHttpClient client = new OkHttpClient();
        String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/" + properties.get("database");

        logger.info("Connecting to the database");
        try (Connection connection = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
            logger.info("Database connection established successfully");

            QuoteDao quoteDao = new QuoteDao(connection);
            PositionDao positionDao = new PositionDao(connection);
            QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper();
            QuoteService quoteService = new QuoteService(quoteDao, quoteHttpHelper);
            PositionService positionService = new PositionService(positionDao, quoteService);

            // Step 4: Start the user interface
            logger.info("Starting the StockQuoteController UI");
            StockQuoteController controller = new StockQuoteController(quoteService, positionService);
            controller.initClient();
        } catch (SQLException e) {
            logger.error("Error connecting to the database", e);
        }
    }
}
