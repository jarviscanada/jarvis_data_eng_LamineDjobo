package ca.jrvs.apps.stockquote.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

    private final Connection connection;

    public PositionDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Position save(Position position) throws IllegalArgumentException {
        String upsertSQL = "INSERT INTO position (symbol, number_of_shares, value_paid) "
                + "VALUES (?, ?, ?) "
                + "ON CONFLICT (symbol) DO UPDATE SET number_of_shares = EXCLUDED.number_of_shares, value_paid = EXCLUDED.value_paid";

        try (PreparedStatement stmt = connection.prepareStatement(upsertSQL)) {
            stmt.setString(1, position.getTicker());
            stmt.setInt(2, position.getNumOfShares());
            stmt.setDouble(3, position.getValuePaid());

            stmt.executeUpdate();
            return position;

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error saving position", e);
        }
    }

    @Override
    public Optional<Position> findById(String symbol) throws IllegalArgumentException {
        String selectSQL = "SELECT * FROM position WHERE symbol = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setString(1, symbol);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractPosition(rs));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error finding position by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Position> findAll() {
        String selectAllSQL = "SELECT * FROM position";
        List<Position> positions = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectAllSQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                positions.add(extractPosition(rs));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error fetching all positions", e);
        }
        return positions;
    }

    @Override
    public void deleteById(String symbol) throws IllegalArgumentException {
        String deleteSQL = "DELETE FROM position WHERE symbol = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
            stmt.setString(1, symbol);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error deleting position by id", e);
        }
    }

    @Override
    public void deleteAll() {
        String deleteSQL = "DELETE FROM position";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error deleting all positions", e);
        }
    }

    private Position extractPosition(ResultSet rs) throws SQLException {
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));
        return position;
    }
}
