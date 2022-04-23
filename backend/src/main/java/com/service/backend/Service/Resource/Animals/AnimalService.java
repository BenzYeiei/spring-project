package com.service.backend.Service.Resource.Animals;

import com.service.backend.Component.ImageEnvironment;
import com.service.backend.DTO.Animals.AnimalDTO;
import com.service.backend.Exception.BaseException;
import com.service.backend.JPA.Entity.Account.AccountEntity;
import com.service.backend.JPA.Entity.Animal.AnimalCategoryEntity;
import com.service.backend.JPA.Entity.Animal.AnimalEntity;
import com.service.backend.JPA.Entity.Animal.AnimalIllustrationEntity;
import com.service.backend.JPA.Repository.Account.AccountRepository;
import com.service.backend.JPA.Repository.AnimalCategoryRepository;
import com.service.backend.JPA.Repository.AnimalRepository;
import com.service.backend.Service.Resource.Image.ImageService;
import com.service.backend.Service.ServiceModel.AnimalService.AnimalModel_CRUD;
import com.service.share.Animal.AnimalMicroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AnimalService implements AnimalModel_CRUD {

    private final AccountRepository accountRepository;
    private final AnimalRepository animalRepository;
    private final AnimalCategoryRepository animalCategoryRepository;
    private final AnimalIllustrationService animalIllustrationService;
    private final ImageService imageService;
    private final RestTemplate restTemplate;
    private final ImageEnvironment imageEnvironment;

    public AnimalService(AccountRepository accountRepository, AnimalRepository animalRepository, AnimalCategoryRepository animalCategoryRepository, AnimalIllustrationService animalIllustrationService, ImageService imageService, RestTemplate restTemplate, ImageEnvironment imageEnvironment) {
        this.accountRepository = accountRepository;
        this.animalRepository = animalRepository;
        this.animalCategoryRepository = animalCategoryRepository;
        this.animalIllustrationService = animalIllustrationService;
        this.imageService = imageService;
        this.restTemplate = restTemplate;
        this.imageEnvironment = imageEnvironment;
    }

    // TODO:: Fetch data for Page
    @Override
    @Cacheable(value = "AnimalPage", key = "#pageNum")
    public Page<AnimalDTO> AnimalService_GetPage(int pageNum) {

        // fetch animal data
        Page<AnimalEntity> page = animalRepository.findAllByOrderByCreateTimeDesc(PageRequest.of(pageNum - 1, 3));

//        Page<AnimalEntity> page = animalRepository.findAll(
//                PageRequest.of(pageNum - 1, 3, Sort.by(Sort.Direction.DESC, "createTime"))
//        );

        // set animal data to dto
        Page<AnimalDTO> page_DTO = page.map(
                result -> result.setAnimal_dto(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration())
        );

        // log info for show process in method, if redis have data method not process
        log.info("load page of data, page at:{}", pageNum);

        return page_DTO;
    }


    // TODO:: Get One Animal by Id
    @Override
    @Cacheable(value = "AnimalGetById", key = "#id")
    public AnimalDTO AnimalService_GetOneById(long id) {
        // Check exists by id
        boolean isAnimalEntity = animalRepository.existsById(id);
        if (!isAnimalEntity) {
            throw new BaseException("api.animals.get.field.id.not-found", HttpStatus.NOT_FOUND);
        }

        // fetch animal data
        AnimalEntity animal = animalRepository.getById(id);

        return animal.setAnimal_dto(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());
    }


    // TODO:: Create Animal
    @Override
    public AnimalDTO AnimalService_Create(
            String name,
            String animalCategory,
            int quantity,
            MultipartFile imageProfile,
            List<MultipartFile> illustrationFiles,
            String path,
            Principal principal
    ) {
        // check field name not null
        if (name == null) {
            log.error("field name null.");
            throw new BaseException("field name null.", HttpStatus.BAD_REQUEST, path);
        }

        // check field category not null
        if (animalCategory == null) {
            log.error("field category null.");
            throw new BaseException("field category null.", HttpStatus.BAD_REQUEST, path);
        }

        // check field profile image not null
        if (imageProfile.isEmpty()) {
            log.error("field imageProfile null.");
            throw new BaseException("field imageProfile null.", HttpStatus.BAD_REQUEST, path);
        }

        // check length of name equal 30
        if (name.length() > 30) {
            log.error("field name length size 30.");
            throw new BaseException("field name length size 30.", HttpStatus.BAD_REQUEST, path);
        }

        // get category data
        Optional<AnimalCategoryEntity> animalCategoryOptional = animalCategoryRepository.findByCategoryName(animalCategory.toLowerCase());

        // check category exists
        if (animalCategoryOptional.isEmpty()) {
            log.error("field category not corresponding.");
            throw new BaseException("field category not corresponding.", HttpStatus.BAD_REQUEST, path);
        }

        // check account of owner
        Optional<AccountEntity> accountResult = accountRepository.findById(principal.getName());
        if (accountResult.isEmpty()) {
            log.error("account name not correct.");
            throw new BaseException("account name not correct.", HttpStatus.BAD_REQUEST, path);
        }

        // get account data from optional
        AccountEntity accountData = accountResult.get();

        // create name image profile
        String imageProfileName = imageEnvironment.getImageName("animal-profile", imageProfile.getOriginalFilename());

        // check image name from method
        if (imageProfileName == null) {
            log.error("can't generate name of image.");
            throw new BaseException("can't generate name of image.", HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // create object AnimalCategoryEntity
        AnimalCategoryEntity AnimalCategoryData = animalCategoryOptional.get();

        // create object AnimalEntity
        AnimalEntity animalEntityObj = new AnimalEntity();

        // Set data to AnimalEntity
        animalEntityObj.setName(name);
        animalEntityObj.setImageProfile(imageProfileName);
        animalEntityObj.setAnimalCategoryFK(AnimalCategoryData);
        animalEntityObj.setQuantity(quantity);
        animalEntityObj.setCreateTime(LocalDateTime.now());
        animalEntityObj.setCreatedByUser(accountData);

        // create variable for get result data
        AnimalEntity animalResultData;

        // Create animal with save();
        try {
            animalResultData = animalRepository.save(animalEntityObj);
        } catch (RuntimeException exception) {
            log.error("can't save animal data. message:{}", exception.getMessage());
            throw new BaseException("Server message: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // upload image
        uploadImage(imageProfile.getResource(), imageProfileName, path);

        // create illustration
        animalIllustrationService.AnimalIllustrationService_Create(animalResultData, accountData, illustrationFiles, path);

        AnimalDTO getAnimalDTO = animalResultData.setAnimal_dto(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());

        // log send create animal data
        log.info("Server create animal data:{}", getAnimalDTO);

        return getAnimalDTO;
    }


    // TODO:: Update Animal
    @Override
    public AnimalDTO AnimalService_Update(
            long id,
            String name,
            String animalCategory,
            int quantity,
            boolean statusState,
            MultipartFile imageProfile,
            String path,
            Principal principal
    ) {
        // get old animal data
        Optional<AnimalEntity> animalOptional = animalRepository.findById(id);

        // search animal by id
        if (animalOptional.isEmpty()) {
            throw new BaseException("animal data is not found.", HttpStatus.NOT_FOUND, path);
        }

        // get old data and old data for send log
        AnimalEntity animal_OldData = animalOptional.get();
        AnimalDTO animal_OldData_ForLog = animal_OldData.setAnimal_dto(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());

        // check own animal data
        if (!animal_OldData.getCreatedByUser().getId().equals(principal.getName())) {
            throw new BaseException("This account can't update data.", HttpStatus.FORBIDDEN, path);
        }

        // Check animal name not null
        if (name == null) {
            throw new BaseException("field name null", HttpStatus.BAD_REQUEST);
        }

        if (name.length() > 30) {
            throw new BaseException("animal name character must less then 30.", HttpStatus.BAD_REQUEST, path);
        }

        // check category name not null
        if (animalCategory == null) {
            throw new BaseException("field category null", HttpStatus.BAD_REQUEST);
        }

        // get category data
        Optional<AnimalCategoryEntity> animalCategoryOptional = animalCategoryRepository.findByCategoryName(animalCategory.toLowerCase());

        // check category name is correct
        if (animalCategoryOptional.isEmpty()) {
            throw new BaseException("category of animal not correct.", HttpStatus.BAD_REQUEST, path);
        }

        // get category data
        AnimalCategoryEntity animalCategory_OldData = animalCategoryOptional.get();

        // Set data request to AnimalEntity
        animal_OldData.setName(name);
        animal_OldData.setQuantity(quantity);
        animal_OldData.setStatusState(statusState);
        animal_OldData.setAnimalCategoryFK(animalCategory_OldData);

        // create variable for return
        AnimalEntity animalNewData;

        // check empty of imageProfile
        if (!imageProfile.isEmpty()) {
            // get old name of image profile for delete
            String old_nameImage = animal_OldData.getImageProfile();

            // create image name
            String imageProfileName = imageEnvironment.getImageName("animal-profile", imageProfile.getOriginalFilename());

            // check image name from method
            if (imageProfileName == null) {
                log.error("can't generate name of image.");
                throw new BaseException("can't generate name of image.", HttpStatus.INTERNAL_SERVER_ERROR, path);
            }

            // set data of image file name to object
            animal_OldData.setImageProfile(imageProfileName);

            // delete old image
            deleteImage(old_nameImage, path);

            // upload image
            uploadImage(imageProfile.getResource(), imageProfileName, path);
        }

        // Save animal data
        try {
            animalNewData = animalRepository.save(animal_OldData);
        } catch (Exception e) {
            throw new BaseException("Server message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // get animal dto
        AnimalDTO getAnimalDTO = animalNewData.setAnimal_dto(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());

        // log send old animal data
        log.info("Server update animal data, with old data:{}", animal_OldData_ForLog);

        // log send update animal data, new data
        log.info("Server update animal data, with new data:{}", getAnimalDTO);

        // return animal id
        return getAnimalDTO;
    }


    // TODO:: Delete Animal
    @Override
    public void AnimalService_Delete(long id, Principal principal, String path) {
        // search animal data
        Optional<AnimalEntity> animal_OldData = animalRepository.findById(id);

        // check data
        if (animal_OldData.isEmpty()) {
            throw new BaseException("animal data is not found.", HttpStatus.NOT_FOUND, path);
        }

        // covert data
        AnimalEntity animalData = animal_OldData.get();

        // get animal dto for send log
        AnimalDTO getAnimalDTO = animalData.setAnimal_dto(imageEnvironment.getRenderAnimalImageProfile(), imageEnvironment.getRenderAnimalIllustration());

        // check own data
        if (!animalData.getCreatedByUser().getId().equals(principal.getName())) {
            throw new BaseException("This account not delete data.", HttpStatus.FORBIDDEN, path);
        }

        // get all Illustration to list
        List<AnimalIllustrationEntity> animalIllustrationList = animalIllustrationService.AnimalIllustrationService_GetListById(
                animalData.getId(),
                path
        );

        // delete each illustration
        for (AnimalIllustrationEntity illustration : animalIllustrationList) {

            // use service
            animalIllustrationService.AnimalIllustrationService_DeleteFromDeleteImage(
                    illustration.getId(),
                    illustration.getName(),
                    path
            );
        }

        // Delete animal data
        try {
            animalRepository.deleteById(animalData.getId());
        } catch (Exception e) {
            throw new BaseException("server can't delete data." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // delete image
        deleteImage(animalData.getImageProfile(), path);

        // log send delete animal data
        log.info("Delete animal data:{}", getAnimalDTO);
    }


    // TODO:: Upload image
    @Override
    public void uploadImage(Resource image, String imageName, String path) {
        // create body for fetch image microservice
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // set data to body
        body.add("image", image);
        body.add("imageName", imageName);
        body.add("keyOfField", imageEnvironment.getKeyOfAnimalImageProfile());

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
        } else {
            log.error("error, body of response is null from image microservice.");
            throw new BaseException(
                    "error, body of response is null from image microservice.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    path
            );
        }
    }

    // TODO:: DELETE image
    @Override
    public void deleteImage(String imageName, String path) {
        // create body for fetch image microservice
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // set data to body
        body.add("imageName", imageName);
        body.add("keyOfField", imageEnvironment.getKeyOfAnimalImageProfile());

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