package ca.jrvs.apps.stockquote.dao;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {
    private static final Logger logger = LogManager.getLogger(QuoteDao.class);
    private final Connection connection;

    public QuoteDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Quote save(Quote quote) throws IllegalArgumentException {
        logger.info("Saving quote");

        String upsertSQL = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT (symbol) DO UPDATE SET open = EXCLUDED.open, high = EXCLUDED.high, low = EXCLUDED.low, "
                + "price = EXCLUDED.price, volume = EXCLUDED.volume, latest_trading_day = EXCLUDED.latest_trading_day, "
                + "previous_close = EXCLUDED.previous_close, change = EXCLUDED.change, change_percent = EXCLUDED.change_percent";

        try (PreparedStatement stmt = connection.prepareStatement(upsertSQL)) {
            // Set parameters for the insert statement
            stmt.setString(1, quote.getTicker());
            stmt.setDouble(2, quote.getOpen());
            stmt.setDouble(3, quote.getHigh());
            stmt.setDouble(4, quote.getLow());
            stmt.setDouble(5, quote.getPrice());
            stmt.setInt(6, quote.getVolume());
            stmt.setDate(7, quote.getLatestTradingDay());
            stmt.setDouble(8, quote.getPreviousClose());
            stmt.setDouble(9, quote.getChange());
            stmt.setString(10, quote.getChangePercent());

            stmt.executeUpdate();
            logger.info("Quote saved successfully ");
            return quote;

        } catch (SQLException e) {
            logger.error("Error saving quote",  e);
            throw new IllegalArgumentException("Error saving quote", e);
        }
    }

    @Override
    public Optional<Quote> findById(String symbol) throws IllegalArgumentException {
        logger.info("Fetching quote with symbol");
        String selectSQL = "SELECT * FROM quote WHERE symbol = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setString(1, symbol);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                logger.info("Found quote");
                return Optional.of(extractQuote(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding quote by id",e);
            throw new IllegalArgumentException("Error finding quote by id", e);
        }
        logger.warn("No quote found for this symbol");
        return Optional.empty();
    }


    @Override
    public Iterable<Quote> findAll() {
        logger.info("Fetching all quotes");
        String selectAllSQL = "SELECT * FROM quote";
        List<Quote> quotes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectAllSQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                quotes.add(extractQuote(rs));
            }
            logger.info("Found {} quotes");
        } catch (SQLException e) {
            logger.error("Error fetching all quotes", e);
            throw new IllegalArgumentException("Error fetching all quotes", e);
        }
        return quotes;
    }


    @Override
    public void deleteById(String symbol) throws IllegalArgumentException {
        logger.info("Deleting quote and associated positions");

        // Delete any positions
        String deletePositionSQL = "DELETE FROM position WHERE symbol = ?";
        try (PreparedStatement positionStmt = connection.prepareStatement(deletePositionSQL)) {
            positionStmt.setString(1, symbol);
            positionStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting positions ", e);  // Log error
            throw new IllegalArgumentException("Error deleting position by id", e);
        }

        // Now delete the quote itself
        String deleteQuoteSQL = "DELETE FROM quote WHERE symbol = ?";
        try (PreparedStatement quoteStmt = connection.prepareStatement(deleteQuoteSQL)) {
            quoteStmt.setString(1, symbol);
            quoteStmt.executeUpdate();
            logger.info("Quote and positions deleted successfully");
        } catch (SQLException e) {
            logger.error("Error deleting quote by id", e);
            throw new IllegalArgumentException("Error deleting quote by id", e);
        }
    }



    @Override
    public void deleteAll() {
        logger.info("Deleting all quotes");
        String deleteSQL = "DELETE FROM quote";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
            stmt.executeUpdate();
            logger.info("All quotes deleted successfully");
        } catch (SQLException e) {
            logger.error("Error deleting all quotes", e);
            throw new IllegalArgumentException("Error deleting all quotes", e);
        }
    }


    private Quote extractQuote(ResultSet rs) throws SQLException {
        Quote quote = new Quote();
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        return quote;
    }
}