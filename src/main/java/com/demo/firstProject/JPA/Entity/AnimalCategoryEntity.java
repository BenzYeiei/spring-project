package com.demo.firstProject.JPA.Entity;

import com.demo.firstProject.DTO.Animals.AnimalCategoryDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity(name = "animal_category_tb")
public class AnimalCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_category_id")
    private int id;

    @Column(name = "animal_category_name", nullable = false, unique = true, length = 25)
    private String categoryName;

    @OneToMany(mappedBy = "animalCategoryFK", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnimalEntity> animalListByCategory = new ArrayList<>();


    public AnimalCategoryDTO SetAnimalCategoryDTO() {
        AnimalCategoryDTO animalCategoryDTO = new AnimalCategoryDTO();
        animalCategoryDTO.setId(this.id);
        animalCategoryDTO.setCategoryName(this.getCategoryName());
        animalCategoryDTO.setAnimalListByCategory(this.getAnimalListByCategory().stream().map(AnimalEntity::SetAnimal_dto).collect(Collectors.toList()));
        return animalCategoryDTO;
    }

}
