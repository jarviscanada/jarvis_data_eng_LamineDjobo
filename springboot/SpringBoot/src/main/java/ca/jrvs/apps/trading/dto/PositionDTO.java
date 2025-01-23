package ca.jrvs.apps.trading.dto;

public class PositionDTO {
    private Integer accountId;
    private String ticker;
    private Integer position;

    public PositionDTO(Integer accountId, String ticker, Integer position) {
        this.accountId = accountId;
        this.ticker = ticker;
        this.position = position;
    }

    // Getters et setters
    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}


