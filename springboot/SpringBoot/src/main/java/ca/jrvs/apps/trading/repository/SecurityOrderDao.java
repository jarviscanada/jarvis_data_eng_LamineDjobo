package ca.jrvs.apps.trading.repository;

import ca.jrvs.apps.trading.model.SecurityOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SecurityOrderDao extends JpaRepository <SecurityOrder,Integer> {
    List<SecurityOrder> findByAccountId(Integer accountId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO security_order (account_id, status, ticker, size, price, notes) VALUES (:accountId, :status, :ticker, :size, :price, :notes)", nativeQuery = true)
    void createDummyOrderForTest(@Param("accountId") Integer accountId,
                                 @Param("status") String status,
                                 @Param("ticker") String ticker,
                                 @Param("size") Integer size,
                                 @Param("price") Double price,
                                 @Param("notes") String notes);

    @Modifying
    @Transactional
    @Query("DELETE FROM SecurityOrder s WHERE s.account.id = :accountId")
    void deleteAllByAccountId(@Param("accountId") Integer accountId);

}
