import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

public class Main {

    public static void main(String[] args) {
        testFromPracticum();
        System.out.println("*\n".repeat(5));
        testChangeNameAndDescriptionSingle();
        System.out.println("*\n".repeat(5));
        testChangeNameAndDescriptionEpic();
        System.out.println("*\n".repeat(5));
        testAddIncompatibleClasses();
        System.out.println("*\n".repeat(5));
        testOthers();
    }

    public static void testFromPracticum() {
        Manager manager = new Manager();
        SingleTask singleTask1 = new SingleTask("Задача1", "Описание1");
        SingleTask singleTask2 = new SingleTask("Задача2", "Описание2");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

        System.out.println("Добавление 2 обычных задач:");
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        System.out.println(manager.getAllSingleTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление 2 эпиков:");
        manager.addEpicTask(epicTask1);
        manager.addEpicTask(epicTask2);
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление подзадач для эпиков:");
        EpicTask epicTaskReceived1 = manager.getEpicTask(2);
        EpicTask epicTaskReceived2 = manager.getEpicTask(3);
        SubTask subTask1 = new SubTask("Субтаск1", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask2 = new SubTask("Субтаск2", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask3 = new SubTask("Субтаск3", "Для эпик2", epicTaskReceived2.getId());

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        System.out.println(manager.getAllSubTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Смена статусов");
        SingleTask singleTaskReceived1 = manager.getSingleTask(0);
        SingleTask singleTaskReceived2 = manager.getSingleTask(1);
        singleTaskReceived1.setStatus(Status.IN_PROGRESS);
        singleTaskReceived2.setStatus(Status.DONE);

        SubTask subTaskReceived1 = manager.getSubTask(4);
        SubTask subTaskReceived2 = manager.getSubTask(5);
        SubTask subTaskReceived3 = manager.getSubTask(6);
        subTaskReceived1.setStatus(Status.IN_PROGRESS);
        subTaskReceived2.setStatus(Status.DONE);
        subTaskReceived3.setStatus(Status.DONE);

        System.out.println("До смены:");
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());

        manager.updateSingleTask(singleTaskReceived1);
        manager.updateSingleTask(singleTaskReceived2);
        manager.updateSubTask(subTaskReceived1);
        manager.updateSubTask(subTaskReceived2);
        manager.updateSubTask(subTaskReceived3);

        System.out.println("После смены:");
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление 1 подзадачи, статус эпика должен измениться:");
        manager.removeSubTask(4);
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление 1 сингл и 1 эпика:");
        manager.removeSingleTask(0);
        manager.removeEpicTask(2);
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

    }

    public static void testChangeNameAndDescriptionSingle() {
        Manager manager = new Manager();
        SingleTask singleTask = new SingleTask("Имя до", "Описание до");
        manager.addSingleTask(singleTask);

        System.out.println(manager.getAllSingleTasks());

        SingleTask singleTaskReceived = manager.getSingleTask(0);
        singleTaskReceived.setName("Имя после");
        singleTaskReceived.setDescription("Описание после");
        manager.updateSingleTask(singleTaskReceived);
        System.out.println(manager.getAllSingleTasks());
    }

    public static void testChangeNameAndDescriptionEpic() {
        System.out.println("testChangeNameAndDescriptionEpic");
        Manager manager = new Manager();
        EpicTask epicTask = new EpicTask("Эпик имя до", "Эпик описание до");
        manager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("Суб имя до", "Суб описание до", 0);
        manager.addSubTask(subTask);

        EpicTask epicTaskReceived = manager.getEpicTask(0);
        SubTask subTaskReceived = manager.getSubTask(1);

        epicTaskReceived.setName("Эпик имя после");
        epicTaskReceived.setDescription("Эпик описание после");

        subTaskReceived.setName("Суб имя после");
        subTaskReceived.setDescription("Суб имя после");

        System.out.println(manager.getAllEpicTasks());
        System.out.println(manager.getAllSubTasks());
        manager.updateEpicTask(epicTaskReceived);
        manager.updateSubTask(subTaskReceived);
        System.out.println(manager.getAllEpicTasks());
        System.out.println(manager.getAllSubTasks());
    }

    public static void testAddIncompatibleClasses() {
        System.out.println("testAddIncompatibleClasses");
        Manager manager = new Manager();
        EpicTask epicTask = new EpicTask("Тест", "тест");
        manager.addSingleTask(epicTask);
        System.out.println(manager.getAllSingleTasks());
    }

    public static void testOthers() {
        Manager manager = new Manager();
        SingleTask singleTask1 = new SingleTask("Задача1", "Описание1");
        SingleTask singleTask2 = new SingleTask("Задача2", "Описание2");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

        System.out.println("Добавление 2 обычных задач:");
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        System.out.println(manager.getAllSingleTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление 2 эпиков:");
        manager.addEpicTask(epicTask1);
        manager.addEpicTask(epicTask2);
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление подзадач для эпиков:");
        EpicTask epicTaskReceived1 = manager.getEpicTask(2);
        EpicTask epicTaskReceived2 = manager.getEpicTask(3);
        SubTask subTask1 = new SubTask("Субтаск1", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask2 = new SubTask("Субтаск2", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask3 = new SubTask("Субтаск3", "Для эпик2", epicTaskReceived2.getId());

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        System.out.println(manager.getAllSubTasks());
        System.out.println("===".repeat(3) + "\n");
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());

        System.out.println("Получение подзадач эпика id=2");
        System.out.println(manager.getSubTasksFromEpic(2));
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление подзадач эпика id=2");
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("После");
        manager.clearEpicSubTasks(2);
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление всего:");
        manager.clearEveryTasks();
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        singleTask1 = new SingleTask("Задача1", "Описание1");
        singleTask2 = new SingleTask("Задача2", "Описание2");
        epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        manager.addEpicTask(epicTask1);
        manager.addEpicTask(epicTask2);
        manager.addSubTask(new SubTask("nnn1", "ddd1", 9));
        manager.addSubTask(new SubTask("nnn2", "ddd2", 9));
        manager.addSubTask(new SubTask("nnn3", "ddd3", 10));

        System.out.println("Удаление одиночных задач:");
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("После");
        manager.clearAllSingleTasks();
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление подзадач:");
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("После");
        manager.clearAllSubTasks();
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        manager.addSubTask(new SubTask("nnn1", "ddd1", 9));
        manager.addSubTask(new SubTask("nnn2", "ddd2", 9));
        manager.addSubTask(new SubTask("nnn3", "ddd3", 10));

        System.out.println("Удаление эпиков:");
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("После");
        manager.clearAllEpicTasks();
        System.out.println(manager.getAllSingleTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");
    }
}
