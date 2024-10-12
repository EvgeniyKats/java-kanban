package manager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.single.SingleTask;
import task.single.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskPriorityManagerTest {
    private TaskPriorityManager taskPriorityManager;

    @BeforeEach
    void beforeEach() {
        taskPriorityManager = TaskPriorityManager.createTaskPriorityManager();
    }

    @Test
    void createTaskPriorityManagerShouldBeNotNull() {
        assertNotNull(TaskPriorityManager.createTaskPriorityManager());
    }

    @Test
    void shouldBeSuccessAdded1TaskWithSetTime() {
        Task task = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(10));
        task.setId(1);
        taskPriorityManager.add(task);
        assertEquals(1, taskPriorityManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldBeNotSuccessAdded1TaskWithoutSetTime() {
        Task task = new SingleTask("", "");
        taskPriorityManager.add(task);
        assertEquals(0, taskPriorityManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldBeNotSuccessAdded1TaskWithSetTimeLessThanMinimum() {
        Task task = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0),
                Duration.ofMinutes(TaskPriorityManager.MINIMUM_DURATION_OF_TASK_IN_MINUTES).minusMillis(1));
        taskPriorityManager.add(task);
        assertEquals(0, taskPriorityManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldBeNotSuccessAdded3TaskInInterval() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 12, 10, 0);

        Task task = new SingleTask("", "", localDateTime, Duration.ofMinutes(3));
        task.setId(1);
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SingleTask singleTask = new SingleTask("", "", localDateTime.plusMinutes(i), Duration.ofMinutes(1));
            tasks.add(singleTask);
            singleTask.setId(2 + i);
        }
        taskPriorityManager.add(task);

        for (Task t : tasks) {
            assertFalse(taskPriorityManager.add(t));
        }

        assertEquals(1, taskPriorityManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldBeSuccessAdded2TaskOutInterval() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 12, 10, 0);

        Task task = new SingleTask("", "", localDateTime, Duration.ofMinutes(1));
        task.setId(1);
        List<Task> tasks = new ArrayList<>();
        tasks.add(new SingleTask("", "", localDateTime.plusMinutes(1), Duration.ofMinutes(1)));
        tasks.add(new SingleTask("", "", localDateTime.minusMinutes(1), Duration.ofMinutes(1)));
        tasks.get(0).setId(2);
        tasks.get(1).setId(3);
        taskPriorityManager.add(task);

        for (Task t : tasks) {
            assertTrue(taskPriorityManager.add(t));
        }

        assertEquals(3, taskPriorityManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldBeSuccessRemovedTask() {
        Task task = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(10));
        task.setId(1);
        taskPriorityManager.add(task);
        assertEquals(1, taskPriorityManager.getPrioritizedTasks().size());
        assertFalse(taskPriorityManager.isBadTaskTime(task, new ArrayList<>()));
        assertTrue(taskPriorityManager.remove(task));
        assertEquals(0, taskPriorityManager.getPrioritizedTasks().size());
        assertFalse(taskPriorityManager.isBadTaskTime(task, new ArrayList<>()));
    }

    @Test
    void shouldBeNotSuccessRemovedTaskIfHaveNot() {
        Task task = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(10));
        task.setId(1);
        assertFalse(taskPriorityManager.remove(task));
    }

    @Test
    void shouldBeEmptyTasksAfterDeleteAll() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 10, 12, 10, 0);

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Task task = new SingleTask("", "", localDateTime.plusMinutes(i), Duration.ofMinutes(1));
            task.setId(i + 1);
            tasks.add(task);
        }

        for (Task t : tasks) {
            assertTrue(taskPriorityManager.add(t));
            assertFalse(taskPriorityManager.isBadTaskTime(t, new ArrayList<>()));
        }
        assertEquals(3, taskPriorityManager.getPrioritizedTasks().size());
        taskPriorityManager.clearAll();
        assertEquals(0, taskPriorityManager.getPrioritizedTasks().size());
        for (Task t : tasks) {
            assertFalse(taskPriorityManager.isBadTaskTime(t, new ArrayList<>()));
        }
    }

    @Test
    void checkCorrectCompareOfTasks() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 10, 12, 10, 1);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 10, 12, 10, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 10, 11, 10, 0);
        LocalDateTime localDateTime4 = LocalDateTime.of(2024, 11, 12, 10, 0);
        LocalDateTime localDateTime5 = LocalDateTime.of(2023, 10, 12, 10, 0);
        LocalDateTime localDateTime6 = LocalDateTime.of(2023, 10, 12, 10, 10);

        Task task1 = new SingleTask("", "", localDateTime1, Duration.ofMinutes(1));
        Task task2 = new SingleTask("", "", localDateTime2, Duration.ofMinutes(1));
        Task task3 = new SingleTask("", "", localDateTime3, Duration.ofMinutes(1));
        Task task4 = new SingleTask("", "", localDateTime4, Duration.ofMinutes(1));
        Task task5 = new SingleTask("", "", localDateTime5, Duration.ofMinutes(1));
        Task task6 = new SingleTask("", "", localDateTime6, Duration.ofMinutes(1));

        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        task4.setId(4);
        task5.setId(5);
        task6.setId(6);

        taskPriorityManager.add(task1);
        taskPriorityManager.add(task2);
        taskPriorityManager.add(task3);
        taskPriorityManager.add(task4);
        taskPriorityManager.add(task5);
        taskPriorityManager.add(task6);

        List<Task> tasks = taskPriorityManager.getPrioritizedTasks();

        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j) {
                    Task taskTemp1 = tasks.get(i);
                    Task taskTemp2 = tasks.get(j);
                    if (i > j) {
                        assertTrue(taskTemp1.getStartTime().isAfter(taskTemp2.getStartTime()));
                    } else {
                        assertTrue(taskTemp1.getStartTime().isBefore(taskTemp2.getStartTime()));
                    }
                }
            }
        }
    }

    @Test
    void shouldBeUpdatedTaskVersion() {
        Task task = new SingleTask("before", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(10));
        task.setId(1);
        assertFalse(taskPriorityManager.updateTask(task));
        taskPriorityManager.add(task);
        Task taskCopy = task.getCopy();
        taskCopy.setName("after");
        assertTrue(taskPriorityManager.updateTask(taskCopy));
        List<Task> tasks = taskPriorityManager.getPrioritizedTasks();
        assertEquals("after", tasks.getFirst().getName());
    }

    @Test
    void shouldBeNotUpdateIfHaveNotTask() {
        Task task = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(10));
        task.setId(1);
        taskPriorityManager.add(task);
        Task task2 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(10));
        task2.setId(2);
        assertFalse(taskPriorityManager.updateTask(task2));
    }

    @Test
    void shouldBeNotUpdateIfBadTimeTask() {
        Task task1 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                0), Duration.ofMinutes(1));
        task1.setId(1);
        taskPriorityManager.add(task1);
        Task task2 = new SingleTask("", "", LocalDateTime.of(2024,
                10,
                12,
                10,
                1), Duration.ofMinutes(1));
        task2.setId(2);
        taskPriorityManager.add(task2);

        Task task3 = task2.getCopy();
        task3.setStartTime(task1.getStartTime());
        assertFalse(taskPriorityManager.updateTask(task3));
    }
}