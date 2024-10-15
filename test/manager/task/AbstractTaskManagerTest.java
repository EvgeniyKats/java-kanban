package manager.task;

import org.junit.jupiter.api.Test;
import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractTaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void shouldBeAddedSingleTask() {
        SingleTask singleTask = new SingleTask("singleName", "singleDescription");
        assertNotEquals(-1, taskManager.addSingleTask(singleTask));
    }

    @Test
    void shouldBeAddedSingleTaskWithIdWithoutConflict() {
        SingleTask singleTask = new SingleTask("singleName", "singleDescription");
        singleTask.setId(1);
        taskManager.addSingleTask(singleTask);

        SingleTask singleTaskReceived = taskManager.getAllSingleTasks().getFirst();
        assertEquals(-1, taskManager.addSingleTask(singleTaskReceived));
        singleTaskReceived.setId(2);
        assertNotEquals(-1, taskManager.addSingleTask(singleTaskReceived));
    }

    @Test
    void shouldBeNotAddedEpicTaskToSingleTask() {
        EpicTask epicTask = new EpicTask("epic", "epic");
        assertEquals(-1, taskManager.addSingleTask(epicTask));
    }

    @Test
    void shouldBeNotDoubleAddedEpic() {
        EpicTask epicTask = new EpicTask("epic", "epic");
        assertNotEquals(-1, taskManager.addEpicTask(epicTask));
        EpicTask epicTaskReceived = taskManager.getAllEpicTasks().getFirst();
        assertEquals(-1, taskManager.addEpicTask(epicTaskReceived));
    }

    @Test
    void shouldBeNotAddedSubTaskWithoutEpic() {
        SubTask subTask = new SubTask("subtaskName", "subtaskDescription", null);
        assertEquals(-1, taskManager.addSubTask(subTask));
        subTask = new SubTask("subtaskName", "subtaskDescription", 1);
        assertEquals(-1, taskManager.addSubTask(subTask));
    }

    @Test
    void shouldBeAddedSubTaskWithEpic() {
        EpicTask epicTask = new EpicTask("epicName", "epicDescription");
        assertNotEquals(-1, taskManager.addEpicTask(epicTask));
        SubTask subTask = new SubTask("subtaskName", "subtaskDescription", 1);
        assertNotEquals(-1, taskManager.addSubTask(subTask));
    }

    @Test
    void shouldBeAddedEpicTask() {
        EpicTask epicTask = new EpicTask("epicName", "epicDescription");
        assertNotEquals(-1, taskManager.addEpicTask(epicTask));
    }

    @Test
    void checkNullIfNoTasksWithThisId() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        assertNull(taskManager.getSingleTask(-1));
        assertNull(taskManager.getSubTask(-1));
        assertNull(taskManager.getEpicTask(-1));
    }

    @Test
    void shouldBeNullGetSubTasksFromNotExistingEpic() {
        assertNull(taskManager.getSubTasksFromEpic(-1));
    }

    @Test
    void getSingleTaskShouldBeNotNull() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        List<SingleTask> singleTasks = taskManager.getAllSingleTasks();
        assertNotNull(taskManager.getSingleTask(singleTasks.getFirst().getId()));
    }

    @Test
    void getSubTaskShouldBeNotNull() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(taskManager.getSubTask(subTasks.getFirst().getId()));
    }

    @Test
    void getEpicTaskShouldBeNotNull() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();
        assertNotNull(taskManager.getEpicTask(epicTasks.getFirst().getId()));
    }

    @Test
    void getAllSingleTasksShouldBeNotNull() {
        taskManager.addSingleTask(new SingleTask("", ""));
        assertNotNull(taskManager.getAllSingleTasks());
    }

    @Test
    void getAllSubTasksShouldBeNotNull() {
        taskManager.addEpicTask(new EpicTask("", ""));
        taskManager.addSubTask(new SubTask("", "", 1));
        assertNotNull(taskManager.getAllSubTasks());
    }

    @Test
    void getSubTasksFromEpicShouldBeNotNull() {
        taskManager.addEpicTask(new EpicTask("", ""));
        taskManager.addSubTask(new SubTask("", "", 1));
        EpicTask epicTaskReceived = taskManager.getEpicTask(1);
        assertNotNull(taskManager.getSubTasksFromEpic(epicTaskReceived.getId()));
    }

    @Test
    void getAllEpicTasksShouldBeNotNull() {
        taskManager.addEpicTask(new EpicTask("", ""));
        assertNotNull(taskManager.getAllEpicTasks());
    }

    @Test
    void updateWrongTasks() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        SingleTask singleTaskReceived = taskManager.getAllSingleTasks().getFirst();
        SubTask subTask = taskManager.getAllSubTasks().getFirst();
        EpicTask epicTask = taskManager.getAllEpicTasks().getFirst();

        singleTaskReceived.setId(-1);
        subTask.setId(-2);
        epicTask.setId(-3);

        assertFalse(taskManager.updateSingleTask(singleTaskReceived));
        assertFalse(taskManager.updateSubTask(subTask));
        assertFalse(taskManager.updateEpicTask(epicTask));
    }

    @Test
    void updateSingleTask() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        SingleTask singleTaskReceived = taskManager.getAllSingleTasks().getFirst();
        singleTaskReceived.setName("newName");
        assertTrue(taskManager.updateSingleTask(singleTaskReceived));
        singleTaskReceived = taskManager.getAllSingleTasks().getFirst();
        assertEquals(singleTaskReceived.getName(), "newName");
    }

    @Test
    void updateSubTask() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        SubTask subTaskReceived = taskManager.getAllSubTasks().getFirst();
        subTaskReceived.setName("newName");
        assertTrue(taskManager.updateSubTask(subTaskReceived));
        subTaskReceived = taskManager.getAllSubTasks().getFirst();
        assertEquals(subTaskReceived.getName(), "newName");
    }

    @Test
    void updateEpicTask() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        EpicTask epicTaskReceived = taskManager.getAllEpicTasks().getFirst();
        epicTaskReceived.setName("newName");
        assertTrue(taskManager.updateEpicTask(epicTaskReceived));
        epicTaskReceived = taskManager.getAllEpicTasks().getFirst();
        assertEquals(epicTaskReceived.getName(), "newName");
    }

    @Test
    void removeNotExistingTasks() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        assertFalse(taskManager.removeSingleTask(-1));
        assertFalse(taskManager.removeSubTask(-1));
        assertFalse(taskManager.removeEpicTask(-1));
    }

    @Test
    void removeSingleTask() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        SingleTask singleTask = taskManager.getAllSingleTasks().getFirst();
        assertTrue(taskManager.removeSingleTask(singleTask.getId()));
        assertFalse(taskManager.getAllSingleTasks().contains(singleTask));
    }

    @Test
    void removeSubTask() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        SubTask subTask = taskManager.getAllSubTasks().getFirst();
        assertTrue(taskManager.removeSubTask(subTask.getId()));
        assertFalse(taskManager.getAllSingleTasks().contains(subTask));
    }

    @Test
    void removeEpicTask() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        EpicTask epicTask = taskManager.getAllEpicTasks().getFirst();
        assertTrue(taskManager.removeEpicTask(epicTask.getId()));
        assertFalse(taskManager.getAllSingleTasks().contains(epicTask));
    }

    @Test
    void clearAllSingleTasks() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        taskManager.clearAllSingleTasks();
        assertEquals(0, taskManager.getAllSingleTasks().size());
        assertNotEquals(0, taskManager.getAllSubTasks().size());
        assertNotEquals(0, taskManager.getAllEpicTasks().size());
    }

    @Test
    void clearAllSubTasks() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        taskManager.clearAllSubTasks();
        assertNotEquals(0, taskManager.getAllSingleTasks().size());
        assertEquals(0, taskManager.getAllSubTasks().size());
        assertNotEquals(0, taskManager.getAllEpicTasks().size());
    }

    @Test
    void clearAllEpicTasks() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        taskManager.clearAllEpicTasks();
        assertNotEquals(0, taskManager.getAllSingleTasks().size());
        assertEquals(0, taskManager.getAllSubTasks().size());
        assertEquals(0, taskManager.getAllEpicTasks().size());
    }

    @Test
    void clearEpicSubTasks() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        assertFalse(taskManager.clearEpicSubTasks(-1));
        EpicTask epicTaskReceived = taskManager.getAllEpicTasks().getFirst();
        assertNotEquals(0, epicTaskReceived.getSubTasksId().size());
        taskManager.clearEpicSubTasks(epicTaskReceived.getId());
        epicTaskReceived = taskManager.getAllEpicTasks().getFirst();
        assertEquals(0, epicTaskReceived.getSubTasksId().size());
    }

    @Test
    void clearEveryTasks() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        taskManager.clearEveryTasks();
        assertEquals(0, taskManager.getAllSingleTasks().size());
        assertEquals(0, taskManager.getAllSubTasks().size());
        assertEquals(0, taskManager.getAllEpicTasks().size());
    }

    @Test
    void updateStatusEpic() {
        putInManager_2SingleTasks_2EpicTasksWith_2Subtasks();
        EpicTask epicTask = taskManager.getAllEpicTasks().getFirst();

        assertEquals(Status.NEW, epicTask.getStatus());

        SubTask subTask1 = taskManager.getSubTask(epicTask.getSubTasksId().get(0));
        SubTask subTask2 = taskManager.getSubTask(epicTask.getSubTasksId().get(1));

        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        assertEquals(Status.IN_PROGRESS, taskManager.getAllEpicTasks().getFirst().getStatus());

        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask2);
        assertEquals(Status.IN_PROGRESS, taskManager.getAllEpicTasks().getFirst().getStatus());

        subTask2.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask2);
        assertEquals(Status.DONE, taskManager.getAllEpicTasks().getFirst().getStatus());
    }

    @Test
    void sizeHistoryShouldBe0() {
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void sizeHistoryShouldBe10AfterGet10Tasks() {
        for (int i = 1; i <= 10; i++) {
            taskManager.addSingleTask(new SingleTask("name", "desc"));
            taskManager.getSingleTask(i);
        }

        List<Task> singleTasks = taskManager.getHistory();
        for (int i = 0; i < 10; i++) {
            assertEquals(i + 1, singleTasks.get(i).getId());
        }

        assertEquals(10, singleTasks.size());
    }

    @Test
    void sizeHistoryShouldBe11AfterGet11Tasks() {

        for (int i = 1; i <= 11; i++) {
            taskManager.addSingleTask(new SingleTask("name", "desc"));
            taskManager.getSingleTask(i);
        }

        List<Task> singleTasks = taskManager.getHistory();
        for (int i = 0; i < 11; i++) {
            assertEquals(i + 1, singleTasks.get(i).getId());
        }

        assertEquals(11, singleTasks.size());
    }

    @Test
    void shouldBeOldVersionOfTaskInHistoryAfterUpdate() {
        SingleTask singleTask = new SingleTask("name", "desc");
        taskManager.addSingleTask(singleTask);
        int id = taskManager.getAllSingleTasks().getFirst().getId();
        SingleTask singleTaskReceived = taskManager.getSingleTask(id);
        singleTaskReceived.setName("newName");
        taskManager.updateSingleTask(singleTaskReceived);
        assertEquals("name", taskManager.getHistory().getFirst().getName());
        assertEquals("newName", taskManager.getSingleTask(id).getName()); // обновит задачу в истории
    }

    @Test
    void shouldBeNotAddedSingeTaskIfSetBadTime() {
        SingleTask task = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1));
        SingleTask copy = task.getCopy();
        assertEquals(1, taskManager.addSingleTask(task));
        assertEquals(-2, taskManager.addSingleTask(copy));
    }

    @Test
    void shouldBeNotAddedSubTaskIfSetBadTime() {
        EpicTask epicTask = new EpicTask("", "");
        taskManager.addEpicTask(epicTask);
        SubTask task = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1), 1);
        SubTask copy = task.getCopy();
        assertEquals(2, taskManager.addSubTask(task));
        assertEquals(-2, taskManager.addSubTask(copy));
    }

    @Test
    void shouldBeUpdatedEpicTaskEndTime() {
        EpicTask epicTask = new EpicTask("", "");
        taskManager.addEpicTask(epicTask);
        EpicTask epicTaskReceived = taskManager.getEpicTask(1);
        assertNull(epicTaskReceived.getEndTime());

        SubTask subTask1 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1), 1);

        SubTask subTask2 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                1), Duration.ofMinutes(1), 1);

        SubTask subTask3 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                2), Duration.ofMinutes(1), 1);

        SubTask subTask4 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                3), Duration.ofMinutes(1), 1);

        int idSub1 = taskManager.addSubTask(subTask1);
        assertEquals(subTask1.getEndTime(), taskManager.getEpicTask(1).getEndTime());
        int idSub2 = taskManager.addSubTask(subTask2);
        assertEquals(subTask2.getEndTime(), taskManager.getEpicTask(1).getEndTime());
        int idSub3 = taskManager.addSubTask(subTask3);
        assertEquals(subTask3.getEndTime(), taskManager.getEpicTask(1).getEndTime());
        int idSub4 = taskManager.addSubTask(subTask4);
        assertEquals(subTask4.getEndTime(), taskManager.getEpicTask(1).getEndTime());

        taskManager.removeSubTask(idSub4);
        assertEquals(subTask3.getEndTime(), taskManager.getEpicTask(1).getEndTime());
        taskManager.removeSubTask(idSub3);
        assertEquals(subTask2.getEndTime(), taskManager.getEpicTask(1).getEndTime());
        taskManager.removeSubTask(idSub2);
        assertEquals(subTask1.getEndTime(), taskManager.getEpicTask(1).getEndTime());

        taskManager.removeSubTask(idSub1);
        assertNull(taskManager.getEpicTask(1).getEndTime());
    }

    @Test
    void shouldBeUpdatedEpicTaskStartTime() {
        EpicTask epicTask = new EpicTask("", "");
        taskManager.addEpicTask(epicTask);
        EpicTask epicTaskReceived = taskManager.getEpicTask(1);
        assertNull(epicTaskReceived.getEndTime());

        SubTask subTask1 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1), 1);

        SubTask subTask2 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                1), Duration.ofMinutes(1), 1);

        SubTask subTask3 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                2), Duration.ofMinutes(1), 1);

        SubTask subTask4 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                3), Duration.ofMinutes(1), 1);

        int idSub4 = taskManager.addSubTask(subTask4);
        assertEquals(subTask4.getStartTime(), taskManager.getEpicTask(1).getStartTime());
        int idSub3 = taskManager.addSubTask(subTask3);
        assertEquals(subTask3.getStartTime(), taskManager.getEpicTask(1).getStartTime());
        int idSub2 = taskManager.addSubTask(subTask2);
        assertEquals(subTask2.getStartTime(), taskManager.getEpicTask(1).getStartTime());
        int idSub1 = taskManager.addSubTask(subTask1);
        assertEquals(subTask1.getStartTime(), taskManager.getEpicTask(1).getStartTime());

        taskManager.removeSubTask(idSub1);
        assertEquals(subTask2.getStartTime(), taskManager.getEpicTask(1).getStartTime());

        taskManager.removeSubTask(idSub2);
        assertEquals(subTask3.getStartTime(), taskManager.getEpicTask(1).getStartTime());

        taskManager.removeSubTask(idSub3);
        assertEquals(subTask4.getStartTime(), taskManager.getEpicTask(1).getStartTime());

        taskManager.removeSubTask(idSub4);
        assertNull(taskManager.getEpicTask(1).getStartTime());
    }

    @Test
    void shouldBeUpdatedEpicTaskEndTimeWithClear() {
        EpicTask epicTask = new EpicTask("", "");
        int idEpic = taskManager.addEpicTask(epicTask);
        assertNull(taskManager.getEpicTask(idEpic).getEndTime());

        SubTask subTask1 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1), 1);


        int idSub1 = taskManager.addSubTask(subTask1);
        assertEquals(subTask1.getEndTime(), taskManager.getEpicTask(idEpic).getEndTime());

        taskManager.removeSubTask(idSub1);
        assertNull(taskManager.getEpicTask(idEpic).getEndTime());

        taskManager.addSubTask(subTask1);
        assertEquals(subTask1.getEndTime(), taskManager.getEpicTask(idEpic).getEndTime());

        taskManager.clearAllSubTasks();
        assertNull(taskManager.getEpicTask(idEpic).getEndTime());
    }

    @Test
    void shouldBeNotSuccessAddIfBadTime() {
        SingleTask singleTask = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1));
        SingleTask singleTask2 = singleTask.getCopy();
        taskManager.addSingleTask(singleTask);
        assertEquals(-2, taskManager.addSingleTask(singleTask2));

        EpicTask epicTask = new EpicTask("", "");
        int idEpic = taskManager.addEpicTask(epicTask);

        SubTask subTask = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1), idEpic);
        assertEquals(-2, taskManager.addSubTask(subTask));
    }

    @Test
    void shouldBeNotUpdateSingleIfBadTimeTask() {
        SingleTask task1 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1));
        taskManager.addSingleTask(task1);
        SingleTask task2 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                1), Duration.ofMinutes(1));
        taskManager.addSingleTask(task2);

        SingleTask task3 = task2.getCopy();
        task3.setStartTime(task1.getStartTime());
        assertFalse(taskManager.updateSingleTask(task3));
    }

    @Test
    void shouldBeNotUpdateSubIfBadTimeTask() {
        EpicTask epicTask = new EpicTask("", "");
        int idEpic = taskManager.addEpicTask(epicTask);

        SubTask task1 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1), idEpic);
        taskManager.addSubTask(task1);
        SubTask task2 = new SubTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                1), Duration.ofMinutes(1), idEpic);
        taskManager.addSubTask(task2);

        SubTask task3 = task2.getCopy();
        task3.setStartTime(task1.getStartTime());
        assertFalse(taskManager.updateSubTask(task3));
    }

    @Test
    void shouldBeSuccessAdded1TaskWithSetTime() {
        SingleTask task1 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                5), Duration.ofMinutes(1));
        SingleTask task2 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1));

        SingleTask task3 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                10), Duration.ofMinutes(1));

        int id1 = taskManager.addSingleTask(task1);
        int id2 = taskManager.addSingleTask(task2);
        int id3 = taskManager.addSingleTask(task3);

        List<Task> tasks = taskManager.getPrioritizedTasks();
        assertEquals(3, tasks.size());
        assertEquals(taskManager.getSingleTask(id1), tasks.get(1));
        assertEquals(taskManager.getSingleTask(id2), tasks.get(0));
        assertEquals(taskManager.getSingleTask(id3), tasks.get(2));
    }

    private void putInManager_2SingleTasks_2EpicTasksWith_2Subtasks() {
        SingleTask singleTask1 = new SingleTask("nameSingle1", "descriptionSingle1");
        SingleTask singleTask2 = new SingleTask("nameSingle2", "descriptionSingle2");
        EpicTask epicTask1 = new EpicTask("nameEpic1", "descriptionEpic1");
        EpicTask epicTask2 = new EpicTask("nameEpic2", "descriptionEpic2");
        taskManager.addSingleTask(singleTask1);
        taskManager.addSingleTask(singleTask2);
        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);

        List<EpicTask> epicTasks = taskManager.getAllEpicTasks();

        SubTask sub1Epic1 = new SubTask("nameSub1", "Epic1", epicTasks.get(0).getId());
        SubTask sub2Epic1 = new SubTask("nameSub2", "Epic1", epicTasks.get(0).getId());
        SubTask sub1Epic2 = new SubTask("nameSub1", "Epic2", epicTasks.get(1).getId());
        SubTask sub2Epic2 = new SubTask("nameSub2", "Epic2", epicTasks.get(1).getId());

        taskManager.addSubTask(sub1Epic1);
        taskManager.addSubTask(sub2Epic1);
        taskManager.addSubTask(sub1Epic2);
        taskManager.addSubTask(sub2Epic2);
    }
}
