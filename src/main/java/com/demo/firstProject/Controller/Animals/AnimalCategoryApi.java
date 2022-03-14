package com.demo.firstProject.Controller.Animals;

import com.demo.firstProject.JPA.Entity.AnimalCategoryEntity;
import com.demo.firstProject.Service.Resource.Animals.AnimalCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animals/categories")
public class AnimalCategoryApi {

    private final AnimalCategoryService animalCategoryService;

    public AnimalCategoryApi(AnimalCategoryService animalCategoryService) {
        this.animalCategoryService = animalCategoryService;
    }

    @GetMapping("")
    public ResponseEntity AnimalCategory_GetList() {
        return animalCategoryService.AnimalCategoryService_GetList();
    }


    @GetMapping("/{id}")
    public ResponseEntity AnimalCategory_GetOneById(@PathVariable int id) {
        return animalCategoryService.AnimalCategoryService_GetOne(id);
    }


    @PostMapping("")
    public ResponseEntity AnimalCategory_Create(@RequestBody AnimalCategoryEntity requestBody) {
        return animalCategoryService.AnimalCategoryService_Create(requestBody);
    }


    @PutMapping("/{id}")
    public ResponseEntity AnimalCategory_Update(@PathVariable int id, @RequestBody AnimalCategoryEntity requestBody) {
        return animalCategoryService.AnimalCategoryService_Update(id, requestBody);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity AnimalCategory_Delete(@PathVariable int id) {
        return animalCategoryService.AnimalCategoryService_Delete(id);
    }

}
