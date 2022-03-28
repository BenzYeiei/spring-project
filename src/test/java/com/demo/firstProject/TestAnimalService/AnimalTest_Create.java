package com.demo.firstProject.TestAnimalService;

import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.Service.Resource.Animals.AnimalsService;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnimalTest_Create {

    @Autowired
    private AnimalsService animalsService;

    @Autowired
    private ImageService imageService;

    private Data_of_AnimalTest data_of_animalTest = new Data_of_AnimalTest();


    @Test
    void Test_AnimalService_Create() {

        AnimalEntity animalResult = animalsService.AnimalService_Create(
                data_of_animalTest.getName(),
                data_of_animalTest.getAnimalCategory(),
                data_of_animalTest.getQuantity(),
                data_of_animalTest.getImageProfile(),
                data_of_animalTest.getIllustrationFiles(),
                data_of_animalTest.getPath(),
                data_of_animalTest.getPrincipal()
        );



        // animal object not null
        Assertions.assertNotNull(animalResult);

        // id of animal not null
        Assertions.assertNotNull(animalResult.getId());

        //  name must equal
        Assertions.assertEquals(data_of_animalTest.getName(), animalResult.getName());

        //  Category must equal
        Assertions.assertEquals(data_of_animalTest.getAnimalCategory(), animalResult.getAnimalCategoryFK().getCategoryName());

        //  Quantity must equal
        Assertions.assertEquals(data_of_animalTest.getQuantity(), animalResult.getQuantity());


    }

}
