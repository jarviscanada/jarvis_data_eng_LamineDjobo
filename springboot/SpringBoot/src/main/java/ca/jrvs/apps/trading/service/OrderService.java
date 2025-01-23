package ca.jrvs.apps.trading.service;


import ca.jrvs.apps.trading.dto.PositionDTO;
import ca.jrvs.apps.trading.model.*;
import ca.jrvs.apps.trading.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.repository.PositionDao;
import ca.jrvs.apps.trading.repository.QuoteDao;
import ca.jrvs.apps.trading.repository.SecurityOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final AccountJpaRepository accountDao;
    private final SecurityOrderDao securityOrderDao;
    private final QuoteDao quoteDao;
    private final PositionDao positionDao;

    @Autowired
    public OrderService(AccountJpaRepository accountDao, SecurityOrderDao securityOrderDao, QuoteDao quoteDao, PositionDao positionDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * Exécute un ordre de marché.
     */
    public SecurityOrder executeMarketOrder(MarketOrder orderData) {
        validateMarketOrder(orderData);  // Valider la requête

        // Récupérer le compte du trader
        Account account = accountDao.getAccountByTraderId(orderData.getTraderId())
                .orElseThrow(() -> new IllegalArgumentException("Trader ID non trouvé"));

        // Récupérer la cotation pour le ticker
        Quote quote = quoteDao.findById(orderData.getTicker())
                .orElseThrow(() -> new IllegalArgumentException("Ticker non trouvé"));

        // Créer un objet SecurityOrder pour sauvegarder l'ordre
        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setAccount(account);
        securityOrder.setStatus("PENDING");
        securityOrder.setQuote(quote);
        securityOrder.setSize(Math.abs(orderData.getSize()));  // Toujours positif
        securityOrder.setNotes(orderData.getOption().name());

        // Traiter l'achat ou la vente
        if (orderData.getOption() == MarketOrder.Option.BUY) {
            handleBuyMarketOrder(orderData, securityOrder, account);
        } else {
            handleSellMarketOrder(orderData, securityOrder, account);
        }

        // Sauvegarder l'ordre et renvoyer le résultat
        return securityOrderDao.save(securityOrder);
    }

    /**
     * Helper pour gérer l'achat.
     */
    protected void handleBuyMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder, Account account) {
        Quote quote = quoteDao.findById(marketOrder.getTicker())
                .orElseThrow(() -> new IllegalArgumentException("Ticker non trouvé"));
        double totalCost = quote.getAskPrice() * securityOrder.getSize();

        if (account.getAmount() < totalCost) {
            securityOrder.setStatus("CANCELED");
            securityOrder.setNotes("Fonds insuffisants pour acheter");
            throw new IllegalArgumentException("Fonds insuffisants.");
        }

        // Déduction des fonds et validation
        account.setAmount(account.getAmount() - totalCost);
        securityOrder.setPrice(quote.getAskPrice());
        securityOrder.setStatus("FILLED");
        accountDao.save(account);
    }

    /**
     * Helper pour gérer la vente.
     */
    protected void handleSellMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder, Account account) {
        // Récupération de la position du compte pour le ticker donné
        Optional<PositionDTO> positionDTO = positionDao.findByAccountIdAndTicker(account.getId(), marketOrder.getTicker());

        // Vérification de la position disponible
        if (positionDTO.isEmpty() || positionDTO.get().getPosition() < securityOrder.getSize()) {
            securityOrder.setStatus("CANCELED");
            securityOrder.setNotes("Position insuffisante pour vendre");
            throw new IllegalArgumentException("Position insuffisante.");
        }

        // Ajout des fonds après vente
        Quote quote = quoteDao.findById(marketOrder.getTicker())
                .orElseThrow(() -> new IllegalArgumentException("Ticker non trouvé"));
        double totalGain = quote.getBidPrice() * securityOrder.getSize();

        // Mettre à jour le solde du compte après la vente
        account.setAmount(account.getAmount() + totalGain);
        securityOrder.setPrice(quote.getBidPrice());
        securityOrder.setStatus("FILLED");

        // Sauvegarder les modifications du compte
        accountDao.save(account);
    }


    /**
     * Validation de la requête d'ordre de marché.
     */
    private void validateMarketOrder(MarketOrder order) {
        if (order == null || order.getTicker() == null || order.getSize() <= 0 || order.getTraderId() <= 0) {
            throw new IllegalArgumentException("Requête d'ordre de marché invalide.");
        }
    }
}
