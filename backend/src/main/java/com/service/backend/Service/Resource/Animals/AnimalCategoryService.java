package com.service.backend.Service.Resource.Animals;

import com.service.backend.JPA.Entity.Animal.AnimalCategoryEntity;
import com.service.backend.Service.Resource.Image.ImageService;
import com.service.backend.Service.ServiceModel.AnimalService.AnimalCategoryModel_CRUD;
import com.service.backend.Exception.BaseException;
import com.service.backend.JPA.Repository.AnimalCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalCategoryService implements AnimalCategoryModel_CRUD {

    @Autowired
    private AnimalCategoryRepository animalCategoryRepository;

    @Autowired
    private ImageService imageService;


    // TODO:: Get List
    @Override
    public List<AnimalCategoryEntity> AnimalCategoryService_GetList() {
        List<AnimalCategoryEntity> animalCategoryResult = animalCategoryRepository.findAll();
        return animalCategoryResult;
    }


    // TODO:: Get One By Id
    @Override
    public AnimalCategoryEntity AnimalCategoryService_GetOne(int id, String path) {

        // get data
        Optional<AnimalCategoryEntity> op_AnimalCategory = animalCategoryRepository.findById(id);

        // Check exists id
        if (op_AnimalCategory.isEmpty()) {
            throw new BaseException("category not found.", HttpStatus.NOT_FOUND, path);
        }

        // convert Optional to AnimalCategoryEntity
        AnimalCategoryEntity animalCategoryResult = op_AnimalCategory.get();

        return animalCategoryResult;
    }


    // TODO:: Create Animal Category
    @Override
    public AnimalCategoryEntity AnimalCategoryService_Create(String name, String path) throws BaseException {
        // check field categoryName
        if (name == null) {
            throw new BaseException("field categoryName is null.", HttpStatus.BAD_REQUEST, path);
        }

        // check length of name
        if (name.length() > 15) {
            throw new BaseException("field categoryName length size 15.", HttpStatus.BAD_REQUEST, path);
        }

        // LowerCase String for Save
        String lowerCaseName = name.toLowerCase();

        // Check categoryName duplicate
        boolean isExistsName = animalCategoryRepository.existsByCategoryName(lowerCaseName);
        if (isExistsName) {
            throw new BaseException("field categoryName is exists", HttpStatus.BAD_REQUEST, path);
        }

        // create Object
        AnimalCategoryEntity animalCategoryEntity = new AnimalCategoryEntity();

        // set data
        animalCategoryEntity.setCategoryName(lowerCaseName);

        // create variable for get result data
        AnimalCategoryEntity getData;

        // Save()
        try {
            getData = animalCategoryRepository.save(animalCategoryEntity);
        } catch (RuntimeException e) {
            throw new BaseException("Server message -> " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // map animalCategory to dto
//        AnimalCategoryDTO animalCategoryResult = getData.setAnimalCategoryDTO(imageService.getDomainUrl());

        return getData;
    }


    // TODO:: Update Animal Category
    @Override
    public AnimalCategoryEntity AnimalCategoryService_Update(int id, String categoryName, String path) {
        // Check exists by id
        boolean isAnimalCategory = animalCategoryRepository.existsById(id);
        if (!isAnimalCategory) {
            throw new BaseException("parameter id not found.", HttpStatus.NOT_FOUND, path);
        }

        // Check animal category name
        if (categoryName == null) {
            throw new BaseException("field categoryName is null.", HttpStatus.BAD_REQUEST, path);
        }

        // LowerCase String for update
        String lowerCaseName = categoryName.toLowerCase();

        // Check categoryName duplicate
        boolean isExistsName = animalCategoryRepository.existsByCategoryName(lowerCaseName);
        if (isExistsName) {
            throw new BaseException("field categoryName is exists.", HttpStatus.BAD_REQUEST, path);
        }

        // Create object
        AnimalCategoryEntity animalCategoryObject = animalCategoryRepository.getById(id);

        // set data
        animalCategoryObject.setCategoryName(lowerCaseName);

        // create variable for get result data
        AnimalCategoryEntity getResult;

        // Update
        try {
            getResult = animalCategoryRepository.save(animalCategoryObject);
        } catch (Exception e) {
            throw new BaseException("Server message ->" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        return getResult;
    }


    // TODO:: Delete Animal Category
    @Override
    public void AnimalCategoryService_Delete(int id, String path) {
        // Check exists by id
        boolean isAnimalCategory = animalCategoryRepository.existsById(id);
        if (!isAnimalCategory) {
            throw new BaseException("parameter not found.", HttpStatus.NOT_FOUND, path);
        }

        // Delete
        try {
            animalCategoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException("Server message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }
    }

}
