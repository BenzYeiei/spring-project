package com.service.backend.DTO.Animals;

import lombok.Data;

import java.util.List;

@Data
public class AnimalCategoryDTO {

    private int id;

    private String categoryName;

    private List<AnimalDTO> animalListByCategory;
}
