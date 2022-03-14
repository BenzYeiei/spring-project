package com.demo.firstProject.Controller.Animals;

import com.demo.firstProject.Configuration.Domain;
import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.DTO.Request.AnimalRequest;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.Service.Resource.Animals.AnimalIllustrationService;
import com.demo.firstProject.Service.Resource.Animals.AnimalsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalApi {

    private final AnimalsService animalsService;
    private final AnimalIllustrationService animalIllustrationService;

    @Value("${domain_url}")
    String domain;

    public AnimalApi(AnimalsService animalsService, AnimalIllustrationService animalIllustrationService) {
        this.animalsService = animalsService;
        this.animalIllustrationService = animalIllustrationService;
    }


    // TODO: GET -> get list
    @RequestMapping
    @GetMapping("")
    public ResponseEntity AnimalList() {
        List<AnimalDTO> animalDTO = animalsService.AnimalService_GetList();
        return ResponseEntity.status(HttpStatus.OK).body(animalDTO);
    }


    // TODO: GET -> get one by id
    @GetMapping("/{id}")
    public ResponseEntity AnimalAPI_GetById(@PathVariable long id) {
        AnimalDTO animalDTO = animalsService.AnimalService_GetOneById(id);
        return ResponseEntity.status(HttpStatus.OK).body(animalDTO);
    }



    // TODO: POST Create Animal
    @PostMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
//    public String AnimalCrate(
    public ResponseEntity AnimalCrate(
            AnimalRequest request,
            @RequestParam(name = "imageProfile", required = false) MultipartFile imageProfile,
            @RequestParam(name = "illustration", required = false) MultipartFile[] illustrationFiles
    ) {
        long animalId = animalsService.AnimalService_Create(request, imageProfile);

        animalIllustrationService.AnimalIllustrationService_Create(animalId, illustrationFiles);

        AnimalDTO animalDTO = animalsService.AnimalService_GetOneById(animalId);
//        return Domain.domainUrl + "/api/animals/" + animalId;-
        return ResponseEntity.status(HttpStatus.OK).body(animalDTO);
    }


    // TODO: PUT Update Animal
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String AnimalUpdate(
            @PathVariable long id,
            AnimalRequest request,
            @RequestParam(name = "imageProfile", required = false) MultipartFile imageProfile,
            @RequestParam(name = "illustration", required = false) MultipartFile[] illustrationFiles
    ) {
        long animalId = animalsService.AnimalService_Update(id, request, imageProfile);

        animalIllustrationService.AnimalIllustrationService_Create(animalId, illustrationFiles);

        return Domain.domainUrl + "/api/animals/" + animalId;
    }


    // TODO: DELETE -> Delete Animal
    @DeleteMapping("/{id}")
    public ResponseEntity AnimalDelete(@PathVariable long id) {
        animalsService.AnimalService_Delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<AnimalDTO>());
    }

}
