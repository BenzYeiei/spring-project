package com.demo.firstProject.JPA.Repository;

import com.demo.firstProject.JPA.Entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {

}
