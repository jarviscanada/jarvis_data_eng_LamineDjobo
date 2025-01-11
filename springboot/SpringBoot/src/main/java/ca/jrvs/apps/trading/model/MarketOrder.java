package ca.jrvs.apps.trading.model;

public class MarketOrder {

    private String ticker;   // Le symbole de l'action (e.g., "AAPL")
    private int size;        // La quantité d'actions à acheter/vendre
    private int traderId;    // L'identifiant du trader
    private Option option;   // Le type d'opération (BUY ou SELL)

    public enum Option { BUY, SELL }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}

