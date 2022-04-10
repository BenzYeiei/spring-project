package com.demo.firstProject.Controller.Animals;

import com.demo.firstProject.DTO.Animals.AnimalCategoryDTO;
import com.demo.firstProject.JPA.Entity.Animal.AnimalCategoryEntity;
import com.demo.firstProject.Service.Resource.Animals.AnimalCategoryService;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animals/v1/categories")
public class AnimalCategoryController {


    private final AnimalCategoryService animalCategoryService;
    private final ImageService imageService;

    public AnimalCategoryController(AnimalCategoryService animalCategoryService, ImageService imageService) {
        this.animalCategoryService = animalCategoryService;
        this.imageService = imageService;
    }


    @GetMapping("")
    public ResponseEntity AnimalCategory_GetList() {

        List<AnimalCategoryEntity> animalCategoryEntityList = animalCategoryService.AnimalCategoryService_GetList();

        List<AnimalCategoryDTO> animalCategoryDTO = animalCategoryEntityList.stream().map(
                result -> result.setAnimalCategoryDTO(imageService.getDomainUrl())
        ).collect(Collectors.toList());

        return ResponseEntity.ok().body(animalCategoryDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity AnimalCategory_GetOneById(@PathVariable int id, HttpServletRequest request) {

        AnimalCategoryEntity animalCategory = animalCategoryService.AnimalCategoryService_GetOne(id, request.getServletPath());

        AnimalCategoryDTO categoryDTO = animalCategory.setAnimalCategoryDTO(imageService.getDomainUrl());

        return ResponseEntity.ok().body(categoryDTO);
    }


    @PostMapping("")
    public ResponseEntity AnimalCategory_Create(
            @RequestPart(name = "name", required = false) String name,
            HttpServletRequest request
    ) {
        AnimalCategoryEntity animalCategory = animalCategoryService.AnimalCategoryService_Create(name, request.getServletPath());

        AnimalCategoryDTO categoryDTO = animalCategory.setAnimalCategoryDTO(imageService.getDomainUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity AnimalCategory_Update(
            @PathVariable int id,
            @RequestPart(name = "name", required = false) String categoryName,
            HttpServletRequest request
    ) {
        AnimalCategoryEntity animalCategory = animalCategoryService.AnimalCategoryService_Update(id, categoryName, request.getServletPath());

        AnimalCategoryDTO categoryDTO = animalCategory.setAnimalCategoryDTO(imageService.getDomainUrl());

        return ResponseEntity.ok().body(categoryDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity AnimalCategory_Delete(@PathVariable int id, HttpServletRequest request) {

        animalCategoryService.AnimalCategoryService_Delete(id, request.getServletPath());

        return ResponseEntity.ok().body(new ArrayList<>());
    }

}
