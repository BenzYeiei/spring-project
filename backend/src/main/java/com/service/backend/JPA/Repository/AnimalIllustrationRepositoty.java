package com.service.backend.JPA.Repository;

import com.service.backend.JPA.Entity.Animal.AnimalEntity;
import com.service.backend.JPA.Entity.Animal.AnimalIllustrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalIllustrationRepositoty extends JpaRepository<AnimalIllustrationEntity, Long> {

    List<AnimalIllustrationEntity> findAllByAnimalFK(AnimalEntity animal);

    Optional<AnimalIllustrationEntity> findByAnimalFK(AnimalEntity animal);

}
