package com.example.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("props")
@Slf4j
public class TestController {

    @Autowired
    PropertiesHolder holder;

    @Autowired
    PropertyAccessor accessor;

    // GET http://localhost:8080/props/get/100/k1
    @RequestMapping("get/{loop}/{key}")
    @ResponseBody
    public void getProperty(@PathVariable("loop") int loopNum, @PathVariable("key") String key) throws InterruptedException {

        for (int i = 0; i < loopNum; i++) {
            log.info("count:{}, key:{}, value:{}", i, key, accessor.get(key));
            TimeUnit.SECONDS.sleep(1);
        }
    }

    // POST http://localhost:8080/props/update/k1/v100
    @RequestMapping("update/{key}/{value}")
    @ResponseBody
    public void updateProperty(@PathVariable("key") String key, @PathVariable("value") String value) {
        holder.update(key, value);
    }

}
