package task;

import manager.Managers;
import manager.task.TaskManager;
import org.junit.jupiter.api.Test;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import static org.junit.jupiter.api.Assertions.*;

public class SingleAndSubAndEpicTest {

    @Test
    void checkEqualsTasks() {
        SingleTask singleTask1 = new SingleTask("n1", "d1");
        SingleTask singleTask2 = new SingleTask("n2", "d2");
        singleTask1.setId(1);
        singleTask2.setId(1);
        assertEquals(singleTask1, singleTask2);

        EpicTask epicTask1 = new EpicTask("n1", "d1");
        EpicTask epicTask2 = new EpicTask("n2", "d2");
        epicTask1.setId(1);
        epicTask2.setId(1);
        assertEquals(epicTask1, epicTask2);

        SubTask subTask1 = new SubTask("n1", "d1", 11);
        SubTask subTask2 = new SubTask("n2", "d2", 22);
        subTask1.setId(1);
        subTask2.setId(1);
        assertEquals(subTask1, subTask2);

        assertNotEquals(singleTask1, subTask1);
        assertNotEquals(singleTask1, epicTask1);
    }

    @Test
    void checkImmutabilityOfSingleTask() {
        SingleTask singleTask = new SingleTask("name", "desc");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addSingleTask(singleTask);
        SingleTask singleTaskReceived = taskManager.getAllSingleTasks().getFirst();

        assertEquals(singleTask.getName(), singleTaskReceived.getName());
        assertEquals(singleTask.getDescription(), singleTaskReceived.getDescription());
        assertEquals(singleTask.getStatus(), singleTaskReceived.getStatus());
    }
}
