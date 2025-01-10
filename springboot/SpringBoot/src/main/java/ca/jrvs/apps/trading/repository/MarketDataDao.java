package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketDataDao extends JpaRepository<Quote,String> {

    Optional<Quote> findByTicker(String ticker);
}
