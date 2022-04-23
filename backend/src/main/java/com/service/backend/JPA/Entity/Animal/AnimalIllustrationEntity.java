package com.service.backend.JPA.Entity.Animal;

import com.service.backend.DTO.Animals.AnimalIllustrationDTO;
import com.service.backend.JPA.Entity.Account.AccountEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "animal_illustration_tb")
public class AnimalIllustrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animals_illustration_id")
    private long id;

    @Column(name = "animal_illustration_name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "animal_foreign_key", nullable = false)
    private AnimalEntity animalFK;

    @ManyToOne
    @JoinColumn(name = "account_foreign_key", nullable = false)
    private AccountEntity accountFK;


    public AnimalIllustrationDTO SetAnimalIllustration_dto(String fetchIllustration) {
        AnimalIllustrationDTO animalIllustrationDTO = new AnimalIllustrationDTO();

        animalIllustrationDTO.setId(this.getId());
        animalIllustrationDTO.setName(fetchIllustration + this.getName());

        return animalIllustrationDTO;
    }

}
