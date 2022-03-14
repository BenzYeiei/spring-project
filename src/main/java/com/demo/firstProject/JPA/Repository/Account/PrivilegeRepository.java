package com.demo.firstProject.JPA.Repository.Account;

import com.demo.firstProject.JPA.Entity.Account.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {
    boolean existsByPrivilegeName(String privilegeName);
    PrivilegeEntity findByPrivilegeName(String privilegeName);
}
