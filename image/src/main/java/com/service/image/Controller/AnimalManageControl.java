package com.service.image.Controller;

import com.service.image.Service.Model.Image_AnimalService;
import com.service.share.Animal.AnimalMicroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(path = "/api/v1/animals")
@RestController
public record AnimalManageControl(Image_AnimalService image_animalService) {

    @PostMapping("/upload")
    public ResponseEntity<AnimalMicroResponse> uploadAnimalImage(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "imageName") String imageName,
            @RequestPart(name = "keyOfField") String keyOfField
    ) {

        // use service
        AnimalMicroResponse animalMicroResponse = image_animalService.upload_AnimalImage(image, imageName, keyOfField);

        return ResponseEntity.ok().body(animalMicroResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<AnimalMicroResponse> updateAnimal(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "imageName") String imageName,
            @RequestPart(name = "oldImageName") String oldImageName,
            @RequestPart(name = "keyOfField") String keyOfField
    ) {

        // use service
        AnimalMicroResponse animalMicroResponse = image_animalService.update_AnimalImage(
                image,
                imageName,
                oldImageName,
                keyOfField
        );

        return ResponseEntity.ok().body(animalMicroResponse);
    }

    @PostMapping("/delete")
    public ResponseEntity<AnimalMicroResponse> deleteAnimalImage(
            @RequestPart(name = "imageName") String imageName,
            @RequestPart(name = "keyOfField") String keyOfField
    ) {

        // use service
        AnimalMicroResponse animalMicroResponse = image_animalService.delete_AnimalImage(imageName, keyOfField);

        return ResponseEntity.ok().body(animalMicroResponse);
    }
}
