package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Trader;
import ca.jrvs.apps.trading.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // Utilise la vraie base de test
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Test
    @Transactional
    public void testCreateAccount() {
        //Trader trader = new Trader("Jane", "Smith", LocalDate.of(1995, 5, 15), "USA", "jane.smith@email.com");
        //trader.setId(1L);
        Account account = new Account();
        //account.setTraderId(1);
        account.setAmount(5000.00);

        //Account createdAccount = accountService.createAccount(account);
       // assertNotNull(createdAccount);
       // assertEquals(5000.00, createdAccount.getAmount(), 0.001);
    }
}

