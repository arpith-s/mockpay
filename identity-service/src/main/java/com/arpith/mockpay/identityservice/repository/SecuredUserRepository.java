package com.arpith.mockpay.identityservice.repository;

import com.arpith.mockpay.identityservice.model.SecuredUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecuredUserRepository extends JpaRepository<SecuredUser, Long> {
    Optional<SecuredUser> findByUsername(String name);

    @Transactional
    @Modifying
    @Query("delete from SecuredUser su WHERE su.username = :username")
    void deleteSecuredUserByUsername(String username);
}
