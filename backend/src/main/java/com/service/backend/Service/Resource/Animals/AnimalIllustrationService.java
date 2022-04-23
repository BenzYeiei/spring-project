package com.service.backend.Service.Resource.Animals;

import com.service.backend.Component.ImageEnvironment;
import com.service.backend.DTO.Animals.AnimalIllustrationDTO;
import com.service.backend.Exception.BaseException;
import com.service.backend.JPA.Entity.Account.AccountEntity;
import com.service.backend.JPA.Entity.Animal.AnimalEntity;
import com.service.backend.JPA.Entity.Animal.AnimalIllustrationEntity;
import com.service.backend.JPA.Repository.Account.AccountRepository;
import com.service.backend.JPA.Repository.AnimalIllustrationRepositoty;
import com.service.backend.JPA.Repository.AnimalRepository;
import com.service.backend.Service.Resource.Image.ImageService;
import com.service.backend.Service.ServiceModel.AnimalService.AnimalIllustrationModel_CRUD;
import com.service.share.Animal.AnimalMicroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public record AnimalIllustrationService(AnimalRepository animalRepository,
                                        AnimalIllustrationRepositoty animalIllustrationRepositoty,
                                        ImageService imageService,
                                        ImageEnvironment imageEnvironment,
                                        RestTemplate restTemplate,
                                        AccountRepository accountRepository) implements AnimalIllustrationModel_CRUD {

    // TODO:: Get List By Animal Id
    @Override
    public List<AnimalIllustrationEntity> AnimalIllustrationService_GetListById(long animalId, String path) {

        // find animal data
        Optional<AnimalEntity> animalOptional = animalRepository.findById(animalId);

        // check animal data
        if (animalOptional.isEmpty()) {
            throw new BaseException("animal data not found.", HttpStatus.NOT_FOUND, path);
        }

        // get animal
        AnimalEntity animal = animalOptional.get();

        return animalIllustrationRepositoty.findAllByAnimalFK(animal);
    }

    // TODO:: Create Illustration of animals
    @Override
    public boolean AnimalIllustrationService_Create(
            AnimalEntity animalEntity,
            AccountEntity accountEntity,
            List<MultipartFile> illustrationImages,
            String path
    ) {

        // loop
        for (MultipartFile multipartFile : illustrationImages) {

            // check list is null
            if (multipartFile.isEmpty()) {
                return true;
            }

            // create name image and path image
            String animalIllustration_Name = imageEnvironment.getImageName(
                    "animal-illustration",
                    multipartFile.getOriginalFilename()
            );

            // check image name from generate image name
            if (animalIllustration_Name == null) {
                log.error("can't generate name of image.");
                throw new BaseException("can't generate name of image.", HttpStatus.INTERNAL_SERVER_ERROR, path);
            }

            // create object
            AnimalIllustrationEntity illustrationObject = new AnimalIllustrationEntity();

            // set data
            illustrationObject.setName(animalIllustration_Name);
            illustrationObject.setAnimalFK(animalEntity);
            illustrationObject.setAccountFK(accountEntity);

            // save data
            try {
                animalIllustrationRepositoty.save(illustrationObject);
            } catch (Exception e) {
                log.error("can't save illustration data. message:{}", e.getMessage());
                throw new BaseException("Server message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
            }

            // upload image
            uploadImage(multipartFile.getResource(), animalIllustration_Name, path);

            // get dto from entity
            AnimalIllustrationDTO animalIllustrationDTO = illustrationObject.SetAnimalIllustration_dto(imageEnvironment.getRenderAnimalIllustration());

            // log send create illustration
            log.info("Server create illustration data:{}", animalIllustrationDTO);

        }

        return true;

    }


    // TODO:: Update Illustration of animals
    @Override
    public String AnimalIllustrationService_Update(
            long animalId,
            long illustrationId,
            MultipartFile illustrationImages,
            Principal principal,
            String path
    ) {

        // find animal data
        Optional<AnimalEntity> animalOptional = animalRepository.findById(animalId);

        // check animal data
        if (animalOptional.isEmpty()) {
            throw new BaseException("animal data not found.", HttpStatus.NOT_FOUND, path);
        }

        // find illustration data
        Optional<AnimalIllustrationEntity> animalIllustrationOptional = animalIllustrationRepositoty.findById(illustrationId);

        // check illustration data
        if (animalIllustrationOptional.isEmpty()) {
            throw new BaseException("illustration data not found.", HttpStatus.NOT_FOUND, path);
        }

        // get animal and illustration data
        AnimalEntity animal = animalOptional.get();
        AnimalIllustrationEntity animalIllustration = animalIllustrationOptional.get();

        // check own illustration
        if (!animalIllustration.getAccountFK().getId().equals(principal.getName())) {
            throw new BaseException("This account can't manage illustration data.", HttpStatus.BAD_REQUEST, path);
        }

        // check illustration is extends from animal
        if (!animalIllustration.getAnimalFK().equals(animal)) {
            throw new BaseException("animal and illustration not related.", HttpStatus.BAD_REQUEST, path);
        }

        // check image file of illustration
        if (illustrationImages.isEmpty()) {
            throw new BaseException("image file of illustration is null.", HttpStatus.BAD_REQUEST, path);
        }

        // create name of illustration
        String illustrationName = imageEnvironment.getImageName("illustration", illustrationImages.getOriginalFilename());

        // check image name from method
        if (illustrationName == null) {
            log.error("Server can't generate image name.");
            throw new BaseException("Server can't generate image name.", HttpStatus.BAD_REQUEST, path);
        }

        // delete old illustration image
        deleteImage(animalIllustration.getName(), path);

        // upload illustration image
        uploadImage(illustrationImages.getResource(), illustrationName, path);

        // set illustration name to object
        animalIllustration.setName(illustrationName);

        // update illustration data
        try {
            animalIllustrationRepositoty.save(animalIllustration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get dto from entity
        AnimalIllustrationDTO animalIllustrationDTO = animalIllustration.SetAnimalIllustration_dto(imageEnvironment.getRenderAnimalIllustration());

        // log send update illustration
        log.info("Server update illustration data:{}", animalIllustrationDTO);

        return imageEnvironment.getRenderAnimalIllustration() + illustrationName;
    }


    // TODO:: Delete illustration of animal
    @Override
    public void AnimalIllustrationService_DeleteFromDeleteImage(
            long illustrationId,
            String illustrationName,
            String path
    ) {

        // delete illustration data
        try {
            animalIllustrationRepositoty.deleteById(illustrationId);
        } catch (Exception e) {
            throw new BaseException("server error, this message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // delete illustration image
        deleteImage(illustrationName, path);

        // log send update illustration
        log.info("Server update illustration name:{}", illustrationName);
    }

    // TODO:: Create Illustration -> Set Before Create
    @Override
    public void AnimalIllustrationService_VerifyAndCreate(
            long animalId,
            List<MultipartFile> illustrationImages,
            Principal principal,
            String path
    ) {

        // find animal data
        Optional<AnimalEntity> animalOptional = animalRepository.findById(animalId);

        // check animal data
        if (animalOptional.isEmpty()) {
            throw new BaseException("animal data not found.", HttpStatus.NOT_FOUND, path);
        }

        // find account data
        Optional<AccountEntity> accountOptional = accountRepository.findById(principal.getName());

        // check account data
        if (accountOptional.isEmpty()) {
            throw new BaseException("This account not found.", HttpStatus.NOT_FOUND, path);
        }

        // get animal and account data
        AnimalEntity animalEntity = animalOptional.get();
        AccountEntity accountEntity = accountOptional.get();

        // check own animal data
        if (!animalEntity.getCreatedByUser().equals(accountEntity)) {
            throw new BaseException("This account not manage illustration data.", HttpStatus.BAD_REQUEST, path);
        }

        AnimalIllustrationService_Create(
                animalEntity,
                accountEntity,
                illustrationImages,
                path
        );
    }

    //TODO:: DELETE Illustration one data
    @Override
    public void AnimalIllustrationService_DeleteOne(
            long animalId,
            long illustrationId,
            Principal principal,
            String path
    ) {
        // find animal data
        Optional<AnimalEntity> animalOptional = animalRepository.findById(animalId);

        // check animal data
        if (animalOptional.isEmpty()) {
            throw new BaseException("animal data not found.", HttpStatus.NOT_FOUND, path);
        }

        // find illustration data
        Optional<AnimalIllustrationEntity> animalIllustrationOptional = animalIllustrationRepositoty.findById(illustrationId);

        // check illustration data
        if (animalIllustrationOptional.isEmpty()) {
            throw new BaseException("illustration data not found.", HttpStatus.NOT_FOUND, path);
        }

        // get animal and illustration data
        AnimalEntity animal = animalOptional.get();
        AnimalIllustrationEntity animalIllustration = animalIllustrationOptional.get();

        // check own illustration
        if (!animalIllustration.getAccountFK().getId().equals(principal.getName())) {
            throw new BaseException("This account can't manage illustration data.", HttpStatus.BAD_REQUEST, path);
        }

        // check illustration is extends from animal
        if (!animalIllustration.getAnimalFK().equals(animal)) {
            throw new BaseException("animal and illustration not related.", HttpStatus.BAD_REQUEST, path);
        }

        // delete illustration data
        try {
            animalIllustrationRepositoty.deleteById(illustrationId);
        } catch (Exception e) {
            throw new BaseException("server error, this message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // delete illustration image
        deleteImage(animalIllustration.getName(), path);

        // get dto from entity
        AnimalIllustrationDTO animalIllustrationDTO = animalIllustration.SetAnimalIllustration_dto(imageEnvironment.getRenderAnimalIllustration());


        // log send delete illustration data
        log.info("Server delete illustration data:{}", animalIllustrationDTO);
    }

    // TODO:: Use upload image with image microservice
    @Override
    public void uploadImage(Resource image, String imageName, String path) {
        // create body for fetch image microservice
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // set data to body
        body.add("image", image);
        body.add("imageName", imageName);
        body.add("keyOfField", imageEnvironment.getKeyOfAnimalIllustration());

        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // create http request
        HttpEntity<MultiValueMap<String, Object>> requestHttpEntity = new HttpEntity<>(body, headers);

        // request image microservice
        ResponseEntity<AnimalMicroResponse> animalMicroResponse = restTemplate.postForEntity(
                "http://127.0.0.1:8082/api/v1/animals/upload",
                requestHttpEntity,
                AnimalMicroResponse.class
        );

        // check response from image microservice
        if (animalMicroResponse.getBody() != null) {

            // check process of image microservice
            if (!animalMicroResponse.getBody().isExecute()) {
                log.error("can't upload image on image microservice. System:{}", animalMicroResponse.getBody().getMessage());
                throw new BaseException(
                        "can't upload image on image microservice. System:" + animalMicroResponse.getBody().getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        path
                );
            }

            // log success
            log.info("Image microservice send message:{}", animalMicroResponse.getBody().getMessage());

        } else {
            log.error("error, body of response is null from image microservice.");
            throw new BaseException(
                    "error, body of response is null from image microservice.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    path
            );
        }
    }

    // TODO:: Use delete image with image microservice
    @Override
    public void deleteImage(String imageName, String path) {
        // create body for fetch image microservice
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // set data to body
        body.add("imageName", imageName);
        body.add("keyOfField", imageEnvironment.getKeyOfAnimalIllustration());

        // create header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // create http request
        HttpEntity<MultiValueMap<String, Object>> requestHttpEntity = new HttpEntity<>(body, headers);

        // request image microservice
        ResponseEntity<AnimalMicroResponse> animalMicroResponse = restTemplate.postForEntity(
                "http://127.0.0.1:8082/api/v1/animals/delete",
                requestHttpEntity,
                AnimalMicroResponse.class
        );

        // check response from image microservice
        if (animalMicroResponse.getBody() != null) {

            // check process of image microservice
            if (!animalMicroResponse.getBody().isExecute()) {
                log.error("can't delete image on image microservice. System:{}", animalMicroResponse.getBody().getMessage());
                throw new BaseException(
                        "can't delete image on image microservice. System:" + animalMicroResponse.getBody().getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        path
                );
            }

            // log success
            log.info("Image microservice send message:{}", animalMicroResponse.getBody().getMessage());

        } else {
            log.error("error, body of response is null from image microservice.");
            throw new BaseException(
                    "error, body of response is null from image microservice.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    path
            );
        }
    }
}
