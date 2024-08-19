package manager.history;

import manager.Managers;
import manager.task.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.single.SingleTask;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
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
    void shouldBe10TasksInHistoryAfterAdd10() {
        for (int i = 0; i < 10; i++) {
            historyManager.add(new SingleTask(i + "n", i + "d"));
        }
        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    void shouldBe10TasksInHistoryAfterAdd11() {
        for (int i = 0; i < 11; i++) {
            historyManager.add(new SingleTask(i + "n", i + "d"));
        }
        assertEquals(10, historyManager.getHistory().size());
    }
}