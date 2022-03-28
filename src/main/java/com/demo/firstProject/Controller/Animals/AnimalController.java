package com.demo.firstProject.Controller.Animals;

import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.Service.Resource.Animals.AnimalIllustrationService;
import com.demo.firstProject.Service.Resource.Animals.AnimalsService;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {

    private final AnimalsService animalsService;
    private final AnimalIllustrationService animalIllustrationService;
    private final ImageService imageService;

    public AnimalController(AnimalsService animalsService, AnimalIllustrationService animalIllustrationService, ImageService imageService) {
        this.animalsService = animalsService;
        this.animalIllustrationService = animalIllustrationService;
        this.imageService = imageService;
    }


    // TODO: GET -> get list
    @GetMapping("")
    public ResponseEntity AnimalList() {

        List<AnimalEntity> animalEntityList = animalsService.AnimalService_GetList();

        List<AnimalDTO> animalDTO_List = animalEntityList.stream().map(
                result -> result.setAnimal_dto(imageService.getDomainUrl())
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(animalDTO_List);
    }


    // TODO: GET -> get one by id
    @GetMapping("/{id}")
    public ResponseEntity AnimalAPI_GetById(@PathVariable long id) {

        AnimalEntity animal = animalsService.AnimalService_GetOneById(id);

        AnimalDTO animalDTO = animal.setAnimal_dto(imageService.getDomainUrl());

        return ResponseEntity.status(HttpStatus.OK).body(animalDTO);
    }



    // TODO: POST Create Animal
    @PostMapping("")
    @ResponseBody
//    @ResponseStatus(HttpStatus.CREATED)
//    public String AnimalCrate(
    public ResponseEntity AnimalCrate(
            @RequestPart(name = "name", required = false) String name,
            @RequestPart(name = "category", required = false) String animalCategory,
            @RequestPart(name = "quantity", required = false) String quantity,
            @RequestPart(name = "imageProfile", required = false) MultipartFile imageProfile,
            @RequestPart(name = "illustration", required = false) List<MultipartFile> illustrationFiles,
            HttpServletRequest request,
            Principal principal
    ) {

        // create animal data
        AnimalEntity animalResult = animalsService.AnimalService_Create(
                name,
                animalCategory,
                Integer.parseInt(quantity),
                imageProfile,
                illustrationFiles,
                request.getServletPath(),
                principal
        );

        AnimalEntity animal = animalsService.AnimalService_GetOneById(animalResult.getId());

        AnimalDTO animalDTO = animal.setAnimal_dto(imageService.getDomainUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(animalDTO);
    }


    // TODO: PUT Update Animal
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity AnimalUpdate(
            @PathVariable long id,
            @RequestPart(name = "name", required = false) String name,
            @RequestPart(name = "category", required = false) String animalCategory,
            @RequestPart(name = "quantity", required = false) String quantity,
            @RequestPart(name = "statusState", required = false) String statusState,
            @RequestPart(name = "imageProfile", required = false) MultipartFile imageProfile,
            @RequestPart(name = "illustration", required = false) List<MultipartFile> illustrationFiles,
            HttpServletRequest request,
            Principal principal
    ) {
        AnimalEntity animalResult = animalsService.AnimalService_Update(
                id,
                name,
                animalCategory,
                Integer.parseInt(quantity),
                Boolean.parseBoolean(statusState),
                imageProfile,
                illustrationFiles,
                request.getServletPath(),
                principal
        );

//        animalIllustrationService.AnimalIllustrationService_Create(animalEntity, illustrationFiles);

        AnimalEntity animal = animalsService.AnimalService_GetOneById(animalResult.getId());

        AnimalDTO animalDTO = animal.setAnimal_dto(imageService.getDomainUrl());

        return ResponseEntity.status(HttpStatus.OK).body(animalDTO);
    }


    // TODO: DELETE -> Delete Animal
    @DeleteMapping("/{id}")
    public ResponseEntity AnimalDelete(@PathVariable long id, Principal principal, HttpServletRequest request) {
        animalsService.AnimalService_Delete(id, principal, request.getServletPath());
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<AnimalDTO>());
    }

}
