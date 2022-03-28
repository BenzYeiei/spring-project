package com.demo.firstProject.TestAnimalService;

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

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
public class AnimalTest_Update {

    @Autowired
    private AnimalsService animalsService;

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ImageService imageService;


    // data for testing, get image
    private Data_of_AnimalTest data_of_animalTest = new Data_of_AnimalTest();

    // get text
    interface date_test_update {
        int quantity = 555;
        boolean statusState = true;
    }

    @Test
    void Test_AnimalService_Update() {
        // find account
        Optional<AccountEntity> account_optional = accountRepository.findById("964c0538-6163-4ea2-b491-b2b46e07e55c");

        // get account data
        AccountEntity account = account_optional.get();

        // find animal
        Optional<AnimalEntity> animal_old_optional = animalRepository.findByNameAndCreatedByUser(data_of_animalTest.getName(), account);

        // get animal data
        AnimalEntity animal_old = animal_old_optional.get();


        AnimalEntity animal = animalsService.AnimalService_Update(
                animal_old.getId(),
                animal_old.getName(),
                animal_old.getAnimalCategoryFK().getCategoryName(),
                date_test_update.quantity, // TODO :: update data
                date_test_update.statusState, // TODO :: update data
                data_of_animalTest.getImageProfile_for_update(),  // TODO :: update data
                new ArrayList<>(),
                "execute on unit testing.",
                data_of_animalTest.getPrincipal()
        );

        // return value not null
        Assertions.assertNotNull(animal);

        //
        Assertions.assertEquals(date_test_update.quantity, animal.getQuantity());
        Assertions.assertEquals(date_test_update.statusState, animal.isStatusState());


    }

}
