package ca.jrvs.apps.trading.service;


import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Trader;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.repository.PositionDao;
import ca.jrvs.apps.trading.repository.SecurityOrderDao;
import ca.jrvs.apps.trading.repository.TraderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TraderAccountService {

    private final TraderDao traderDao;
    private final AccountJpaRepository accountDao;
    private final PositionDao positionDao;
    private final SecurityOrderDao securityOrderDao;

    @Autowired
    public TraderAccountService(TraderDao traderDao, AccountJpaRepository accountDao, PositionDao positionDao, SecurityOrderDao securityOrderDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
    }

    /**
     * Create a new trader and initialize a new account with 0 amount
     */
    public TraderAccountView createTraderAndAccount(Trader trader) {
        if (trader == null || trader.getId() != null || trader.getFirstName() == null || trader.getLastName() == null ||
                trader.getEmail() == null || trader.getCountry() == null || trader.getDob() == null) {
            throw new IllegalArgumentException("Invalid trader details");
        }

        Trader savedTrader = traderDao.save(trader);
        Account account = new Account();
        account.setId(savedTrader.getId());
        account.setAmount(0.0);
        Account savedAccount = accountDao.save(account);

        return new TraderAccountView(savedTrader, savedAccount);
    }

    /**
     * Delete a trader if they have no open positions and a 0 cash balance
     */
    public void deleteTraderById(Integer traderId) {
        if (traderId == null || !traderDao.existsById(traderId)) {
            throw new IllegalArgumentException("Trader ID is invalid or not found");
        }

        Account account = accountDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for traderId: " + traderId));

        if (account.getAmount() != 0.0) {
            throw new IllegalArgumentException("Cannot delete trader with non-zero balance");
        }

        if (!positionDao.findAll().stream().filter(p -> p.getAccountId().equals(traderId)).allMatch(p -> p.getPosition() == 0)) {
            throw new IllegalArgumentException("Cannot delete trader with open positions");
        }

        securityOrderDao.deleteAll();
        accountDao.delete(account);
        traderDao.deleteById(traderId);
    }

    /**
     * Deposit funds into a trader's account
     */
    public Account deposit(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("Invalid trader ID or fund");
        }

        Account account = accountDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for traderId: " + traderId));

        account.setAmount(account.getAmount() + fund);
        return accountDao.save(account);
    }

    /**
     * Withdraw funds from a trader's account
     */
    public Account withdraw(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("Invalid trader ID or fund");
        }

        Account account = accountDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for traderId: " + traderId));

        if (account.getAmount() < fund) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        account.setAmount(account.getAmount() - fund);
        return accountDao.save(account);
    }
}
