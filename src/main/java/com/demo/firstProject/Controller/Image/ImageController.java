package com.demo.firstProject.Controller.Image;

import com.demo.firstProject.Service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images/{name}")
    public ResponseEntity<Resource> Api_Image(@PathVariable String name) {
        return imageService.ImageService_ResponseAllImage(name);
    }

}
