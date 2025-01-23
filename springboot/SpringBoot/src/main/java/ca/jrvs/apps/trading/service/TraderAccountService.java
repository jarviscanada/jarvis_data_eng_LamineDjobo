package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dto.PositionDTO;
import ca.jrvs.apps.trading.model.*;
import ca.jrvs.apps.trading.repository.*;
import ca.jrvs.apps.trading.model.TraderAccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * Crée un trader et un compte associé.
     */
    @Transactional
    public TraderAccountView createTraderAndAccount(Trader trader) {
        if (trader == null ||
                trader.getFirstName() == null || trader.getFirstName().isEmpty() ||
                trader.getLastName() == null || trader.getLastName().isEmpty() ||
                trader.getEmail() == null || trader.getEmail().isEmpty() ||
                trader.getDob() == null ||
                trader.getCountry() == null || trader.getCountry().isEmpty()) {
            throw new IllegalArgumentException("Trader fields must not be null or empty.");

    }

        if (trader.getId() != null) {
            throw new IllegalArgumentException("Trader ID must be null when creating a new trader.");
        }

        // Save Trader
        traderDao.save(trader);

        // Create Account
        Account account = new Account();
        account.setTrader(trader);
        account.setAmount(0.0); // Initialize with 0 amount
        accountDao.save(account);

        // Return TraderAccountView
        return new TraderAccountView(trader, account);
    }



    /**
     * Supprime un trader s'il n'a pas de position ouverte et un solde de 0.
     */
    @Transactional
    public void deleteTraderById(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("Trader ID cannot be null");
        }

        Trader trader = traderDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Trader ID not found"));

        Account account = accountDao.getAccountByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("No account found for this trader"));

        // Check for open positions
        List<PositionDTO> positions = positionDao.findAllDTO().stream()
                .filter(p -> p.getAccountId().equals(account.getId()))
                .toList();
        if (!positions.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete trader with open positions");
        }

        // Check if account balance is 0
        if (account.getAmount() > 0) {
            throw new IllegalArgumentException("Cannot delete trader with non-zero account balance");
        }

        // Delete related security orders
        securityOrderDao.deleteAllByAccountId(account.getId());

        // Delete the account
        accountDao.delete(account);

        // Delete the trader
        traderDao.deleteById(trader.getId());
    }



    /**
     * Dépôt d'un montant sur le compte du trader.
     */
    public Account deposit(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("Trader ID and fund must not be null, and fund must be greater than 0.");
        }

        // Ensure Trader exists
        Trader trader = traderDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Trader ID not found"));

        // Find or create the associated account
        Account account = accountDao.getAccountByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for Trader ID"));

        // Update the account's funds
        account.setAmount(account.getAmount() + fund);
        return accountDao.save(account);
    }


    /**
     * Retrait d'un montant sur le compte du trader.
     */
    public Account withdraw(Integer traderId, Double fund) {
        if (traderId == null || fund == null || fund <= 0) {
            throw new IllegalArgumentException("Trader ID and fund must not be null, and fund must be greater than 0.");
        }

        // Ensure Trader exists
        Trader trader = traderDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Trader ID not found"));

        // Find the associated account
        Account account = accountDao.getAccountByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for Trader ID"));

        if (account.getAmount() < fund) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        // Update account balance
        account.setAmount(account.getAmount() - fund);
        return accountDao.save(account);
    }

}

