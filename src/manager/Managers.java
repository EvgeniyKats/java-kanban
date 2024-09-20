package manager;

import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.task.FileBackedTaskManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

import java.nio.file.Path;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getFileBackendTaskManager(Path path) {
        return FileBackedTaskManager.loadFromFile(path, true);
    }
}
