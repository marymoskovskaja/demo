package com.example.demo;

import com.example.demo.controllers.TestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
public class TestControllerTests {

    @Autowired
    private TestController testController;

    @Test
    void applicationIsWorkingTest() {
        Assertions.assertTrue(Objects.nonNull(testController.hello()));
    }

}
