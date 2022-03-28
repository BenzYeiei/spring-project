package com.demo.firstProject.Controller.Image;

import com.demo.firstProject.Service.Resource.Image.ImageFetch_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ImageController {

    @Autowired
    private ImageFetch_Service imageFetchService;

    @GetMapping("/images/{name}")
    public ResponseEntity<Resource> Api_Image(@PathVariable String name) {
        return imageFetchService.ImageService_ResponseAllImage(name);
    }

}
