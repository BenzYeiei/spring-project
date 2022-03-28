package com.demo.firstProject.Service.ServiceModel.AnimalService;

import com.demo.firstProject.JPA.Entity.AnimalCategoryEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnimalCategoryModel_CRUD {

    List<AnimalCategoryEntity> AnimalCategoryService_GetList();

    AnimalCategoryEntity AnimalCategoryService_GetOne(int id, String path);

    AnimalCategoryEntity AnimalCategoryService_Create(String name, String path);

    AnimalCategoryEntity AnimalCategoryService_Update(int id, String categoryName, String path);

    void AnimalCategoryService_Delete(int id, String path);

}
