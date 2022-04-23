package com.service.backend.JPA.Repository;

import com.service.backend.JPA.Entity.Animal.AnimalEntity;
import com.service.backend.JPA.Entity.Account.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {

    Optional<AnimalEntity> findByNameAndCreatedByUser(String name, AccountEntity account);

    Page<AnimalEntity> findAllByOrderByCreateTimeDesc(Pageable pageable);

    Page<AnimalEntity> findAll(Pageable pageable);

}
