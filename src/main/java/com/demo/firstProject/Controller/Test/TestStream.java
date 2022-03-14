package com.demo.firstProject.Controller.Test;

import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.Model.ForTest.ForStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("api/tests/streams")
public class TestStream {

    @GetMapping
    public ResponseEntity ModifyStream() {

        ForStream forStream1 = new ForStream();
        forStream1.setId(1);
        forStream1.setName("banana");
        forStream1.setAge(23);
        forStream1.setDateCreated(LocalDate.now());

        ForStream forStream2 = new ForStream();
        forStream2.setId(2);
        forStream2.setName("hanual");
        forStream2.setAge(30);
        forStream2.setDateCreated(LocalDate.now());

        ForStream forStream3 = new ForStream();
        forStream3.setId(3);
        forStream3.setName("olity");
        forStream3.setAge(17);
        forStream3.setDateCreated(LocalDate.now());

        List<ForStream> myList = new ArrayList<>();
        myList.add(forStream1);
        myList.add(forStream2);
        myList.add(forStream3);

        // TODO:: with map step 1
        List<ForStream> processWithMap_1 = myList.stream().map(value -> value).collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(processWithMap_1);
    }


    @GetMapping("/streams/2")
    public ResponseEntity TestException() throws BaseException, IOException {

        Path getPath = Paths.get("D://");
        Files.delete(getPath);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

}
