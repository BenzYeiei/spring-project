package com.service.image.Service.Model;

import com.service.image.Component.ImageEnvironment;
import com.service.image.Service.Resource.Animal_Image_Model;
import com.service.share.Animal.AnimalMicroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public record Image_AnimalService(
        ImageEnvironment imageEnvironment
    ) implements Animal_Image_Model {


    // TODO:: Upload animal Image
    @Override
    public AnimalMicroResponse upload_AnimalImage(MultipartFile image, String imageName, String keyOfField) {
        // create object of response
        AnimalMicroResponse response = new AnimalMicroResponse();

        // get path
        Path imagePath = createPath(imageName, keyOfField);

        // check path
        if (imagePath == null) {
            response.setExecute(false);
            response.setMessage("field keyOfField not correct.");
            return response;
        }

        try {
            // upload image to my computer
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // set data
            response.setExecute(true);
            response.setMessage("Upload image "+ imageName +" success.");

            log.info("Server upload image:{}", imageName);

            return response;
        } catch (IOException e) {
            // set data
            response.setExecute(false);
            response.setMessage(e.getMessage());

            // log error
            log.error("can't upload animal imageProfile. message:{}", e.getMessage());

            return response;
        }
    }

    // TODO:: UPDATE animal Image
    @Override
    public AnimalMicroResponse update_AnimalImage(MultipartFile image, String imageName, String oldImageName, String keyOfField) {
        // create object of response
        AnimalMicroResponse response = new AnimalMicroResponse();

        // get path
        Path imagePath = createPath(imageName, keyOfField);
        Path oldImagePath = createPath(imageName, keyOfField);

        // check path
        if (imagePath == null) {
            response.setExecute(false);
            response.setMessage("field keyOfField not correct.");
            return response;
        }

        try {
            // delete old image from my computer
            Files.delete(oldImagePath);

            // upload image to my computer
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // set data
            response.setExecute(true);
            response.setMessage("Upload image success.");

            return response;
        } catch (IOException e) {
            // set data
            response.setExecute(false);
            response.setMessage(e.getMessage());

            // log error
            log.error("can't upload animal imageProfile. message:{}", e.getMessage());

            return response;
        }
    }

    // TODO:: DELETE animal Image
    @Override
    public AnimalMicroResponse delete_AnimalImage(String imageName, String keyOfField) {
        // create object of response
        AnimalMicroResponse response = new AnimalMicroResponse();

        // get path
        Path imagePath = createPath(imageName, keyOfField);

        // check path
        if (imagePath == null) {
            response.setExecute(false);
            response.setMessage("field keyOfField not correct.");
            return response;
        }

        try {
            // delete image from my computer
            Files.delete(imagePath);

            // set data
            response.setExecute(true);
            response.setMessage("Delete image " + imageName + " success.");

            log.info("Server delete image:{}", imageName);

            return response;
        } catch (IOException e) {
            // set data
            response.setExecute(false);
            response.setMessage(e.getMessage());

            // log error
            log.error("can't upload animal imageProfile. message:{}", e.getMessage());

            return response;
        }
    }

    // TODO:: CREATE Image Path
    @Override
    public Path createPath(String imageName, String keyOfField) {
        // create variable
        Path imagePath = null;

        // create image profile path
        if (keyOfField.equals(imageEnvironment.getKeyOfAnimalImageProfile())) {
            imagePath = Path.of(imageEnvironment.getDirectory_animal() + imageName);
        }

        // create illustration path
        if (keyOfField.equals(imageEnvironment.getKeyOfAnimalIllustration())) {
            imagePath = Path.of(imageEnvironment.getDirectory_animal_illustrations() + imageName);
        }

        //
        if (imagePath == null) {
            log.error("can't create path of image, because keyOfField not correct.");
        }

        return imagePath;
    }
}
