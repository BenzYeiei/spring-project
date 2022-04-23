package com.service.backend.TestAnimalService;

import com.service.backend.DTO.Animals.AnimalDTO;
import com.service.backend.JPA.Entity.Account.AccountEntity;
import com.service.backend.JPA.Entity.Animal.AnimalEntity;
import com.service.backend.JPA.Repository.Account.AccountRepository;
import com.service.backend.JPA.Repository.AnimalRepository;
import com.service.backend.Service.Resource.Animals.AnimalService;
import com.service.backend.Service.Resource.Image.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class AnimalTest_Read {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private AccountRepository accountRepository;


    private Data_of_AnimalTest data_of_animalTest = new Data_of_AnimalTest();

    @Test
    public void Test_AnimalService_GetOneById() {

        // find account
        Optional<AccountEntity> account_optional = accountRepository.findById("964c0538-6163-4ea2-b491-b2b46e07e55c");

        // get account data
        AccountEntity account = account_optional.get();

        // find animal
        Optional<AnimalEntity> animal_old_optional = animalRepository.findByNameAndCreatedByUser(data_of_animalTest.getName(), account);

        // get animal data
        AnimalEntity animal_old = animal_old_optional.get();

        AnimalDTO animal = animalService.AnimalService_GetOneById(animal_old.getId());

        Assertions.assertNotNull(animal);

    }

}
