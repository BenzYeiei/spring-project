package com.service.backend.Controller.Animals;

import com.service.backend.DTO.Animals.AnimalDTO;
import com.service.backend.Exception.BaseException;
import com.service.backend.Service.Resource.Animals.AnimalIllustrationService;
import com.service.backend.Service.Resource.Animals.AnimalService;
import com.service.backend.Service.Resource.Image.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/animals")
@Slf4j
public record AnimalController(AnimalService animalService,
                               AnimalIllustrationService animalIllustrationService,
                               ImageService imageService) {


    // TODO:: Get Page
    @GetMapping("/page")
    public ResponseEntity<Page> AnimalGet_Page(
            @RequestParam(name = "number", required = false) String pageNum, // variable Sting because can't throw error if int not assign data
            HttpServletRequest request
    ) {

        // check null of numPage
        if (pageNum == null) {
            log.error("number of page is null.");
            throw new BaseException("must send number of page.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        //convert String to int
        int convertPageNum;
        try {
            convertPageNum = Integer.parseInt(pageNum);
        } catch (NumberFormatException e) {
            log.error("number params not integer.");
            throw new BaseException("must assign number params with integer", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // get page from service
        Page<AnimalDTO> page = animalService.AnimalService_GetPage(convertPageNum);

        return ResponseEntity.ok().body(page);
    }


    // TODO: GET -> get one by id
    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> AnimalAPI_GetById(@PathVariable long id) {

        // get animal dto from service
        AnimalDTO animal = animalService.AnimalService_GetOneById(id);

        return ResponseEntity.status(HttpStatus.OK).body(animal);
    }


    // TODO: POST Create Animal
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<AnimalDTO> AnimalCrate(
            @RequestPart(name = "name") String name,
            @RequestPart(name = "category") String animalCategory,
            @RequestPart(name = "quantity") String quantity,
            @RequestPart(name = "imageProfile") MultipartFile imageProfile,
            @RequestPart(name = "illustration") List<MultipartFile> illustrationFiles,
            HttpServletRequest request,
            Principal principal
    ) {

        // create animal data
        AnimalDTO animalResult = animalService.AnimalService_Create(
                name,
                animalCategory,
                Integer.parseInt(quantity),
                imageProfile,
                illustrationFiles,
                request.getServletPath(),
                principal
        );

        AnimalDTO animal = animalService.AnimalService_GetOneById(animalResult.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }


    // TODO: PUT Update Animal
    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> AnimalUpdate(
            @PathVariable long id,
            @RequestPart(name = "name") String name,
            @RequestPart(name = "category") String animalCategory,
            @RequestPart(name = "quantity") String quantity,
            @RequestPart(name = "statusState") String statusState,
            @RequestPart(name = "imageProfile") MultipartFile imageProfile,
            HttpServletRequest request,
            Principal principal
    ) {
        AnimalDTO animalResult = animalService.AnimalService_Update(
                id,
                name,
                animalCategory,
                Integer.parseInt(quantity),
                Boolean.parseBoolean(statusState),
                imageProfile,
                request.getServletPath(),
                principal
        );

        return ResponseEntity.status(HttpStatus.OK).body(animalResult);
    }


    // TODO: DELETE -> Delete Animal
    @DeleteMapping("/{id}")
    public ResponseEntity<ArrayList<AnimalDTO>> AnimalDelete(@PathVariable long id, Principal principal, HttpServletRequest request) {
        animalService.AnimalService_Delete(id, principal, request.getServletPath());
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<AnimalDTO>());
    }

}
