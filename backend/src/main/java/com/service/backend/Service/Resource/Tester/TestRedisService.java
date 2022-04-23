package com.service.backend.Service.Resource.Tester;

import com.service.backend.Model.TestRedisModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestRedisService {

    private TestRedisModel testRedisModel;

    public TestRedisService() {
        this.testRedisModel = new TestRedisModel();
        this.testRedisModel.setId("1");
        this.testRedisModel.setName("Momo");
    }

    @Cacheable(value = "tester", key = "#id", unless = "#result==null")
    public TestRedisModel TestRedisService_getData(String id) {

        TestRedisModel data = this.testRedisModel;

        log.info("id:{}, data:{}",id, data);

        return data;
    }

}
