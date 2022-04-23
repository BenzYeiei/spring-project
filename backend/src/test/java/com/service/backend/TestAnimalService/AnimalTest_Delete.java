package com.service.backend.TestAnimalService;

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
public class AnimalTest_Delete {

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
        animalService.AnimalService_Delete(
                animal.getId(),
                data_of_animalTest.getPrincipal(),
                "execute on unit testing."
        );

        Optional<AnimalEntity> get_for_find = animalRepository.findById(animal.getId());

        // find data from id with argument id in delete method
        Assertions.assertTrue(get_for_find.isEmpty());

    }

}
