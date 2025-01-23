package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.dto.PositionDTO;
import ca.jrvs.apps.trading.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionDao extends JpaRepository<Position, Integer> {


    @Override
    @Deprecated
    default void deleteById(Integer integer) {
        throw new UnsupportedOperationException("Delete operation is not supported for Position view");
    }


    @Query("SELECT new ca.jrvs.apps.trading.dto.PositionDTO(p.accountId, p.ticker, p.position) FROM Position p")
    List<PositionDTO> findAllDTO();

    @Query("SELECT new ca.jrvs.apps.trading.dto.PositionDTO(p.accountId, p.ticker, p.position) FROM Position p WHERE p.accountId = :accountId AND p.ticker = :ticker")
    Optional<PositionDTO> findByAccountIdAndTicker(@Param("accountId") Integer accountId, @Param("ticker") String ticker);
}
