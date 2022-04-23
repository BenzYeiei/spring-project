package com.service.backend.JPA.Repository.Account;


import com.service.backend.JPA.Entity.Account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    boolean existsByUsername(String username);
    Optional<AccountEntity> findByUsername(String username);
}
