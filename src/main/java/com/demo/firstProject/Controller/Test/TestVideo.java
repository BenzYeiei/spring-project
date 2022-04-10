package com.demo.firstProject.Controller.Test;

import com.demo.firstProject.Service.Resource.Image.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RequestMapping("/api/tests/media-type")
@Controller
public class TestVideo {

    private final ImageService imageService;

    public TestVideo(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping(path = "/videos")
    public ResponseEntity TestVideo_Resource() {

        Path path_VideoName = Path.of(System.getProperty("user.dir") + "\\uploads\\video\\videoplayback.mp4" );

        try {
            // get resource from method UrlResource
            Resource resource = new UrlResource(path_VideoName.toUri());

            //
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/ogg").body(resource);
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "multipart/form-data").body(resource);
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "video/mp4").body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("errorMessage", e.getMessage())
                    .body(null);
        }

    }

    @GetMapping(path = "/images")
    public ResponseEntity TestImage_Resource() {

        try {

            Path path_NameImage = Path.of(System.getProperty("user.dir") + "\\src\\test\\Image_Test\\Chonky-Animals-2-Quiz.jpg");

            Resource resource = new UrlResource(path_NameImage.toUri());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/png").body(resource);
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "multipart/form-data").body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("errorMessage", e.getMessage()).body(null);
        }
    }

}
