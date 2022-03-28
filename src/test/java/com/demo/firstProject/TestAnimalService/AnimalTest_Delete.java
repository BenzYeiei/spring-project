package com.demo.firstProject.TestAnimalService;

import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.JPA.Repository.Account.AccountRepository;
import com.demo.firstProject.JPA.Repository.AnimalRepository;
import com.demo.firstProject.Service.Resource.Animals.AnimalsService;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import org.checkerframework.checker.guieffect.qual.AlwaysSafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class AnimalTest_Delete {

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
    void Test_AnimalService_Delete() {

        // get account
        Optional<AccountEntity> accountResult = accountRepository.findById(data_of_animalTest.getPrincipal().getName());

        // convert account from optional
        AccountEntity account = accountResult.get();

        // get animal data from name and account
        Optional<AnimalEntity> animalResult = animalRepository.findByNameAndCreatedByUser(
                data_of_animalTest.getName(),
                account
        );

        if (animalResult.isEmpty()) {
            System.out.println("---------- animal data for test null or empty. ----------");
            return;
        }

        // convert to animal from optional
        AnimalEntity animal = animalResult.get();

        // test method delete
        animalsService.AnimalService_Delete(
                animal.getId(),
                data_of_animalTest.getPrincipal(),
                "execute on unit testing."
        );

        Optional<AnimalEntity> get_for_find = animalRepository.findById(animal.getId());

        // find data from id with argument id in delete method
        Assertions.assertTrue(get_for_find.isEmpty());

    }

}
