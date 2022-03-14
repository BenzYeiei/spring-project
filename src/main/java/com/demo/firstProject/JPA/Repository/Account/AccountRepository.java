package com.demo.firstProject.JPA.Repository.Account;


import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    boolean existsByEmail(String email);
    AccountEntity findByEmail(String email);
}
