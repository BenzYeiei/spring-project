package com.demo.firstProject.JPA.Repository;

import com.demo.firstProject.JPA.Entity.AnimalCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalCategoryRepository extends JpaRepository<AnimalCategoryEntity, Integer> {

    boolean existsByCategoryName(String categoryName);

    AnimalCategoryEntity findByCategoryName(String categoryName);

}
