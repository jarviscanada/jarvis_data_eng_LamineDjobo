package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.model.Trader;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.repository.QuoteDao;
import ca.jrvs.apps.trading.repository.SecurityOrderDao;
import ca.jrvs.apps.trading.repository.TraderDao;
import ca.jrvs.apps.trading.service.TraderAccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TraderServiceIntTest {

    @Autowired
    private TraderAccountService traderService;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private AccountJpaRepository accountDao;

    @Autowired
    private SecurityOrderDao securityOrderDao;

    private Trader testTrader;

    @Autowired
    private QuoteDao quoteDao;

    @BeforeEach
    public void setup() {
        // Clean up dependent tables in the correct order
        securityOrderDao.deleteAll(); // Clean security orders first
        accountDao.deleteAll();       // Then accounts
        traderDao.deleteAll();        // Then traders
        quoteDao.deleteAll();         // Clean quotes last

        // Insert required quotes
        Quote quote1 = new Quote("AAPL", 150.0, 144, 151.0, 100, 200);
        Quote quote2 = new Quote("MSFT", 250.0, 219, 251.0, 300, 400);
        Quote quote3 = new Quote("GOOGL", 1000.0, 499, 1001.0, 10, 20);
        quoteDao.save(quote1);
        quoteDao.save(quote2);
        quoteDao.save(quote3);

        // Insert a trader
        Trader trader1 = new Trader();
        trader1.setFirstName("John");
        trader1.setLastName("Doe");
        trader1.setCountry("USA");
        trader1.setDob(LocalDate.of(1990, 1, 1));
        trader1.setEmail("john.doe@example.com");
        traderDao.save(trader1);

        // Insert an account associated with the trader
        Account account1 = new Account();
        account1.setTrader(trader1);
        account1.setAmount(1000.0);
        accountDao.save(account1);
    }


    @Test
    public void createTraderAndAccount_success() {
        TraderAccountView traderAccountView = traderService.createTraderAndAccount(testTrader);

        assertNotNull(traderAccountView.getTrader());
        assertNotNull(traderAccountView.getAccount());
        assertEquals(0.0, traderAccountView.getAccount().getAmount());
        assertEquals(testTrader.getFirstName(), traderAccountView.getTrader().getFirstName());
    }



    @Test
    public void createTraderAndAccount_invalidTrader_throwsException() {
        // Trader with missing required fields
        Trader invalidTrader = new Trader();
        invalidTrader.setFirstName("");  // Missing first name
        invalidTrader.setLastName("");    // Blank last name
        invalidTrader.setEmail(null);     // Missing email
        invalidTrader.setDob(null);       // Missing date of birth
        invalidTrader.setCountry(null);   // Missing country

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traderService.createTraderAndAccount(invalidTrader);
        });

        assertTrue(exception.getMessage().contains("Trader fields must not be null or empty."),
                "Expected validation exception for invalid trader");
    }



    @Test
    public void testDeleteTraderById() {
        Trader trader = new Trader();
        trader.setFirstName("John");
        trader.setLastName("Doe");
        trader.setCountry("USA");
        trader.setEmail("john.doe@example.com");
        trader.setDob(LocalDate.of(1985, 1, 1));
        traderDao.save(trader);

        Account account = new Account();
        account.setTrader(trader);
        account.setAmount(0.0);
        accountDao.save(account);

        // Test deletion
        traderService.deleteTraderById(trader.getId());

        Optional<Trader> deletedTrader = traderDao.findById(trader.getId());
        assertFalse(deletedTrader.isPresent(), "Trader should be deleted");

        Optional<Account> deletedAccount = accountDao.getAccountByTraderId(trader.getId());
        assertFalse(deletedAccount.isPresent(), "Associated account should be deleted");
    }


    @Test
    public void deleteTraderById_withOpenPosition_throwsException() {
        TraderAccountView traderAccountView = traderService.createTraderAndAccount(testTrader);
        Integer traderId = traderAccountView.getTrader().getId();
        Account account = traderAccountView.getAccount();



        // Simulate open position
        securityOrderDao.createDummyOrderForTest(
                account.getId(),
                "FILLED",     // Simulates a filled order
                "AAPL",       // Ticker
                100,          // Size
                150.0,        // Price
                "Test Order"  // Notes
        );
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traderService.deleteTraderById(traderId);
        });
        assertTrue(exception.getMessage().contains("Cannot delete trader with open positions"));
    }

    @Test
    public void deposit_success() {
        TraderAccountView traderAccountView = traderService.createTraderAndAccount(testTrader);
        Integer traderId = traderAccountView.getTrader().getId();
        double depositAmount = 500.0;

        Account updatedAccount = traderService.deposit(traderId, depositAmount);
        assertEquals(depositAmount, updatedAccount.getAmount());
    }

    @Test
    public void deposit_invalidAmount_throwsException() {
        TraderAccountView traderAccountView = traderService.createTraderAndAccount(testTrader);
        Integer traderId = traderAccountView.getTrader().getId();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traderService.deposit(traderId, -100.0);  // Invalid deposit amount
        });
        assertTrue(exception.getMessage().contains("fund must be greater than 0"));
    }

    @Test
    public void withdraw_success() {
        TraderAccountView traderAccountView = traderService.createTraderAndAccount(testTrader);
        Integer traderId = traderAccountView.getTrader().getId();

        traderService.deposit(traderId, 1000.0);  // Add initial balance
        Account updatedAccount = traderService.withdraw(traderId, 500.0);

        assertEquals(500.0, updatedAccount.getAmount());
    }

    @Test
    public void withdraw_insufficientFunds_throwsException() {
        TraderAccountView traderAccountView = traderService.createTraderAndAccount(testTrader);
        Integer traderId = traderAccountView.getTrader().getId();

        traderService.deposit(traderId, 200.0);  // Add limited funds

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traderService.withdraw(traderId, 500.0);  // Try withdrawing more than balance
        });
        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }

    @Test
    public void withdraw_invalidAmount_throwsException() {
        TraderAccountView traderAccountView = traderService.createTraderAndAccount(testTrader);
        Integer traderId = traderAccountView.getTrader().getId();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traderService.withdraw(traderId, -100.0);
        });
        assertTrue(exception.getMessage().contains("fund must be greater than 0"));
    }

    @AfterEach
    public void cleanup() {
        traderDao.deleteAll();
        accountDao.deleteAll();
        securityOrderDao.deleteAll();
    }
}
