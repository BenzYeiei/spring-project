package com.demo.firstProject.JPA.Repository;

import com.demo.firstProject.JPA.Entity.Animal.AnimalCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalCategoryRepository extends JpaRepository<AnimalCategoryEntity, Integer> {

    boolean existsByCategoryName(String categoryName);

    Optional<AnimalCategoryEntity> findByCategoryName(String categoryName);

}
