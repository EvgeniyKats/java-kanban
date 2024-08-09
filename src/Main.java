import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.Task;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        testFromPracticum(manager);
        System.out.println("*\n".repeat(3));

        Task task1 = new Task("Одиночная задача1", "Описание одиночной задачи1");
        Task task2 = new Task("Одиночная задача2", "Описание одиночной задачи2");
        Task task3 = new Task("Одиночная задача3", "Описание одиночной задачи3");

        EpicTask epicTask1 = new EpicTask("Покупка машины", "Покупка машины до 20xx г.");
        EpicTask epicTask2 = new EpicTask("Покупка квартиры", "Покупка квартиры до 20yy г.");

        SubTask subTask1 = new SubTask("Посмотреть цены", "Авито");
        SubTask subTask2 = new SubTask("Посмотреть цены", "АвтоРу");
        SubTask subTask3 = new SubTask("Посмотреть цены", "Яндекс");

        List<SubTask> listSubTasksCar = new ArrayList<>();
        listSubTasksCar.add(subTask1);
        listSubTasksCar.add(subTask2);
        listSubTasksCar.add(subTask3);

        List<SubTask> listSubTasksFlat = new ArrayList<>(listSubTasksCar);

        manager.addTaskToMap(task1);
        manager.addTaskToMap(task2);
        manager.addTaskToMap(task3);
        manager.addTaskToMap(epicTask1);
        manager.addTaskToMap(subTask1);
        manager.addTaskToMap(subTask2);
        manager.addTaskToMap(subTask3);
        System.out.println("=".repeat(20));

        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        System.out.println("manager.getTask(3) = " + manager.getTask(3));
        EpicTask epicTask = (EpicTask) manager.getTask(3);
        manager.addTaskToMap(epicTask);

        EpicTask updatedEpicTask = epicTask.getCopy();
        updatedEpicTask.addSubTask(listSubTasksCar.get(0));
        updatedEpicTask.addSubTask(listSubTasksCar.get(2));

        manager.updateTask(updatedEpicTask, epicTask.getId(), epicTask);
        System.out.println("manager.getTask(3) = " + manager.getTask(3));
        epicTask = (EpicTask) manager.getTask(3);
        SubTask subForDelete = epicTask.getSubTasks().getFirst();
        updatedEpicTask = epicTask.getCopy();
        updatedEpicTask.removeSubTask(subForDelete); // updatedEpicTask.removeSubTask(4); / updatedEpicTask.removeSubTask(subForDelete);
        updatedEpicTask.removeSubTask(99);
        updatedEpicTask.addSubTask(new SubTask("n", "d", Status.DONE, null, updatedEpicTask.getId()));
        manager.updateTask(updatedEpicTask, epicTask.getId(), epicTask);
        System.out.println("manager.getTask(3) = " + manager.getTask(3));
        System.out.println("Удаление задачи из Epic со статусом NEW");
        /*
        Удаление задачи из Epic со статусом NEW
         */
        manager.removeTask(5);
        System.out.println("manager.getTask(3) = " + manager.getTask(3));


        System.out.println("===".repeat(3));
        System.out.println("Добавление Epic с уже готовыми сабтасками");
        /*
        Добавление Epic с уже готовыми сабтасками
         */
        epicTask2.addSubTask(listSubTasksFlat.get(0));
        epicTask2.addSubTask(listSubTasksFlat.get(1));
        epicTask2.addSubTask(listSubTasksFlat.get(2));
        manager.addTaskToMap(epicTask2);
        System.out.println("manager.getTask(4) = " + manager.getTask(4));

        System.out.println("===".repeat(3));
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        System.out.println("Удаление id1");
        /*Удаление id1*/
        manager.removeTask(1);
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());

        System.out.println("===".repeat(3));
        System.out.println("Удаление эпика целиком");
        // Удаление эпика целиком
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        manager.removeTask(4);
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());

        System.out.println("===".repeat(3));
        System.out.println("Добавление новой задачи - должна занять ближайший индекс (1)");
        // Добавление новой задачи - должна занять ближайший индекс (1)
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        manager.addTaskToMap(new Task("Новая задача", "Должен быть индекс 1"));
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());

        System.out.println("===".repeat(3));
        System.out.println("Смена статуса задачи 1");
        Task task = manager.getTask(1);
        Task updated = new Task(task.getName(), task.getDescription(), Status.IN_PROGRESS, task.getId());
        manager.updateTask(updated, task.getId(), task);
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        System.out.println("Очистка хранилища");
        manager.clearAllTasks();
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        manager.updateTask(updated, task.getId(), task);

    }

    public static void testFromPracticum(Manager manager) {
        Task task1 = new Task("Задача1", "Описание1");
        Task task2 = new Task("Задача2", "Описание2");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

        SubTask subTask1 = new SubTask("Субтаск1", "Для эпик1");
        SubTask subTask2 = new SubTask("Субтаск2", "Для эпик1");
        SubTask subTask3 = new SubTask("Субтаск3", "Для эпик2");

        manager.addTaskToMap(task1);
        manager.addTaskToMap(task2);

        epicTask1.addSubTask(subTask1);
        epicTask1.addSubTask(subTask2);
        manager.addTaskToMap(epicTask1);

        manager.addTaskToMap(epicTask2);

        EpicTask epicGet = (EpicTask) manager.getTask(5);
        epicGet.addSubTask(subTask3);
        manager.updateTask(epicGet, 5, manager.getTask(5));

        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        Task single1 = manager.getTask(0);
        Task single2 = manager.getTask(1);

        Task input1 = new Task(single1.getName(), single1.getDescription(), Status.IN_PROGRESS, single1.getId());
        Task input2 = new Task(single2.getName(), single2.getDescription(), Status.DONE, single2.getId());

        SubTask sub1 = (SubTask) manager.getTask(4);
        SubTask sub2 = (SubTask) manager.getTask(6);

        SubTask input3 = new SubTask(sub1.getName(), sub1.getDescription(), Status.IN_PROGRESS, sub1.getId(), sub1.getEpicId());
        SubTask input4 = new SubTask(sub2.getName(), sub2.getDescription(), Status.DONE, sub2.getId(), sub2.getEpicId());

        manager.updateTask(input1, single1.getId(), single1);
        manager.updateTask(input2, single2.getId(), single2);
        manager.updateTask(input3, sub1.getId(), sub1);
        manager.updateTask(input4, sub2.getId(), sub2);
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());
        manager.removeTask(5);
        manager.removeTask(0);
        System.out.println("manager.getAllTasks() = " + manager.getAllTasks());

        manager.clearAllTasks();
    }
}
