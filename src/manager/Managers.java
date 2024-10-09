package manager;

import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.task.FileBackedTaskManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;
import manager.task.TaskPriorityManager;

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

    public static TaskManager getFileBackendTaskManager(Path path, boolean needsToRestoreTasks) {
        return FileBackedTaskManager.loadFromFile(path, needsToRestoreTasks);
    }

    public static TaskPriorityManager getTaskPriorityManager() {
        return TaskPriorityManager.createTaskPriorityManager();
    }
}
