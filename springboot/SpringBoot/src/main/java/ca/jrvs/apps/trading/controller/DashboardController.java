package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.PortfolioView;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Afficher le profil d'un trader par son ID
     */
    @GetMapping("/profile/traderId/{traderId}")
    public ResponseEntity<TraderAccountView> getAccount(@PathVariable Integer traderId) {
        try {
            logger.info("Requête pour afficher le profil du trader ID: {}", traderId);
            TraderAccountView traderAccountView = dashboardService.getTraderAccount(traderId);
            return new ResponseEntity<>(traderAccountView, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Erreur interne", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Afficher le portefeuille d'un trader par son ID
     */
    @GetMapping("/portfolio/traderId/{traderId}")
    public ResponseEntity<PortfolioView> getPortfolioView(@PathVariable Integer traderId) {
        try {
            logger.info("Requête pour afficher le portefeuille du trader ID: {}", traderId);
            PortfolioView portfolioView = dashboardService.getProfileViewByTraderId(traderId);
            return new ResponseEntity<>(portfolioView, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Erreur interne", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

