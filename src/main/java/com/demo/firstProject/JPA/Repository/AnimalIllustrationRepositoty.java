package com.demo.firstProject.JPA.Repository;

import com.demo.firstProject.JPA.Entity.Animal.AnimalEntity;
import com.demo.firstProject.JPA.Entity.Animal.AnimalIllustrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalIllustrationRepositoty extends JpaRepository<AnimalIllustrationEntity, Long> {

    List<AnimalIllustrationEntity> findAllByAnimalFK(AnimalEntity animal);

}
