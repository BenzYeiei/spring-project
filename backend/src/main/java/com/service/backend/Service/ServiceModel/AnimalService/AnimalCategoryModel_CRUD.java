package com.service.backend.Service.ServiceModel.AnimalService;

import com.service.backend.JPA.Entity.Animal.AnimalCategoryEntity;

import java.util.List;

public interface AnimalCategoryModel_CRUD {

    List<AnimalCategoryEntity> AnimalCategoryService_GetList();

    AnimalCategoryEntity AnimalCategoryService_GetOne(int id, String path);

    AnimalCategoryEntity AnimalCategoryService_Create(String name, String path);

    AnimalCategoryEntity AnimalCategoryService_Update(int id, String categoryName, String path);

    void AnimalCategoryService_Delete(int id, String path);

}
