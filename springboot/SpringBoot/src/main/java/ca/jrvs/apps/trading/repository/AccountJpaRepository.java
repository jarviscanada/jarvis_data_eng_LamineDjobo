package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account,Integer> {
    Optional<Account> getAccountByTraderId(Integer traderId);
}
