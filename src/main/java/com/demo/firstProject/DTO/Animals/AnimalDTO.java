package com.demo.firstProject.DTO.Animals;

import com.demo.firstProject.JPA.Entity.AnimalIllustrationEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnimalDTO {
    private long id;

    private String name;

    private String imageProfile;

    private long quantity;

    private boolean statusState = false;

    private LocalDateTime createTime;

    private String animalCategoryFK;

    private List<AnimalIllustrationDTO> animalIllustration;

    private String CreatedByUser;

}
