package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Trader;

public class TraderAccountView {

    private Trader trader;
    private Account account;

    public TraderAccountView(Trader trader, Account account) {
        this.trader = trader;
        this.account = account;
    }

    public Trader getTrader() {
        return trader;
    }

    public Account getAccount() {
        return account;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

