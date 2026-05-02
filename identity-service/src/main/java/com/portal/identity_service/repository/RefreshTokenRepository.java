package com.portal.identity_service.repository;

import com.portal.identity_service.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    List<RefreshToken> findByUsernameAndRevokedFalse(String username);
    List<RefreshToken> findByUsernameAndRevokedFalseOrderByExpiryTimeAsc(String username);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.username = :username")
    void revokeAll(@Param("username") String username);
}
