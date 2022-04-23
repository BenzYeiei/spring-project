package com.service.backend.Service.Resource.Image;

import com.service.backend.Exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class ImageFetch_Service {

    @Value("${image.dir_name.base}")
    private String rootDirBase;

    @Value("${image.dir_name.animal}")
    private String rootDirAnimalProfile;

    @Value("${image.dir_name.illustration}")
    private String rootDirAnimalIllustration;


    // TODO:: response resource of animal image profile
    public ResponseEntity<Resource> ImageService_ResponseAllImage(String name) {
        // TODO: check null of name
        if (name == null) {
            throw new BaseException("api.images.animals.null", HttpStatus.NOT_FOUND);
        }
        // TODO: get all path from dir
        Path basePath = Path.of(rootDirBase + "/" + name);
        Path animalProfilePath = Path.of(rootDirAnimalProfile + "/" + name);
        Path animalIllustration = Path.of(rootDirAnimalIllustration + "/" + name);

        try {
            // TODO: get get resource from path with UrlResource method
            Resource baseResource = new UrlResource(basePath.toUri());
            if (baseResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "image/png").body(baseResource);
            }

            Resource animalProfileResource = new UrlResource(animalProfilePath.toUri());
            if (animalProfileResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "image/png").body(animalProfileResource);
            }

            Resource animalIllustrationResource = new UrlResource(animalIllustration.toUri());
            if (animalIllustrationResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "image/png").body(animalIllustrationResource);
            }
            throw new BaseException("not-found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new BaseException("can't render image." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
