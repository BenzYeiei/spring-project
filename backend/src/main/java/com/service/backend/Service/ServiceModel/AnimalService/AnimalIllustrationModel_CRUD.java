package com.service.backend.Service.ServiceModel.AnimalService;

import com.service.backend.JPA.Entity.Account.AccountEntity;
import com.service.backend.JPA.Entity.Animal.AnimalEntity;
import com.service.backend.JPA.Entity.Animal.AnimalIllustrationEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface AnimalIllustrationModel_CRUD {

    List<AnimalIllustrationEntity> AnimalIllustrationService_GetListById(long animalId, String path);

    boolean AnimalIllustrationService_Create(
            AnimalEntity animalEntity,
            AccountEntity accountEntity,
            List<MultipartFile> illustrationImages,
            String path
    );

    void AnimalIllustrationService_VerifyAndCreate(
            long animalId,
            List<MultipartFile> illustrationImages,
            Principal principal,
            String path
    );

    String AnimalIllustrationService_Update(
            long animalId,
            long illustrationId,
            MultipartFile illustrationImages,
            Principal principal,
            String path
    );

    void AnimalIllustrationService_DeleteOne(
            long animalId,
            long illustrationId,
            Principal principal,
            String path
    );

    void AnimalIllustrationService_DeleteFromDeleteImage(
            long illustrationId,
            String illustrationName,
            String path
    );

    void uploadImage(Resource image, String imageName, String path);

    void deleteImage(String imageName, String path);

}
