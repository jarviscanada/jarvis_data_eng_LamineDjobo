package ca.jrvs.apps.trading.model;

public class SecurityRow {

    private String ticker;   // Ex: "AAPL"
    private int position;    // Nombre d'actions détenues
    private double marketValue; // Valeur de marché calculée (position * prix actuel)

    public SecurityRow(String ticker, int position, double marketValue) {
        this.ticker = ticker;
        this.position = position;
        this.marketValue = marketValue;
    }

    // Getters et Setters
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    @Override
    public String toString() {
        return "SecurityRow{" +
                "ticker='" + ticker + '\'' +
                ", position=" + position +
                ", marketValue=" + marketValue +
                '}';
    }
}
