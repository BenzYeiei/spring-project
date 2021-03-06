package com.service.backend.Controller.Test;

import com.service.backend.Model.TestRedisModel;
import com.service.backend.Service.Resource.Tester.TestRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/tests/redis")
public class TestRedis {

    private final TestRedisService testRedisService;

    public TestRedis(TestRedisService testRedisService) {
        this.testRedisService = testRedisService;
    }

    @GetMapping(path = "")
    public ResponseEntity<TestRedisModel> TestRedis_getData() {


       TestRedisModel data = testRedisService.TestRedisService_getData("1");

        return ResponseEntity.ok().body(data);
    }

}
