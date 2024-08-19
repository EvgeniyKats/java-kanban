import manager.task.InMemoryTaskManager;
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
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        SingleTask singleTask1 = new SingleTask("Задача1", "Описание1");
        SingleTask singleTask2 = new SingleTask("Задача2", "Описание2");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

        System.out.println("Добавление 2 обычных задач:");
        inMemoryTaskManager.addSingleTask(singleTask1);
        inMemoryTaskManager.addSingleTask(singleTask2);
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление 2 эпиков:");
        inMemoryTaskManager.addEpicTask(epicTask1);
        inMemoryTaskManager.addEpicTask(epicTask2);
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление подзадач для эпиков:");
        EpicTask epicTaskReceived1 = inMemoryTaskManager.getEpicTask(3);
        EpicTask epicTaskReceived2 = inMemoryTaskManager.getEpicTask(4);
        SubTask subTask1 = new SubTask("Субтаск1", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask2 = new SubTask("Субтаск2", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask3 = new SubTask("Субтаск3", "Для эпик2", epicTaskReceived2.getId());

        inMemoryTaskManager.addSubTask(subTask1);
        inMemoryTaskManager.addSubTask(subTask2);
        inMemoryTaskManager.addSubTask(subTask3);
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Смена статусов");
        SingleTask singleTaskReceived1 = inMemoryTaskManager.getSingleTask(1);
        SingleTask singleTaskReceived2 = inMemoryTaskManager.getSingleTask(2);
        singleTaskReceived1.setStatus(Status.IN_PROGRESS);
        singleTaskReceived2.setStatus(Status.DONE);

        SubTask subTaskReceived1 = inMemoryTaskManager.getSubTask(5);
        SubTask subTaskReceived2 = inMemoryTaskManager.getSubTask(6);
        SubTask subTaskReceived3 = inMemoryTaskManager.getSubTask(7);
        subTaskReceived1.setStatus(Status.IN_PROGRESS);
        subTaskReceived2.setStatus(Status.DONE);
        subTaskReceived3.setStatus(Status.DONE);

        System.out.println("До смены:");
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());

        inMemoryTaskManager.updateSingleTask(singleTaskReceived1);
        inMemoryTaskManager.updateSingleTask(singleTaskReceived2);
        inMemoryTaskManager.updateSubTask(subTaskReceived1);
        inMemoryTaskManager.updateSubTask(subTaskReceived2);
        inMemoryTaskManager.updateSubTask(subTaskReceived3);

        System.out.println("После смены:");
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление 1 подзадачи, статус эпика должен измениться:");
        inMemoryTaskManager.removeSubTask(5);
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление 1 сингл и 1 эпика:");
        inMemoryTaskManager.removeSingleTask(1);
        inMemoryTaskManager.removeEpicTask(3);
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

    }

    public static void testChangeNameAndDescriptionSingle() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        SingleTask singleTask = new SingleTask("Имя до", "Описание до");
        inMemoryTaskManager.addSingleTask(singleTask);

        System.out.println(inMemoryTaskManager.getAllSingleTasks());

        SingleTask singleTaskReceived = inMemoryTaskManager.getSingleTask(1);
        singleTaskReceived.setName("Имя после");
        singleTaskReceived.setDescription("Описание после");
        inMemoryTaskManager.updateSingleTask(singleTaskReceived);
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
    }

    public static void testChangeNameAndDescriptionEpic() {
        System.out.println("testChangeNameAndDescriptionEpic");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Эпик имя до", "Эпик описание до");
        inMemoryTaskManager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("Суб имя до", "Суб описание до", 1);
        inMemoryTaskManager.addSubTask(subTask);

        EpicTask epicTaskReceived = inMemoryTaskManager.getEpicTask(1);
        SubTask subTaskReceived = inMemoryTaskManager.getSubTask(2);

        epicTaskReceived.setName("Эпик имя после");
        epicTaskReceived.setDescription("Эпик описание после");

        subTaskReceived.setName("Суб имя после");
        subTaskReceived.setDescription("Суб имя после");

        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        inMemoryTaskManager.updateEpicTask(epicTaskReceived);
        inMemoryTaskManager.updateSubTask(subTaskReceived);
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
    }

    public static void testAddIncompatibleClasses() {
        System.out.println("testAddIncompatibleClasses");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Тест", "тест");
        inMemoryTaskManager.addSingleTask(epicTask);
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
    }

    public static void testOthers() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        SingleTask singleTask1 = new SingleTask("Задача1", "Описание1");
        SingleTask singleTask2 = new SingleTask("Задача2", "Описание2");

        EpicTask epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        EpicTask epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

        System.out.println("Добавление 2 обычных задач:");
        inMemoryTaskManager.addSingleTask(singleTask1);
        inMemoryTaskManager.addSingleTask(singleTask2);
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление 2 эпиков:");
        inMemoryTaskManager.addEpicTask(epicTask1);
        inMemoryTaskManager.addEpicTask(epicTask2);
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Добавление подзадач для эпиков:");
        EpicTask epicTaskReceived1 = inMemoryTaskManager.getEpicTask(3);
        EpicTask epicTaskReceived2 = inMemoryTaskManager.getEpicTask(4);
        SubTask subTask1 = new SubTask("Субтаск1", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask2 = new SubTask("Субтаск2", "Для эпик1", epicTaskReceived1.getId());
        SubTask subTask3 = new SubTask("Субтаск3", "Для эпик2", epicTaskReceived2.getId());

        inMemoryTaskManager.addSubTask(subTask1);
        inMemoryTaskManager.addSubTask(subTask2);
        inMemoryTaskManager.addSubTask(subTask3);
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println("===".repeat(3) + "\n");
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());

        System.out.println("Получение подзадач эпика id=2");
        System.out.println(inMemoryTaskManager.getSubTasksFromEpic(3));
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление подзадач эпика id=2");
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("После");
        inMemoryTaskManager.clearEpicSubTasks(3);
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление всего:");
        inMemoryTaskManager.clearEveryTasks();
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        singleTask1 = new SingleTask("Задача1", "Описание1");
        singleTask2 = new SingleTask("Задача2", "Описание2");
        epicTask1 = new EpicTask("Эпик1", "ОписаниеЭпик1");
        epicTask2 = new EpicTask("Эпик2", "ОписаниеЭпик2");

        inMemoryTaskManager.addSingleTask(singleTask1);
        inMemoryTaskManager.addSingleTask(singleTask2);
        inMemoryTaskManager.addEpicTask(epicTask1);
        inMemoryTaskManager.addEpicTask(epicTask2);
        inMemoryTaskManager.addSubTask(new SubTask("nnn1", "ddd1", 10));
        inMemoryTaskManager.addSubTask(new SubTask("nnn2", "ddd2", 10));
        inMemoryTaskManager.addSubTask(new SubTask("nnn3", "ddd3", 11));

        System.out.println("Удаление одиночных задач:");
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("После");
        inMemoryTaskManager.clearAllSingleTasks();
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        System.out.println("Удаление подзадач:");
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("После");
        inMemoryTaskManager.clearAllSubTasks();
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");

        inMemoryTaskManager.addSubTask(new SubTask("nnn1", "ddd1", 10));
        inMemoryTaskManager.addSubTask(new SubTask("nnn2", "ddd2", 10));
        inMemoryTaskManager.addSubTask(new SubTask("nnn3", "ddd3", 11));

        System.out.println("Удаление эпиков:");
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("После");
        inMemoryTaskManager.clearAllEpicTasks();
        System.out.println(inMemoryTaskManager.getAllSingleTasks());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("===".repeat(3) + "\n");
    }
}
