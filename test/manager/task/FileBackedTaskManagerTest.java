package manager.task;

import manager.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileBackedTaskManagerTest extends TaskManagerTest {

    @Override
    public void beforeEach() {
        try {
            taskManager = Managers.getFileBackendTaskManager(Files.createTempFile("test", ".csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}