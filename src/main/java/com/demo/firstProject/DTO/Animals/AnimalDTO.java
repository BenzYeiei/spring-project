package com.demo.firstProject.DTO.Animals;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnimalDTO implements Serializable {
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
