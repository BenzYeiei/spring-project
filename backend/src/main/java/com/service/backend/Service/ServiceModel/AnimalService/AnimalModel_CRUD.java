package com.service.backend.Service.ServiceModel.AnimalService;

import com.service.backend.DTO.Animals.AnimalDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface AnimalModel_CRUD {

    Page<AnimalDTO> AnimalService_GetPage(int pageNum);

    AnimalDTO AnimalService_GetOneById(long id);

    AnimalDTO AnimalService_Create(
            String name,
            String animalCategory,
            int quantity,
            MultipartFile imageProfile,
            List<MultipartFile> illustrationFiles,
            String path,
            Principal principal
    );

    AnimalDTO AnimalService_Update(
            long id,
            String name,
            String animalCategory,
            int quantity,
            boolean statusState,
            MultipartFile imageProfile,
            String path,
            Principal principal
    );

    void AnimalService_Delete(long id, Principal principal, String path);

    void uploadImage(Resource image, String imageName, String path);
    void deleteImage(String imageName, String path);

}
