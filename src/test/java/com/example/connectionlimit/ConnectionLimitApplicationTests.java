package com.example.connectionlimit;

import com.example.connectionlimit.controller.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.Duration.ofSeconds;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class ConnectionLimitApplicationTests {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final List<String> responses = new ArrayList<>();

    @Test
    void testConnectionLimit() throws InterruptedException {
        // make request and make sure we hang there
        makeRequest(new RestTemplate());
        assertTrue(TestController.lock.waitUntilEntered(3));

        // 2nd request should not be processed since connection limit should be 1
        makeRequest(new RestTemplate());
        assertFalse(TestController.lock.waitUntilEntered(3));

        // let 1st request go
        TestController.lock.proceed();
        await().atMost(ofSeconds(3)).until(() -> responses.size() == 1);

        // let 2nd request go
        TestController.lock.proceed();

        await().atMost(ofSeconds(3)).until(() -> responses.size() == 2);
    }

    private void makeRequest(RestTemplate restTemplate) {
        var headers = new HttpHeaders();
        headers.setConnection("close");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        executorService.execute(() -> responses.add(restTemplate.exchange("http://localhost:8080/test", HttpMethod.GET, httpEntity, String.class).getBody()));
    }

}
