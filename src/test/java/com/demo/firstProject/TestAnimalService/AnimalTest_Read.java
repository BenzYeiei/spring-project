package com.demo.firstProject.TestAnimalService;

import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.JPA.Repository.Account.AccountRepository;
import com.demo.firstProject.JPA.Repository.AnimalRepository;
import com.demo.firstProject.Service.Resource.Animals.AnimalsService;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AnimalTest_Read {

    @Autowired
    private AnimalsService animalsService;

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

        AnimalEntity animal = animalsService.AnimalService_GetOneById(animal_old.getId());

        Assertions.assertNotNull(animal);

    }

}