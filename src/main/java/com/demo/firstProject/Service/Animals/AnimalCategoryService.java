package com.demo.firstProject.Service.Animals;

import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.DTO.Animals.AnimalCategoryDTO;
import com.demo.firstProject.JPA.Entity.AnimalCategoryEntity;
import com.demo.firstProject.JPA.Repository.AnimalCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalCategoryService {

    private final AnimalCategoryRepository animalCategoryRepository;

    public AnimalCategoryService(AnimalCategoryRepository animalCategoryRepository) {
        this.animalCategoryRepository = animalCategoryRepository;
    }


    // TODO:: Get List
    public ResponseEntity AnimalCategoryService_GetList() {
        List<AnimalCategoryEntity> animalCategoryResult = animalCategoryRepository.findAll();

        List<AnimalCategoryDTO> animalCategoryDTO = animalCategoryResult.stream().map(AnimalCategoryEntity::SetAnimalCategoryDTO).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(animalCategoryDTO);
    }


    // TODO:: Get One By Id
    public ResponseEntity AnimalCategoryService_GetOne(int id) {
        // TODO: Check exists by id
        boolean isAnimalCategory = animalCategoryRepository.existsById(id);
        if (!isAnimalCategory) {
            throw new BaseException("api.animals.category.get.field.id.not-found", HttpStatus.NOT_FOUND);
        }

        AnimalCategoryEntity animalCategoryResult = animalCategoryRepository.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(animalCategoryResult.SetAnimalCategoryDTO());
    }


    // TODO:: Create Animal Category
    public ResponseEntity AnimalCategoryService_Create(AnimalCategoryEntity requestBody) throws BaseException {
        // TODO: validate
        if (requestBody.getCategoryName() == null) {
            throw new BaseException("api.animals.categories.post.field.categoryName.null", HttpStatus.BAD_REQUEST);
        }

        // TODO: verify
        // TODO: LowerCase String for Save
        requestBody.setCategoryName(requestBody.getCategoryName().toLowerCase());

        // TODO: Check categoryName duplicate
        boolean isExistsName = animalCategoryRepository.existsByCategoryName(requestBody.getCategoryName());
        if (isExistsName) {
            throw new BaseException("api.animals.categories.post.field.categoryName.exists", HttpStatus.BAD_REQUEST);
        }

        // TODO: create Object
        AnimalCategoryEntity animalCategoryEntity = new AnimalCategoryEntity();
        animalCategoryEntity.setCategoryName(requestBody.getCategoryName());

        // TODO: Save()
        AnimalCategoryEntity animalCategoryResultBySave = animalCategoryRepository.save(animalCategoryEntity);
        AnimalCategoryEntity animalCategoryResult = animalCategoryRepository.getById(animalCategoryResultBySave.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(animalCategoryResult.SetAnimalCategoryDTO());
    }


    // TODO:: Update Animal Category
    public ResponseEntity AnimalCategoryService_Update(int id, AnimalCategoryEntity requestBody) {
        // TODO: Check exists by id
        boolean isAnimalCategory = animalCategoryRepository.existsById(id);
        if (!isAnimalCategory) {
            throw new BaseException("api.animals.categories.put.field.id.not-found", HttpStatus.NOT_FOUND);
        }

        // TODO: Check animal category name
        if (requestBody.getCategoryName() == null) {
            throw new BaseException("api.animals.categories.put.field.categoryName.null", HttpStatus.BAD_REQUEST);
        }

        // TODO: LowerCase String for update
        requestBody.setCategoryName(requestBody.getCategoryName().toLowerCase());

        // TODO: Check categoryName duplicate
        boolean isExistsName = animalCategoryRepository.existsByCategoryName(requestBody.getCategoryName());
        if (isExistsName) {
            throw new BaseException("api.animals.categories.post.field.categoryName.exists", HttpStatus.BAD_REQUEST);
        }

        // TODO: Create object
        AnimalCategoryEntity animalCategoryObject = animalCategoryRepository.getById(id);
        animalCategoryObject.setCategoryName(requestBody.getCategoryName());

        // TODO: Update
        AnimalCategoryEntity animalCategoryResult = animalCategoryRepository.save(animalCategoryObject);

        return ResponseEntity.status(HttpStatus.OK).body(animalCategoryResult.SetAnimalCategoryDTO());
    }


    // TODO:: Delete Animal Category
    public ResponseEntity AnimalCategoryService_Delete(int id) {
        // TODO: Check exists by id
        boolean isAnimalCategory = animalCategoryRepository.existsById(id);
        if (!isAnimalCategory) {
            throw new BaseException("api.animals.categories.delete.field.not-found", HttpStatus.NOT_FOUND);
        }

        // TODO: Delete
        animalCategoryRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<AnimalCategoryEntity>());
    }

}
