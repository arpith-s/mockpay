package com.arpith.mockpay.walletservice.repository;

import com.arpith.mockpay.walletservice.model.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByWalletId(String walletId);

    @Modifying
    @Transactional
    @Query("delete from Wallet w where w.walletId = :walletId")
    void deleteByWalletId(String walletId);

    @Modifying
    @Transactional
    @Query("update Wallet w set w.balance = w.balance + :amount where w.walletId = :walletId")
    void creditWallet(String walletId, Long amount);

    @Modifying
    @Transactional
    @Query("update Wallet w set w.balance = w.balance - :amount where w.walletId = :walletId")
    void debitWallet(String walletId, Long amount);
}
