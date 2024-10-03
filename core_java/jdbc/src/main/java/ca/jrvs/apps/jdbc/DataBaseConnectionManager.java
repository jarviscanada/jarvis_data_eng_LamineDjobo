package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnectionManager {

    private final String url;
    private final Properties properties;

    public DataBaseConnectionManager(String host,String port, String databaseName, String username, String password) {
        this.url = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
        this.properties = new Properties();
        this.properties.setProperty("user",username);
        this.properties.setProperty("password",password);
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }

        return DriverManager.getConnection(this.url, this.properties);
    }

}
