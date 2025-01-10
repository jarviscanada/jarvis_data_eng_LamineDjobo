package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionDao extends JpaRepository<Position,Integer> {
}
