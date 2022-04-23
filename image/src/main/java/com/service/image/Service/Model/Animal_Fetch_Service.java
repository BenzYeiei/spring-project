package com.service.image.Service.Model;

import com.service.image.Component.ImageEnvironment;
import com.service.image.Service.Resource.Animal_Fetch_Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
public record Animal_Fetch_Service(
        ImageEnvironment imageEnvironment) implements Animal_Fetch_Model {

    @Override
    public Resource fetchImageProfile(String imageProfile) {
        // get path of image
        Path getPath = Path.of(imageEnvironment.getDirectory_animal() + imageProfile);

        try {
            // get resource from class UrlResource
            Resource imageProfileResource = new UrlResource(getPath.toUri());

            // response
            return imageProfileResource;
        } catch (IOException e) {
            log.error("can't get image from class UrlResource with message:{}", e.getMessage());
            return null;
        }

    }

    @Override
    public Resource fetchIllustration(String illustration) {
        // get path of image
        Path getPath = Path.of(imageEnvironment.getDirectory_animal_illustrations() + illustration);

        try {
            // get resource from class UrlResource
            Resource illustrationResource = new UrlResource(getPath.toUri());

            // send log
            log.info("directory of image:{}", getPath);

            // response
            return illustrationResource;
        } catch (IOException e) {
            log.error("can't get image from class UrlResource with message:{}", e.getMessage());
            return null;
        }
    }
}
