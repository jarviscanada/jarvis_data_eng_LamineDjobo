package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Trader;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.service.TraderAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/trader")
public class TraderAccountController {

    private static final Logger logger = LoggerFactory.getLogger(TraderAccountController.class);
    private final TraderAccountService traderAccountService;

    @Autowired
    public TraderAccountController(TraderAccountService traderAccountService) {
        this.traderAccountService = traderAccountService;
    }

    /**
     * Créer un trader via un DTO (objet `Trader`).
     * URL : POST /trader/
     */
    @PostMapping("/")
    public ResponseEntity<TraderAccountView> createTrader(@RequestBody Trader trader) {
        logger.info("Requête reçue pour créer un trader via DTO : {}", trader);
        try {
            TraderAccountView traderAccountView = traderAccountService.createTraderAndAccount(trader);
            return new ResponseEntity<>(traderAccountView, HttpStatus.CREATED);  // 201 CREATED
        } catch (IllegalArgumentException e) {
            logger.error("Erreur de validation des données du trader : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Créer un trader via les paramètres.
     * URL : POST /trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}
     */
    @PostMapping("/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}")
    public ResponseEntity<TraderAccountView> createTrader(
            @PathVariable String firstname,
            @PathVariable String lastname,
            @PathVariable String dob,
            @PathVariable String country,
            @PathVariable String email
    ) {
        logger.info("Requête reçue pour créer un trader via paramètres.");
        try {
            LocalDate dateOfBirth = LocalDate.parse(dob, DateTimeFormatter.ISO_DATE);  // Format : "yyyy-MM-dd"
            Trader trader = new Trader();
            trader.setFirstName(firstname);
            trader.setLastName(lastname);
            trader.setDob(dateOfBirth);
            trader.setCountry(country);
            trader.setEmail(email);

            TraderAccountView traderAccountView = traderAccountService.createTraderAndAccount(trader);
            return new ResponseEntity<>(traderAccountView, HttpStatus.CREATED);  // 201 CREATED
        } catch (DateTimeParseException e) {
            logger.error("Erreur de format de date : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (IllegalArgumentException e) {
            logger.error("Erreur de validation des données du trader : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Supprimer un trader par ID.
     * URL : DELETE /trader/traderId/{traderId}
     */
    @DeleteMapping("/traderId/{traderId}")
    public ResponseEntity<String> deleteTrader(@PathVariable Integer traderId) {
        logger.info("Requête reçue pour supprimer le trader avec l'ID : {}", traderId);
        try {
            traderAccountService.deleteTraderById(traderId);
            return new ResponseEntity<>("Trader supprimé avec succès.", HttpStatus.OK);  // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Erreur : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>("Erreur interne lors de la suppression.", HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Dépôt de fonds dans le compte d'un trader.
     * URL : PUT /trader/deposit/traderId/{traderId}/amount/{amount}
     */
    @PutMapping("/deposit/traderId/{traderId}/amount/{amount}")
    public ResponseEntity<Account> depositFund(@PathVariable Integer traderId, @PathVariable Double amount) {
        logger.info("Requête reçue pour déposer des fonds pour le trader ID : {}", traderId);
        try {
            Account account = traderAccountService.deposit(traderId, amount);
            return new ResponseEntity<>(account, HttpStatus.OK);  // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Erreur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }

    /**
     * Retrait de fonds dans le compte d'un trader.
     * URL : PUT /trader/withdraw/traderId/{traderId}/amount/{amount}
     */
    @PutMapping("/withdraw/traderId/{traderId}/amount/{amount}")
    public ResponseEntity<Account> withdrawFund(@PathVariable Integer traderId, @PathVariable Double amount) {
        logger.info("Requête reçue pour retirer des fonds pour le trader ID : {}", traderId);
        try {
            Account account = traderAccountService.withdraw(traderId, amount);
            return new ResponseEntity<>(account, HttpStatus.OK);  // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Erreur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }
}
