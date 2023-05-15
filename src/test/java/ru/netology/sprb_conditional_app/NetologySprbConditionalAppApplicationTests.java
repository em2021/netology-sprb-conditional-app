package ru.netology.sprb_conditional_app;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NetologySprbConditionalAppApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    private static GenericContainer<?> devApp = new GenericContainer<>("devapp")
            .withExposedPorts(8080);
    private static GenericContainer<?> prodApp = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        devApp.start();
        prodApp.start();
    }

    @AfterAll
    public static void completed() {
        devApp.stop();
        prodApp.stop();
    }

    @ParameterizedTest
    @MethodSource("argsForContextLoads")
    void contextLoads(GenericContainer<?> app, int port, String expected) {
        //when:
        ResponseEntity<String> forEntity =
                restTemplate.getForEntity("http://localhost:" + app.getMappedPort(port) + "/profile", String.class);
        //then:
        String actual = forEntity.getBody();
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> argsForContextLoads() {
        //given:
        return Stream.of(
                Arguments.of(devApp, 8080, "Current profile is dev"),
                Arguments.of(prodApp, 8081, "Current profile is production"));
    }
}
