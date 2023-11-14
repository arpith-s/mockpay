package com.arpith.mockpay.transactionservice.repository;

import com.arpith.mockpay.transactionservice.enumeration.Status;
import com.arpith.mockpay.transactionservice.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySenderId(String senderId);

    @Transactional
    @Modifying
    @Query("update Transaction t set t.status = :status where t.transactionId = :id")
    void updateTransaction(@Param("id") String transactionId, @Param("status") Status status);
}

