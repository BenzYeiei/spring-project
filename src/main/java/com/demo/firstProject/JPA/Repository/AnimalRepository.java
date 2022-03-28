package com.demo.firstProject.JPA.Repository;

import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {

    Optional<AnimalEntity> findByNameAndCreatedByUser(String name, AccountEntity account);

}
