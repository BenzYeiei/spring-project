package com.service.backend.JPA.Repository.Account;

import com.service.backend.JPA.Entity.Account.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    boolean existsByRoleName(String roleName);
    RoleEntity findByRoleName(String roleName);
}
