package ca.jrvs.apps.trading.model;

import java.util.List;

public class PortfolioView {

    private List<SecurityRow> securityRows;  // Liste des actions détenues

    public PortfolioView(List<SecurityRow> securityRows) {
        this.securityRows = securityRows;
    }

    // Getter et Setter
    public List<SecurityRow> getSecurityRows() {
        return securityRows;
    }

    public void setSecurityRows(List<SecurityRow> securityRows) {
        this.securityRows = securityRows;
    }

    @Override
    public String toString() {
        return "PortfolioView{" +
                "securityRows=" + securityRows +
                '}';
    }
}
