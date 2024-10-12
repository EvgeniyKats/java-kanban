package manager.task;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
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
    public void pathShouldBeNotEmpty() {
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
    void shouldBeEqualsStartTimeAndDurationSingleTask() {
        TaskManager manager = Managers.getFileBackendTaskManager(path, false);
        int id = manager.addSingleTask(new SingleTask("",
                "",
                LocalDateTime.of(2024, 10, 12, 22, 0),
                Duration.ofMinutes(1)));
        SingleTask received1 = manager.getSingleTask(id);
        received1.setStatus(Status.IN_PROGRESS);
        manager.updateSingleTask(received1);
        received1 = manager.getSingleTask(id);
        TaskManager manager2 = Managers.getFileBackendTaskManager(path, true);
        SingleTask received2 = manager2.getSingleTask(id);

        assertEquals(received1, received2);
        assertEquals(received1.getStatus(), received2.getStatus());
    }

    @Test
    void shouldBeEqualsStartTimeAndDurationSubTaskAndEpic() {
        TaskManager manager = Managers.getFileBackendTaskManager(path, false);
        EpicTask epicTask = new EpicTask("", "");
        int idEpic = manager.addEpicTask(epicTask);
        int id = manager.addSubTask(new SubTask("",
                "",
                LocalDateTime.of(2024, 10, 12, 22, 0),
                Duration.ofMinutes(1), idEpic));
        SubTask received1 = manager.getSubTask(id);
        received1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(received1);
        received1 = manager.getSubTask(id);
        TaskManager manager2 = Managers.getFileBackendTaskManager(path, true);
        SubTask received2 = manager2.getSubTask(id);

        assertEquals(received1, received2);
        assertEquals(received1.getStartTime(), received2.getStartTime());
        assertEquals(received1.getDuration(), received2.getDuration());

        EpicTask receivedEpic1 = manager.getEpicTask(idEpic);
        EpicTask receivedEpic2 = manager2.getEpicTask(idEpic);
        assertEquals(receivedEpic1, receivedEpic2);
        assertEquals(receivedEpic1.getStartTime(), receivedEpic2.getStartTime());
        assertEquals(receivedEpic1.getDuration(), receivedEpic2.getDuration());
        assertEquals(receivedEpic1.getEndTime(), receivedEpic2.getEndTime());
    }

    @Test
    void taskIdShouldBe1AfterLoadWithoutTasks() {
        TaskManager taskManager1 = Managers.getFileBackendTaskManager(path, false);
        SingleTask task = new SingleTask("", "");
        int id = taskManager1.addSingleTask(task);
        assertEquals(1, id);
    }

    @Test
    void taskIdShouldBe2AfterLoadWithOneTask() {
        taskManager.addSingleTask(new SingleTask("", ""));
        TaskManager taskManager1 = Managers.getFileBackendTaskManager(path, true);
        SingleTask task = new SingleTask("", "");
        int id = taskManager1.addSingleTask(task);
        assertEquals(2, id);
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