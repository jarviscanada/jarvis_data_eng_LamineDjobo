package ca.jrvs.apps.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageQuoteDOM {

    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("05. price")
    private String price;

    @JsonProperty("09. change")
    private String change;

    @JsonProperty("10. change percent")
    private String changePercent;

    // Getters et Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }
}
