package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testDeleteAccountByTraderId() {
        accountService.deleteAccountByTraderId(1);
        assertThrows(IllegalArgumentException.class, () -> accountService.deleteAccountByTraderId(2));
    }
}

