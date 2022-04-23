package com.service.backend.Controller.Test;

import com.service.backend.Component.ImageEnvironment;
import com.service.share.Animal.AnimalHello;
import com.service.share.Animal.AnimalMicroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(path = "/api/tests/connect-microservice")
@Slf4j
@RestController
public class UseMicroservice {

    private final RestTemplate restTemplate;
    private final ImageEnvironment imageEnvironment;

    public UseMicroservice(RestTemplate restTemplate, ImageEnvironment imageEnvironment) {
        this.restTemplate = restTemplate;
        this.imageEnvironment = imageEnvironment;
    }

    @GetMapping(path = "")
    public ResponseEntity<String> connectHelloImage() {

        ResponseEntity<AnimalHello> animalHello = restTemplate.getForEntity(
                "http://127.0.0.1:8082/api/vi/testers/hello",
                AnimalHello.class
        );

        log.info("response:{}", animalHello);

        return ResponseEntity.ok().body(animalHello.getBody().getMessage());
    }

    @GetMapping(path = "/withParam")
    public ResponseEntity<String> connectParamImage() {

        ResponseEntity<AnimalHello> animalHello = restTemplate.getForEntity(
                "http://127.0.0.1:8082/api/vi/testers/param?name={name}",
                AnimalHello.class,
                "BarenBenz"
        );

        log.info("response:{}", animalHello);

        return ResponseEntity.ok().body(animalHello.getBody().getMessage());
    }

    @PostMapping(path = "/abc")
    public ResponseEntity<String> test88(
            @RequestPart(name = "file") MultipartFile file
            ) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

//        try {
//
//        } catch (IOException e) {
//            log.error("{}", e.getMessage());
//        }
        body.add("image", file.getResource());
        body.add("imageName", file.getOriginalFilename());
        body.add("keyOfField", "animal_imageProfile");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> HttpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<AnimalMicroResponse> animalHello = restTemplate.postForEntity(
                "http://127.0.0.1:8082/api/vi/animals/upload",
                HttpEntity,
                AnimalMicroResponse.class
        );

        return ResponseEntity.ok().body(null);
    }

}
