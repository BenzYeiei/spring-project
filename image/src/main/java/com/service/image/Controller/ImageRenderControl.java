package com.service.image.Controller;

import com.service.image.Service.Model.Animal_Fetch_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/renders")
@Slf4j
@RestController
public record ImageRenderControl(Animal_Fetch_Service animal_fetch_service) {

    @GetMapping(path = "")
    public ResponseEntity<String> Test() {

        String path = System.getProperty("user.dir");

        log.info("dir name of project:{}", path);

        return ResponseEntity.ok().body(path);
    }

    @GetMapping(path = "/animals")
    public ResponseEntity<Resource> renderAnimal(
            @RequestParam(name = "imageProfile", required = false) String imageProfile,
            @RequestParam(name = "illustration", required = false) String illustration
    ) {

        // create variable resource
        Resource imageResource = null;

        // use imageProfile service
        if (imageProfile != null) {
            imageResource = animal_fetch_service.fetchImageProfile(imageProfile);
        }

        // use illustration service
        if (illustration != null) {
            imageResource = animal_fetch_service.fetchIllustration(illustration);
        }

        if (imageResource == null || !imageResource.isReadable()) {
            log.error("animal image can't render. with not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // response
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageResource);
    }

}
