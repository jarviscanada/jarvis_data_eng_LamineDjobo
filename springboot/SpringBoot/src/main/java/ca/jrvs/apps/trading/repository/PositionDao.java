package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionDao extends JpaRepository<Position, Integer> {

    @Override
    @Deprecated
    default <S extends Position> S save(S entity) {
        throw new UnsupportedOperationException("Save operation is not supported for Position view");
    }

    @Override
    @Deprecated
    default void deleteById(Integer integer) {
        throw new UnsupportedOperationException("Delete operation is not supported for Position view");
    }

    @Query("SELECT p FROM Position p WHERE p.account.id = :accountId AND p.quote.ticker = :ticker")
    Optional<Position> findByAccountIdAndTicker(@Param("accountId") Integer accountId, @Param("ticker") String ticker);

}
