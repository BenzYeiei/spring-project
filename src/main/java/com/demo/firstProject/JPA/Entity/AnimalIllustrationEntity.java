package com.demo.firstProject.JPA.Entity;

import com.demo.firstProject.Configuration.Domain;
import com.demo.firstProject.DTO.Animals.AnimalIllustrationDTO;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
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


    public AnimalIllustrationDTO SetAnimalIllustration_dto(String domain) {
        AnimalIllustrationDTO animalIllustrationDTO = new AnimalIllustrationDTO();

        animalIllustrationDTO.setId(this.getId());
        animalIllustrationDTO.setName(domain + "/api/images/" + this.getName());

        return animalIllustrationDTO;
    }

}
