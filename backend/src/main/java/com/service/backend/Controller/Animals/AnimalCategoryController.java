package com.service.backend.Controller.Animals;

import com.service.backend.Component.ImageEnvironment;
import com.service.backend.JPA.Entity.Animal.AnimalCategoryEntity;
import com.service.backend.Service.Resource.Animals.AnimalCategoryService;
import com.service.backend.Service.Resource.Image.ImageService;
import com.service.backend.DTO.Animals.AnimalCategoryDTO;
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
    private final ImageEnvironment imageEnvironment;

    public AnimalCategoryController(AnimalCategoryService animalCategoryService, ImageService imageService, ImageEnvironment imageEnvironment) {
        this.animalCategoryService = animalCategoryService;
        this.imageService = imageService;
        this.imageEnvironment = imageEnvironment;
    }


    @GetMapping("")
    public ResponseEntity AnimalCategory_GetList() {

        List<AnimalCategoryEntity> animalCategoryEntityList = animalCategoryService.AnimalCategoryService_GetList();

        List<AnimalCategoryDTO> animalCategoryDTO = animalCategoryEntityList.stream().map(
                result -> result.setAnimalCategoryDTO(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration())
        ).collect(Collectors.toList());

        return ResponseEntity.ok().body(animalCategoryDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity AnimalCategory_GetOneById(@PathVariable int id, HttpServletRequest request) {

        AnimalCategoryEntity animalCategory = animalCategoryService.AnimalCategoryService_GetOne(id, request.getServletPath());

        AnimalCategoryDTO categoryDTO = animalCategory.setAnimalCategoryDTO(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());

        return ResponseEntity.ok().body(categoryDTO);
    }


    @PostMapping("")
    public ResponseEntity AnimalCategory_Create(
            @RequestPart(name = "name", required = false) String name,
            HttpServletRequest request
    ) {
        AnimalCategoryEntity animalCategory = animalCategoryService.AnimalCategoryService_Create(name, request.getServletPath());

        AnimalCategoryDTO categoryDTO = animalCategory.setAnimalCategoryDTO(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity AnimalCategory_Update(
            @PathVariable int id,
            @RequestPart(name = "name", required = false) String categoryName,
            HttpServletRequest request
    ) {
        AnimalCategoryEntity animalCategory = animalCategoryService.AnimalCategoryService_Update(id, categoryName, request.getServletPath());

        AnimalCategoryDTO categoryDTO = animalCategory.setAnimalCategoryDTO(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());

        return ResponseEntity.ok().body(categoryDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity AnimalCategory_Delete(@PathVariable int id, HttpServletRequest request) {

        animalCategoryService.AnimalCategoryService_Delete(id, request.getServletPath());

        return ResponseEntity.ok().body(new ArrayList<>());
    }

}
