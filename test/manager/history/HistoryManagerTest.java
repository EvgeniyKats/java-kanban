package manager.history;

import manager.Managers;
import manager.task.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;
    TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldNotContainTaskInHistory() {
        SingleTask singleTask = new SingleTask("testN", "testD");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addSingleTask(singleTask);
        SingleTask singleTaskReceived = taskManager.getAllSingleTasks().getFirst();

        for (int i = 0; i < 10; i++) {
            taskManager.addSingleTask((new SingleTask(i + "n", i + "d")));
        }
        assertFalse(historyManager.getHistory().contains(singleTaskReceived));
    }

    @Test
    void shouldBe0TasksInHistory() {
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void shouldBe1TasksInHistoryAfterAdd1() {
        SingleTask singleTask = new SingleTask("n", "d");
        singleTask.setId(0);
        historyManager.add(singleTask);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldBe10TasksInHistoryAfterAdd10() {
        for (int i = 0; i < 10; i++) {
            SingleTask singleTask = new SingleTask(i + "n", i + "d");
            singleTask.setId(i);
            historyManager.add(singleTask);
        }
        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    void shouldBe111TasksInHistoryAfterAdd11() {
        for (int i = 0; i < 111; i++) {
            SingleTask singleTask = new SingleTask(i + "n", i + "d");
            singleTask.setId(i);
            historyManager.add(singleTask);
        }
        assertEquals(111, historyManager.getHistory().size());
    }

    @Test
    void checkSpamOneTaskStability() {
        taskManager.addSingleTask(new SingleTask("name", "desc"));

        assertEquals(0, taskManager.getHistory().size());
        for (int i = 0; i < 111; i++) {
            taskManager.getSingleTask(1);
        }
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void checkOrderOfTasksWithGetSinge() {
        SingleTask singleTask1 = new SingleTask("1n", "1d");
        SingleTask singleTask2 = new SingleTask("2n", "2d");
        SingleTask singleTask3 = new SingleTask("3n", "3d");
        SingleTask singleTask4 = new SingleTask("4n", "4d");
        taskManager.addSingleTask(singleTask1);
        taskManager.addSingleTask(singleTask2);
        taskManager.addSingleTask(singleTask3);
        taskManager.addSingleTask(singleTask4);

        for (int i = 1; i <= 4; i++) {
            SingleTask received1 = taskManager.getSingleTask(1);
            assertEquals(received1, taskManager.getHistory().getLast());
            SingleTask received2 = taskManager.getSingleTask(2);
            assertEquals(received2, taskManager.getHistory().getLast());
            SingleTask received3 = taskManager.getSingleTask(3);
            assertEquals(received3, taskManager.getHistory().getLast());
            SingleTask received4 = taskManager.getSingleTask(4);
            assertEquals(received4, taskManager.getHistory().getLast());
            SingleTask iterationTask = taskManager.getSingleTask(i);
            assertEquals(iterationTask, taskManager.getHistory().getLast());
            assertEquals(4, taskManager.getHistory().size());
        }
    }


    @Test
    void checkOrderOfTasksWithRemoveSingle() {
        SingleTask singleTask1 = new SingleTask("1n", "1d");
        SingleTask singleTask2 = new SingleTask("2n", "2d");
        SingleTask singleTask3 = new SingleTask("3n", "3d");
        SingleTask singleTask4 = new SingleTask("4n", "4d");
        taskManager.addSingleTask(singleTask1);
        taskManager.addSingleTask(singleTask2);
        taskManager.addSingleTask(singleTask3);
        taskManager.addSingleTask(singleTask4);

        SingleTask received1 = taskManager.getSingleTask(1);
        SingleTask received2 = taskManager.getSingleTask(2);
        SingleTask received3 = taskManager.getSingleTask(3);
        SingleTask received4 = taskManager.getSingleTask(4);

        assertTrue(taskManager.getHistory().contains(received1));
        assertTrue(taskManager.getHistory().contains(received2));
        assertTrue(taskManager.getHistory().contains(received3));
        assertTrue(taskManager.getHistory().contains(received4));

        taskManager.removeSingleTask(3);
        assertTrue(taskManager.getHistory().contains(received1));
        assertTrue(taskManager.getHistory().contains(received2));
        assertFalse(taskManager.getHistory().contains(received3));
        assertTrue(taskManager.getHistory().contains(received4));

        List<SingleTask> history = taskManager.getHistory();
        assertEquals(received1, history.get(0));
        assertEquals(received2, history.get(1));
        assertEquals(received4, history.get(2));


        System.out.println(taskManager.getHistory());

        taskManager.removeSingleTask(2);
        assertTrue(taskManager.getHistory().contains(received1));
        assertFalse(taskManager.getHistory().contains(received2));
        assertFalse(taskManager.getHistory().contains(received3));
        assertTrue(taskManager.getHistory().contains(received4));
        history = taskManager.getHistory();
        assertEquals(received1, history.get(0));
        assertEquals(received4, history.get(1));

        taskManager.removeSingleTask(1);
        assertFalse(taskManager.getHistory().contains(received1));
        assertFalse(taskManager.getHistory().contains(received2));
        assertFalse(taskManager.getHistory().contains(received3));
        assertTrue(taskManager.getHistory().contains(received4));
        history = taskManager.getHistory();
        assertEquals(received4, history.getFirst());

        taskManager.removeSingleTask(4);
        assertFalse(taskManager.getHistory().contains(received1));
        assertFalse(taskManager.getHistory().contains(received2));
        assertFalse(taskManager.getHistory().contains(received3));
        assertFalse(taskManager.getHistory().contains(received4));
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void checkOrderOfTasksWithGetEpicAndSubs() {
        EpicTask epicTask1 = new EpicTask("epic", "desc");
        EpicTask epicTask2 = new EpicTask("epic2", "desc2");

        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);

        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();

        epicTask1 = epicTasks.getFirst();
        epicTask2 = epicTasks.getLast();

        SubTask subTask1Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask2Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask3Epic2 = new SubTask("", "", epicTask2.getId());
        SubTask subTask4Epic2 = new SubTask("", "", epicTask2.getId());

        taskManager.addSubTask(subTask1Epic1);
        taskManager.addSubTask(subTask2Epic1);
        taskManager.addSubTask(subTask3Epic2);
        taskManager.addSubTask(subTask4Epic2);

        for (int i = 0; i < 6; i++) {
            EpicTask epicTaskReceived1 = taskManager.getEpicTask(1);
            assertEquals(epicTaskReceived1, taskManager.getHistory().getLast());
            EpicTask epicTaskReceived2 = taskManager.getEpicTask(2);
            assertEquals(epicTaskReceived2, taskManager.getHistory().getLast());
            SubTask subTaskReceived1 = taskManager.getSubTask(3);
            assertEquals(subTaskReceived1, taskManager.getHistory().getLast());
            SubTask subTaskReceived4 = taskManager.getSubTask(6);
            assertEquals(subTaskReceived4, taskManager.getHistory().getLast());
            SubTask subTaskReceived3 = taskManager.getSubTask(5);
            assertEquals(subTaskReceived3, taskManager.getHistory().getLast());
            SubTask subTaskReceived2 = taskManager.getSubTask(4);
            assertEquals(subTaskReceived2, taskManager.getHistory().getLast());
        }
    }

    @Test
    void checkOrderOfTasksWithRemoveEpicAndSubs() {
        EpicTask epicTask1 = new EpicTask("epic", "desc");
        EpicTask epicTask2 = new EpicTask("epic2", "desc2");

        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);

        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();

        epicTask1 = epicTasks.getFirst();
        epicTask2 = epicTasks.getLast();

        SubTask subTask1Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask2Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask3Epic2 = new SubTask("", "", epicTask2.getId());
        SubTask subTask4Epic2 = new SubTask("", "", epicTask2.getId());

        taskManager.addSubTask(subTask1Epic1);
        taskManager.addSubTask(subTask2Epic1);
        taskManager.addSubTask(subTask3Epic2);
        taskManager.addSubTask(subTask4Epic2);

        EpicTask task1 = taskManager.getEpicTask(1);
        EpicTask task2 = taskManager.getEpicTask(2);
        SubTask task3 = taskManager.getSubTask(3);
        SubTask task4 = taskManager.getSubTask(4);
        SubTask task5 = taskManager.getSubTask(5);
        SubTask task6 = taskManager.getSubTask(6);

        assertTrue(taskManager.getHistory().contains(task1));
        assertTrue(taskManager.getHistory().contains(task2));
        assertTrue(taskManager.getHistory().contains(task3));
        assertTrue(taskManager.getHistory().contains(task4));
        assertTrue(taskManager.getHistory().contains(task5));
        assertTrue(taskManager.getHistory().contains(task6));

        taskManager.removeEpicTask(1);
        assertFalse(taskManager.getHistory().contains(task1));
        assertTrue(taskManager.getHistory().contains(task2));
        assertFalse(taskManager.getHistory().contains(task3));
        assertFalse(taskManager.getHistory().contains(task4));
        assertTrue(taskManager.getHistory().contains(task5));
        assertTrue(taskManager.getHistory().contains(task6));

        List<SingleTask> history = taskManager.getHistory();
        assertEquals(task2, history.get(0));
        assertEquals(task5, history.get(1));
        assertEquals(task6, history.get(2));

        taskManager.removeEpicTask(2);
        assertFalse(taskManager.getHistory().contains(task1));
        assertFalse(taskManager.getHistory().contains(task2));
        assertFalse(taskManager.getHistory().contains(task3));
        assertFalse(taskManager.getHistory().contains(task4));
        assertFalse(taskManager.getHistory().contains(task5));
        assertFalse(taskManager.getHistory().contains(task6));
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void checkClearSingleTasks() {
        SingleTask singleTask1 = new SingleTask("1n", "1d");
        SingleTask singleTask2 = new SingleTask("2n", "2d");
        SingleTask singleTask3 = new SingleTask("3n", "3d");
        SingleTask singleTask4 = new SingleTask("4n", "4d");
        taskManager.addSingleTask(singleTask1);
        taskManager.addSingleTask(singleTask2);
        taskManager.addSingleTask(singleTask3);
        taskManager.addSingleTask(singleTask4);

        for (int i = 1; i <= 4; i++) {
            taskManager.getSingleTask(i);
        }
        assertEquals(4, taskManager.getHistory().size());
        taskManager.clearAllSingleTasks();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void checkClearSubTasks() {
        EpicTask epicTask1 = new EpicTask("epic", "desc");
        EpicTask epicTask2 = new EpicTask("epic2", "desc2");

        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);

        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();

        epicTask1 = epicTasks.getFirst();
        epicTask2 = epicTasks.getLast();

        SubTask subTask1Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask2Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask3Epic2 = new SubTask("", "", epicTask2.getId());
        SubTask subTask4Epic2 = new SubTask("", "", epicTask2.getId());

        taskManager.addSubTask(subTask1Epic1);
        taskManager.addSubTask(subTask2Epic1);
        taskManager.addSubTask(subTask3Epic2);
        taskManager.addSubTask(subTask4Epic2);

        EpicTask task1 = taskManager.getEpicTask(1);
        EpicTask task2 = taskManager.getEpicTask(2);
        SubTask task3 = taskManager.getSubTask(3);
        SubTask task4 = taskManager.getSubTask(4);
        SubTask task5 = taskManager.getSubTask(5);
        SubTask task6 = taskManager.getSubTask(6);

        taskManager.clearAllSubTasks();
        assertEquals(2, taskManager.getHistory().size());
        assertTrue(taskManager.getHistory().contains(task1));
        assertTrue(taskManager.getHistory().contains(task2));
        assertFalse(taskManager.getHistory().contains(task3));
        assertFalse(taskManager.getHistory().contains(task4));
        assertFalse(taskManager.getHistory().contains(task5));
        assertFalse(taskManager.getHistory().contains(task6));
    }

    @Test
    void checkClearEpicTasks() {
        EpicTask epicTask1 = new EpicTask("epic", "desc");
        EpicTask epicTask2 = new EpicTask("epic2", "desc2");

        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);

        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();

        epicTask1 = epicTasks.getFirst();
        epicTask2 = epicTasks.getLast();

        SubTask subTask1Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask2Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask3Epic2 = new SubTask("", "", epicTask2.getId());
        SubTask subTask4Epic2 = new SubTask("", "", epicTask2.getId());

        taskManager.addSubTask(subTask1Epic1);
        taskManager.addSubTask(subTask2Epic1);
        taskManager.addSubTask(subTask3Epic2);
        taskManager.addSubTask(subTask4Epic2);

        EpicTask task1 = taskManager.getEpicTask(1);
        EpicTask task2 = taskManager.getEpicTask(2);
        SubTask task3 = taskManager.getSubTask(3);
        SubTask task4 = taskManager.getSubTask(4);
        SubTask task5 = taskManager.getSubTask(5);
        SubTask task6 = taskManager.getSubTask(6);

        taskManager.clearAllEpicTasks();
        assertEquals(0, taskManager.getHistory().size());
        assertFalse(taskManager.getHistory().contains(task1));
        assertFalse(taskManager.getHistory().contains(task2));
        assertFalse(taskManager.getHistory().contains(task3));
        assertFalse(taskManager.getHistory().contains(task4));
        assertFalse(taskManager.getHistory().contains(task5));
        assertFalse(taskManager.getHistory().contains(task6));
    }

    @Test
    void checkClearEpicSubtasks() {
        EpicTask epicTask1 = new EpicTask("epic", "desc");
        EpicTask epicTask2 = new EpicTask("epic2", "desc2");

        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);

        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();

        epicTask1 = epicTasks.getFirst();
        epicTask2 = epicTasks.getLast();

        SubTask subTask1Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask2Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask3Epic2 = new SubTask("", "", epicTask2.getId());
        SubTask subTask4Epic2 = new SubTask("", "", epicTask2.getId());

        taskManager.addSubTask(subTask1Epic1);
        taskManager.addSubTask(subTask2Epic1);
        taskManager.addSubTask(subTask3Epic2);
        taskManager.addSubTask(subTask4Epic2);

        EpicTask task1 = taskManager.getEpicTask(1);
        EpicTask task2 = taskManager.getEpicTask(2);
        SubTask task3 = taskManager.getSubTask(3);
        SubTask task4 = taskManager.getSubTask(4);
        SubTask task5 = taskManager.getSubTask(5);
        SubTask task6 = taskManager.getSubTask(6);

        taskManager.clearEpicSubTasks(1);
        assertEquals(4, taskManager.getHistory().size());
        assertTrue(taskManager.getHistory().contains(task1));
        assertTrue(taskManager.getHistory().contains(task2));
        assertFalse(taskManager.getHistory().contains(task3));
        assertFalse(taskManager.getHistory().contains(task4));
        assertTrue(taskManager.getHistory().contains(task5));
        assertTrue(taskManager.getHistory().contains(task6));
    }

    @Test
    void checkClearEveryTasks() {
        SingleTask singleTask1 = new SingleTask("1n", "1d");
        SingleTask singleTask2 = new SingleTask("2n", "2d");
        SingleTask singleTask3 = new SingleTask("3n", "3d");
        SingleTask singleTask4 = new SingleTask("4n", "4d");
        taskManager.addSingleTask(singleTask1);
        taskManager.addSingleTask(singleTask2);
        taskManager.addSingleTask(singleTask3);
        taskManager.addSingleTask(singleTask4);

        for (int i = 1; i <= 4; i++) {
            taskManager.getSingleTask(i);
        }

        EpicTask epicTask1 = new EpicTask("epic", "desc");
        EpicTask epicTask2 = new EpicTask("epic2", "desc2");

        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);

        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();

        epicTask1 = epicTasks.getFirst();
        epicTask2 = epicTasks.getLast();

        SubTask subTask1Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask2Epic1 = new SubTask("", "", epicTask1.getId());
        SubTask subTask3Epic2 = new SubTask("", "", epicTask2.getId());
        SubTask subTask4Epic2 = new SubTask("", "", epicTask2.getId());

        taskManager.addSubTask(subTask1Epic1);
        taskManager.addSubTask(subTask2Epic1);
        taskManager.addSubTask(subTask3Epic2);
        taskManager.addSubTask(subTask4Epic2);

        taskManager.getEpicTask(5);
        taskManager.getEpicTask(6);
        taskManager.getSubTask(7);
        taskManager.getSubTask(8);
        taskManager.getSubTask(9);
        taskManager.getSubTask(10);

        assertEquals(10, taskManager.getHistory().size());
        taskManager.clearEveryTasks();
        assertEquals(0, taskManager.getHistory().size());
    }
}