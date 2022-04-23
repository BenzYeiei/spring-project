package com.service.backend.Controller.Animals;

import com.service.backend.Component.ImageEnvironment;
import com.service.backend.DTO.Animals.AnimalIllustrationDTO;
import com.service.backend.JPA.Entity.Animal.AnimalIllustrationEntity;
import com.service.backend.Service.Resource.Animals.AnimalIllustrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animals/v1/illustration")
public record AnimalIllustrationApi(
        ImageEnvironment imageEnvironment,
        AnimalIllustrationService animalIllustrationService) {

    @GetMapping(path = "")
    public ResponseEntity<List<AnimalIllustrationDTO>> AnimalIllustration_GetList(
            @RequestParam(name = "animalId") long animalId,
            HttpServletRequest request
    ) {

        // use service
        List<AnimalIllustrationEntity> illustrationEntityList = animalIllustrationService.AnimalIllustrationService_GetListById(
                animalId,
                request.getServletPath()
        );

        // entity to dto
        List<AnimalIllustrationDTO> illustrationDTOList = illustrationEntityList.stream().map(
                result -> result.SetAnimalIllustration_dto(imageEnvironment.getRenderAnimalIllustration())
        ).collect(Collectors.toList());

        return ResponseEntity.ok().body(illustrationDTOList);
    }

    @PostMapping(path = "")
    public ResponseEntity<List<AnimalIllustrationDTO>> AnimalIllustration_Create(
            @RequestParam(name = "animalId") long animalId,
            @RequestPart(name = "illustration") List<MultipartFile> illustrationImages,
            Principal principal,
            HttpServletRequest request
    ) {
        // use service
        animalIllustrationService.AnimalIllustrationService_VerifyAndCreate(
                animalId,
                illustrationImages,
                principal,
                request.getServletPath()
        );

        // use service fetch list illustration
        List<AnimalIllustrationEntity> illustrationEntityList = animalIllustrationService.AnimalIllustrationService_GetListById(
                animalId,
                request.getServletPath()
        );

        // entity to dto
        List<AnimalIllustrationDTO> illustrationDTOList = illustrationEntityList.stream().map(
                result -> result.SetAnimalIllustration_dto(imageEnvironment.getRenderAnimalIllustration())
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(illustrationDTOList);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HashMap<String, String>> AnimalIllustration_Update(
            @RequestParam(name = "animalId") long animalId,
            @RequestParam(name = "illustrationId") long illustrationId,
            @RequestPart(name = "illustration") MultipartFile illustrationImages,
            Principal principal,
            HttpServletRequest request
    ) {
        // use service
        String illustrationName = animalIllustrationService.AnimalIllustrationService_Update(
                animalId,
                illustrationId,
                illustrationImages,
                principal,
                request.getServletPath()
        );

        // create body
        HashMap<String, String> body = new HashMap<>();
        body.put("URL-Resource", illustrationName);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }


    @DeleteMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<String>> AnimalIllustration_Delete(
            @RequestParam(name = "animalId") long animalId,
            @RequestParam(name = "illustrationId") long illustrationId,
            Principal principal,
            HttpServletRequest request
    ) {

        // use service
        animalIllustrationService.AnimalIllustrationService_DeleteOne(
                animalId,
                illustrationId,
                principal,
                request.getServletPath()
        );

        return ResponseEntity.ok().body(new ArrayList<>());
    }

}
