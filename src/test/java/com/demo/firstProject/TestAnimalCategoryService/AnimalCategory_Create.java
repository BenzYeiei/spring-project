package com.demo.firstProject.TestAnimalCategoryService;

import com.demo.firstProject.JPA.Entity.AnimalCategoryEntity;
import com.demo.firstProject.Service.Resource.Animals.AnimalCategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;

@SpringBootTest
public class AnimalCategory_Create {

    @Autowired
    private AnimalCategoryService animalCategoryService;

    interface dataTesting {
        String categoryName = "cat";

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "964c0538-6163-4ea2-b491-b2b46e07e55c";
            }
        };
    }

    @Test
    void Test_AnimalCategoryService_Create() {

        AnimalCategoryEntity animalCategory = animalCategoryService.AnimalCategoryService_Create(
                dataTesting.categoryName,
                "execute on unit testing."
        );

        Assertions.assertNotNull(animalCategory.getCategoryName());

        Assertions.assertEquals(animalCategory.getCategoryName(), dataTesting.categoryName);

    }

}
