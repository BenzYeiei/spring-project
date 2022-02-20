package com.demo.firstProject.Controller.Animals;

import com.demo.firstProject.Service.Animals.AnimalIllustrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/animals")
public class AnimalIllustrationApi {

    private final AnimalIllustrationService animalIllustrationService;

    public AnimalIllustrationApi(AnimalIllustrationService animalIllustrationService) {
        this.animalIllustrationService = animalIllustrationService;
    }


    @PutMapping("/{animalId}/{illustrationId}")
    @ResponseStatus(HttpStatus.OK)
    public void AnimalIllustration_Update(
            @PathVariable(name = "animalId") long animalId,
            @PathVariable(name = "illustrationId") long illustrationId,
            @RequestParam(name = "illustration", required = false) MultipartFile illustrationFile
    ) {
        animalIllustrationService.AnimalIllustrationService_Update(animalId, illustrationId, illustrationFile);
    }


    @DeleteMapping("/{animalId}/{illustrationId}")
    @ResponseStatus(HttpStatus.OK)
    public void AnimalIllustration_Delete(
            @PathVariable(name = "animalId") long animalId,
            @PathVariable(name = "illustrationId") long illustrationId
    ) {

    }

}
