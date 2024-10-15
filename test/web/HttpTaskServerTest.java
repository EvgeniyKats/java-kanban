package web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    @AfterEach
    void afterEach() {
        HttpTaskServer.getInstance().stop();
    }

    @Test
    void shouldBeNotNullGetInstance() {
        assertNotNull(HttpTaskServer.getInstance());
    }

    @Test
    void shouldBeNoBindExceptionIfDoubleStart() {
        assertDoesNotThrow(() -> HttpTaskServer.getInstance().start());
        assertDoesNotThrow(() -> HttpTaskServer.getInstance().start());
    }

    @Test
    void shouldBeTrueAndFalseAfterDoubleStart() throws IOException {
        assertTrue(HttpTaskServer.getInstance().start());
        assertFalse(HttpTaskServer.getInstance().start());
    }

    @Test
    void shouldStartAfterStop() throws IOException {
        assertTrue(HttpTaskServer.getInstance().start());
        HttpTaskServer.getInstance().stop();
        assertTrue(HttpTaskServer.getInstance().start());
    }

}
