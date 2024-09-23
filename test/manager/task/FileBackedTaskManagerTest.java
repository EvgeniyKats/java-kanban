package manager.task;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends AbstractTaskManagerTest<FileBackedTaskManager> {

    Path path;

    @BeforeEach
    public void beforeEach() {
        try {
            path = Files.createTempFile("test", ".csv");
            taskManager = (FileBackedTaskManager) Managers.getFileBackendTaskManager(path, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveCheckNotEmpty() {
        try {
            assertEquals("", Files.readString(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SingleTask singleTask = new SingleTask("", "");
        taskManager.addSingleTask(singleTask);
        EpicTask epicTask = new EpicTask("", "");
        taskManager.addEpicTask(epicTask);

        try {
            assertNotEquals("", Files.readString(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldBeEqualsAfterSaveAndLoad() {
        Path path;
        try {
            path = Files.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TaskManager manager = Managers.getFileBackendTaskManager(path, false);
        SingleTask singleTask1 = new SingleTask("Задача1", "Описание1");
        SingleTask singleTask2 = new SingleTask("Задача2", "Описание2");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

//        Добавление 2 обычных задач:
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

//        Добавление 2 эпиков:
        manager.addEpicTask(epicTask1);
        manager.addEpicTask(epicTask2);

//        Добавление 3 подзадач для эпика 1:
        EpicTask epicTaskReceived1 = manager.getEpicTask(3);
        SubTask subTask1 = new SubTask("Сабтаск1", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask2 = new SubTask("Сабтаск2", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask3 = new SubTask("Сабтаск3", "Для эпик1", epicTaskReceived1.getId());

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        taskManager = (FileBackedTaskManager) Managers.getFileBackendTaskManager(path, true);
        List<SingleTask> singleTasks = manager.getAllSingleTasks();
        List<SingleTask> singleTasks2 = taskManager.getAllSingleTasks();

        assertEquals(singleTasks, singleTasks2);

        List<EpicTask> epicTasks = manager.getAllEpicTasks();
        List<EpicTask> epicTasks2 = taskManager.getAllEpicTasks();

        assertEquals(epicTasks, epicTasks2);

        List<SubTask> subTasks = manager.getAllSubTasks();
        List<SubTask> subTasks2 = taskManager.getAllSubTasks();

        assertEquals(subTasks, subTasks2);
    }

    @Test
    public void shouldBeEqualsAfterSaveAndLoadWithDifferenceStatus() {
        Path path;
        try {
            path = Files.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TaskManager manager = Managers.getFileBackendTaskManager(path, false);
        SingleTask singleTask1 = new SingleTask("Задача1", "Описание1");
        SingleTask singleTask2 = new SingleTask("Задача2", "Описание2");
        SingleTask singleTask3 = new SingleTask("Задача3", "Описание3");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

//        Добавление 3 обычных задач:
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        manager.addSingleTask(singleTask3);

//        Добавление 2 эпиков:
        manager.addEpicTask(epicTask1);
        manager.addEpicTask(epicTask2);

//        Добавление 3 подзадач для эпика 1:
        EpicTask epicTaskReceived1 = manager.getEpicTask(4);
        SubTask subTask1 = new SubTask("Сабтаск1", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask2 = new SubTask("Сабтаск2", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask3 = new SubTask("Сабтаск3", "Для эпик1", epicTaskReceived1.getId());

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        List<SingleTask> receivedSingleTasks = manager.getAllSingleTasks();
        List<SubTask> receivedSubTasks = manager.getAllSubTasks();
        receivedSingleTasks.get(1).setStatus(Status.IN_PROGRESS);
        receivedSingleTasks.get(2).setStatus(Status.DONE);
        receivedSubTasks.get(1).setStatus(Status.IN_PROGRESS);
        receivedSubTasks.get(2).setStatus(Status.DONE);
        manager.updateSingleTask(receivedSingleTasks.get(1));
        manager.updateSingleTask(receivedSingleTasks.get(2));
        manager.updateSubTask(receivedSubTasks.get(1));
        manager.updateSubTask(receivedSubTasks.get(2));

        TaskManager manager2 = Managers.getFileBackendTaskManager(path, true);
        List<SingleTask> singleTasks = manager.getAllSingleTasks();
        List<SingleTask> singleTasks2 = manager2.getAllSingleTasks();

        assertEquals(singleTasks, singleTasks2);

        List<EpicTask> epicTasks = manager.getAllEpicTasks();
        List<EpicTask> epicTasks2 = manager2.getAllEpicTasks();

        assertEquals(epicTasks, epicTasks2);

        List<SubTask> subTasks = manager.getAllSubTasks();
        List<SubTask> subTasks2 = manager2.getAllSubTasks();

        assertEquals(subTasks, subTasks2);
    }

    @Test
    void shouldBeEmptyIfWithoutLoad() {
        taskManager.addSingleTask(new SingleTask("", ""));

        TaskManager withLoad = Managers.getFileBackendTaskManager(path, true);
        TaskManager withoutLoad = Managers.getFileBackendTaskManager(path, false);
        assertEquals(1, withLoad.getAllSingleTasks().size());
        assertEquals(0, withoutLoad.getAllSingleTasks().size());
    }
}