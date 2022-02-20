package com.demo.firstProject.JPA.Repository;

import com.demo.firstProject.JPA.Entity.AnimalIllustrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalIllustrationRepositoty extends JpaRepository<AnimalIllustrationEntity, Long> {
}
