package com.service.image.Service.Resource;

import com.service.share.Animal.AnimalMicroResponse;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface Animal_Image_Model {

    AnimalMicroResponse upload_AnimalImage(MultipartFile image, String imageName, String keyOfField);
    AnimalMicroResponse update_AnimalImage(MultipartFile image, String imageName, String oldImageName, String keyOfField);
    AnimalMicroResponse delete_AnimalImage(String imageName, String keyOfField);

    Path createPath(String imageName, String keyOfField);

}
