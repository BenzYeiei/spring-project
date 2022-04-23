package com.service.image.Controller;

import com.service.share.Animal.AnimalHello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping(path = "/api/vi/testers")
@RestController
public class Tester {

    @GetMapping(path = "/hello")
    public ResponseEntity<AnimalHello> helloImageMicroservice() {

        AnimalHello animalHello = new AnimalHello();

        animalHello.setMessage("Hello image microservice.");

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            log.error("sleep can't executing. message:{}", e.getMessage());
        }

        return ResponseEntity.ok().body(animalHello);
    }

    @GetMapping(path = "/param")
    public ResponseEntity<AnimalHello> paramsImageMicroservice(
            @RequestParam(name = "name") String param
    ) {

        AnimalHello animalHello = new AnimalHello();

        animalHello.setMessage("Hello image microservice get param " + param + ".");

        return ResponseEntity.ok().body(animalHello);
    }
}
