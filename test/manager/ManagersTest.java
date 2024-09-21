package manager;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    void getFileBackendTaskManager() {
        try {
            assertNotNull(Managers.getFileBackendTaskManager(Files.createTempFile("test", ".csv"), true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}