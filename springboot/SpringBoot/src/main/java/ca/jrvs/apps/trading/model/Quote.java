package ca.jrvs.apps.trading.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quote", schema = "public")
public class Quote {

    @Id
    @Column(name = "ticker", nullable = false)
    private String ticker;

    @Column(name = "last_price", nullable = false)
    private Double lastPrice;

    @Column(name = "bid_price", nullable = false)
    private Double bidPrice;

    @Column(name = "bid_size", nullable = false)
    private Integer bidSize;

    @Column(name = "ask_price", nullable = false)
    private Double askPrice;

    @Column(name = "ask_size", nullable = false)
    private Integer askSize;

    // Constructeur par défaut
    public Quote() {}

    // Constructeur avec tous les paramètres
    public Quote(String ticker, double askPrice, int askSize, double bidPrice, int bidSize, double lastPrice) {
        this.ticker = ticker;
        this.askPrice = askPrice;
        this.askSize = askSize;
        this.bidPrice = bidPrice;
        this.bidSize = bidSize;
        this.lastPrice = lastPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Integer getBidSize() {
        return bidSize;
    }

    public void setBidSize(Integer bidSize) {
        this.bidSize = bidSize;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }

    public Integer getAskSize() {
        return askSize;
    }

    public void setAskSize(Integer askSize) {
        this.askSize = askSize;
    }
}
