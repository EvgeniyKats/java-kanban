import manager.Managers;
import manager.task.TaskManager;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Path path;
        try {
            path = Files.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TaskManager manager = Managers.getFileBackendTaskManager(path);
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

        TaskManager manager2 = Managers.getFileBackendTaskManager(path);
        List<SingleTask> singleTasks = manager.getAllSingleTasks();
        List<SingleTask> singleTasks2 = manager2.getAllSingleTasks();

        System.out.println("SingleTasks equals=" + singleTasks.equals(singleTasks2));

        List<EpicTask> epicTasks = manager.getAllEpicTasks();
        List<EpicTask> epicTasks2 = manager2.getAllEpicTasks();

        System.out.println("EpicTasks equals=" + epicTasks.equals(epicTasks2));

        List<SubTask> subTasks = manager.getAllSubTasks();
        List<SubTask> subTasks2 = manager2.getAllSubTasks();

        System.out.println("SubTasks equals=" + subTasks.equals(subTasks2));
    }
}
