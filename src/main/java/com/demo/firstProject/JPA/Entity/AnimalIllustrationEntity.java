package com.demo.firstProject.JPA.Entity;

import com.demo.firstProject.Config.Domain;
import com.demo.firstProject.DTO.Animals.AnimalIllustrationDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "Animals_Illustration_TB")
public class AnimalIllustrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animals_illustration_id")
    private long id;

    @Column(name = "animal_illustration_name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "animal_illustration_fk", nullable = false)
    private AnimalEntity animalFK;


    public AnimalIllustrationDTO SetAnimalIllustration_dto() {
        AnimalIllustrationDTO animalIllustrationDTO = new AnimalIllustrationDTO();

        animalIllustrationDTO.setId(this.getId());
        animalIllustrationDTO.setName(Domain.domainUrl + "/api/images/" + this.getName());

        return animalIllustrationDTO;
    }

}
