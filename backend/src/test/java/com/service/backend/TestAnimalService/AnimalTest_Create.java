package com.service.backend.TestAnimalService;

import com.service.backend.DTO.Animals.AnimalDTO;
import com.service.backend.Service.Resource.Animals.AnimalService;
import com.service.backend.Service.Resource.Image.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnimalTest_Create {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private ImageService imageService;

    private Data_of_AnimalTest data_of_animalTest = new Data_of_AnimalTest();


    @Test
    void Test_AnimalService_Create() {

        AnimalDTO animalResult = animalService.AnimalService_Create(
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
        Assertions.assertEquals(data_of_animalTest.getAnimalCategory(), animalResult.getAnimalCategoryFK());

        //  Quantity must equal
        Assertions.assertEquals(data_of_animalTest.getQuantity(), animalResult.getQuantity());


    }

}
