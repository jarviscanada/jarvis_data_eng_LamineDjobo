package ca.jrvs.apps.trading.service;


import ca.jrvs.apps.trading.model.*;
import ca.jrvs.apps.trading.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.repository.PositionDao;
import ca.jrvs.apps.trading.repository.QuoteDao;
import ca.jrvs.apps.trading.repository.TraderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DashboardService {

    private final TraderDao traderDao;
    private final AccountJpaRepository accountDao;
    private final PositionDao positionDao;
    private final QuoteDao quoteDao;

    @Autowired
    public DashboardService(TraderDao traderDao, AccountJpaRepository accountDao, PositionDao positionDao, QuoteDao quoteDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.quoteDao = quoteDao;
    }

    /**
     * Crée et retourne une vue traderAccountView par ID du trader
     */
    public TraderAccountView getTraderAccount(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("Trader ID ne peut pas être null");
        }

        Trader trader = traderDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Trader ID non trouvé"));
        Account account = findAccountByTraderId(traderId);

        return new TraderAccountView(trader, account);
    }

    /**
     * Crée et retourne une vue portfolioView par ID du trader
     */
    public PortfolioView getProfileViewByTraderId(Integer traderId) {
        Account account = findAccountByTraderId(traderId);

        List<SecurityRow> securityRows = positionDao.findAllDTO().stream()
                .filter(position -> position.getAccountId().equals(account.getId()))
                .map(positionDTO -> {
                    double marketPrice = quoteDao.findById(positionDTO.getTicker())
                            .orElseThrow(() -> new IllegalArgumentException("Quote non trouvée pour le ticker"))
                            .getLastPrice();
                    return new SecurityRow(positionDTO.getTicker(), positionDTO.getPosition(), marketPrice);
                })
                .collect(Collectors.toList());

        return new PortfolioView(securityRows);
    }


    /**
     * Méthode helper pour trouver le compte correspondant au trader
     */
    private Account findAccountByTraderId(Integer traderId) {
        return accountDao.getAccountByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé pour le trader ID"));
    }
}

