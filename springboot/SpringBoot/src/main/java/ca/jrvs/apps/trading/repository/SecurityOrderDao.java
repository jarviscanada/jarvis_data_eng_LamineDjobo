package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.SecurityOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityOrderDao extends JpaRepository <SecurityOrder,Integer> {
    List<SecurityOrder> findByAccountId(Integer accountId);

}
