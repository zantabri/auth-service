package com.zantabri.auth_service.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.DefaultResponseErrorHandler;

//@ActiveProfiles("test")
@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PingControllerTest {

    @LocalServerPort
    public int port;

    public static String baseUrl;


    @BeforeEach
    public void beforeEach() {
        baseUrl = new StringBuilder().append("http://localhost:").append(port).toString();
    }

    @Transactional()
    @Test
    public void testPositive() {

        TestRestTemplate testRestTemplate = new TestRestTemplate();

        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.concat("/ping"), String.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("pong!", response.getBody())
        );


    }

}
