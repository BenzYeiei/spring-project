package com.demo.firstProject.Service.Resource.Animals;

import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.AnimalCategoryEntity;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.JPA.Entity.AnimalIllustrationEntity;
import com.demo.firstProject.JPA.Repository.Account.AccountRepository;
import com.demo.firstProject.JPA.Repository.AnimalCategoryRepository;
import com.demo.firstProject.JPA.Repository.AnimalRepository;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import com.demo.firstProject.Service.ServiceModel.AnimalService.AnimalModel_CRUD;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalsService implements AnimalModel_CRUD {

    private final AccountRepository accountRepository;
    private final AnimalRepository animalRepository;
    private final AnimalCategoryRepository animalCategoryRepository;
    private final AnimalIllustrationService animalIllustrationService;
    private final ImageService imageService;

    public AnimalsService(AccountRepository accountRepository, AnimalRepository animalRepository, AnimalCategoryRepository animalCategoryRepository, AnimalIllustrationService animalIllustrationService, ImageService imageService) {
        this.accountRepository = accountRepository;
        this.animalRepository = animalRepository;
        this.animalCategoryRepository = animalCategoryRepository;
        this.animalIllustrationService = animalIllustrationService;
        this.imageService = imageService;
    }


    // TODO:: Get List Animal
    @Override
    public List<AnimalEntity> AnimalService_GetList() {
        List<AnimalEntity> animal = animalRepository.findAll();
        return animal;
    }


    // TODO:: Get One Animal by Id
    @Override
    public AnimalEntity AnimalService_GetOneById(long id) {
        // Check exists by id
        boolean isAnimalEntity = animalRepository.existsById(id);
        if (!isAnimalEntity) {
            throw new BaseException("api.animals.get.field.id.not-found", HttpStatus.NOT_FOUND);
        }
        return animalRepository.getById(id);
    }


    // TODO:: Create Animal
    @Override
    public AnimalEntity AnimalService_Create(
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
            throw new BaseException("field name null.", HttpStatus.BAD_REQUEST, path);
        }

        // check field category not null
        if (animalCategory == null) {
            throw new BaseException("field category null.", HttpStatus.BAD_REQUEST, path);
        }

        // check field profile image not null
        if (imageProfile == null) {
            throw new BaseException("field imageProfile null.", HttpStatus.BAD_REQUEST, path);
        }

        // check length of name equal 30
        if (name.length() > 30) {
            throw new BaseException("field name length size 30.", HttpStatus.BAD_REQUEST, path);
        }

        // get category data
        Optional<AnimalCategoryEntity> animalCategoryOptional = animalCategoryRepository.findByCategoryName(animalCategory.toLowerCase());

        // check category exists
        if (animalCategoryOptional.isEmpty()) {
            throw new BaseException("field category not corresponding.", HttpStatus.BAD_REQUEST, path);
        }

        // check account of owner
        Optional<AccountEntity> accountResult = accountRepository.findById(principal.getName());
        AccountEntity accountData = accountResult.get();
        if (accountResult.isEmpty()) {
            throw new BaseException("account name not coorect.", HttpStatus.BAD_REQUEST, path);
        }

        // create name image profile
        String imageProfileName = imageService.getImageName("animal-profile", imageProfile.getOriginalFilename());

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
        } catch(RuntimeException exception) {
            throw new BaseException("Server message: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // create path image
        Path newPath = Path.of(imageService.getDir_name_animal() + imageProfileName);

        // upload image
        try{
            Files.copy(imageProfile.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException("can't upload image. System:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // create illustration
        boolean isSaveIll = animalIllustrationService.AnimalIllustrationService_Create(animalResultData, accountData, illustrationFiles, path);

        if (isSaveIll) {
            return animalResultData;
        } else {
            return animalResultData;
        }
    }


    // TODO:: Update Animal
    @Override
    public AnimalEntity AnimalService_Update(
            long id,
            String name,
            String animalCategory,
            int quantity,
            boolean statusState,
            MultipartFile imageProfile,
            List<MultipartFile> illustrationFiles,
            String path,
            Principal principal
    ) {
        // get old animal data
        Optional<AnimalEntity> animalOptional = animalRepository.findById(id);

        // search animal by id
        if (animalOptional.isEmpty()) {
            throw new BaseException("animal data is not found.", HttpStatus.NOT_FOUND, path);
        }

        // get old data
        AnimalEntity animal_OldData = animalOptional.get();

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
            String imageProfileName = imageService.getImageName("animal-profile", imageProfile.getOriginalFilename());

            // create image path
            Path imageProfilePath = Path.of(imageService.getDir_name_animal() + "/" + imageProfileName);

            // set data of image file name to object
            animal_OldData.setImageProfile(imageProfileName);

            // Save
            try {
                animalNewData = animalRepository.save(animal_OldData);
            } catch (Exception e) {
                throw new BaseException("Server message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
            }

            // upload image
            try {
                Files.copy(imageProfile.getInputStream(), imageProfilePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(Path.of(imageService.getDir_name_animal() + "/" + old_nameImage));
            } catch (Exception e) {
                throw new BaseException("can't upload image. System:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // return animal id
            return animalNewData;
        }

        // Save
        try {
            animalNewData = animalRepository.save(animal_OldData);
        } catch (Exception e) {
            throw new BaseException("Server message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
        }

        // return animal id
        return animalNewData;
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

        // check own data
        if (!animalData.getCreatedByUser().getId().equals(principal.getName())) {
            throw new BaseException("This account not delete data.", HttpStatus.FORBIDDEN, path);
        }

        // Delete animal
        try {

            List<AnimalIllustrationEntity> animalIllustrationList = animalIllustrationService.AnimalIllustrationService_GetListById(animalData);

            for (AnimalIllustrationEntity illustration : animalIllustrationList) {
                animalIllustrationService.AnimalIllustrationService_Delete(animalData.getId(), illustration.getId(), path);
            }

            animalRepository.deleteById(id);

            Files.delete(Path.of(imageService.getDir_name_animal() + "/" + animalData.getImageProfile()));

        } catch (Exception e) {
            throw new BaseException("server can't delete data." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
