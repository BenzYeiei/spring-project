package com.demo.firstProject.JPA.Repository.Account;

import com.demo.firstProject.JPA.Entity.Account.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    boolean existsByRoleName(String roleName);
    RoleEntity findByRoleName(String roleName);
}
