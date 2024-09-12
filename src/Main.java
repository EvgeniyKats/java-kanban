import manager.task.InMemoryTaskManager;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        SingleTask singleTask1 = new SingleTask("Задача1", "Описание1");
        SingleTask singleTask2 = new SingleTask("Задача2", "Описание2");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

//        Добавление 2 обычных задач:
        inMemoryTaskManager.addSingleTask(singleTask1);
        inMemoryTaskManager.addSingleTask(singleTask2);

//        Добавление 2 эпиков:
        inMemoryTaskManager.addEpicTask(epicTask1);
        inMemoryTaskManager.addEpicTask(epicTask2);

//        Добавление 3 подзадач для эпика 1:
        EpicTask epicTaskReceived1 = inMemoryTaskManager.getEpicTask(3);
        SubTask subTask1 = new SubTask("Сабтаск1", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask2 = new SubTask("Сабтаск2", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask3 = new SubTask("Сабтаск3", "Для эпик1", epicTaskReceived1.getId());

        inMemoryTaskManager.addSubTask(subTask1);
        inMemoryTaskManager.addSubTask(subTask2);
        inMemoryTaskManager.addSubTask(subTask3);

        List<SingleTask> singleTasks = inMemoryTaskManager.getAllSingleTasks();
        List<SubTask> subTasks = inMemoryTaskManager.getAllSubTasks();
        List<EpicTask> epicTasks = inMemoryTaskManager.getAllEpicTasks();
        System.out.println("Проверка сценария (история по порядку):");
        for (SingleTask singleTask : singleTasks) {
            inMemoryTaskManager.getSingleTask(singleTask.getId());
        }
        for (SubTask subTask : subTasks) {
            inMemoryTaskManager.getSubTask(subTask.getId());
        }
        for (EpicTask epicTask : epicTasks) {
            inMemoryTaskManager.getEpicTask(epicTask.getId());
        }
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Проверка сценария (история в обратном порядке):");
        for (int i = subTasks.size() - 1; i >= 0; i--) {
            inMemoryTaskManager.getSubTask(subTasks.get(i).getId());
        }
        for (int i = epicTasks.size() - 1; i >= 0; i--) {
            inMemoryTaskManager.getEpicTask(epicTasks.get(i).getId());
        }
        for (int i = singleTasks.size() - 1; i >= 0; i--) {
            inMemoryTaskManager.getSingleTask(singleTasks.get(i).getId());
        }
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Проверка сценария SubTask должен стать последним в истории:");
        inMemoryTaskManager.getSubTask(subTasks.getFirst().getId());
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Проверка сценария Epic и его сабтаски должны быть удалены из истории:");
        inMemoryTaskManager.removeEpicTask(epicTaskReceived1.getId());
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
