package com.demo.firstProject.JPA.Repository;

import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.JPA.Entity.AnimalIllustrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalIllustrationRepositoty extends JpaRepository<AnimalIllustrationEntity, Long> {

    List<AnimalIllustrationEntity> findAllByAnimalFK(AnimalEntity animal);

}
