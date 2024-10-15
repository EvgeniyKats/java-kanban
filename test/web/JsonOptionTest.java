package web;

import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;
import org.junit.jupiter.api.Test;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JsonOptionTest {

    @Test
    void shouldBeEqualsSingleTaskSerializationDeserializationNoDateNoDuration() {
        SingleTask singleTask = new SingleTask("", "");
        singleTask.setId(1);
        String json = JsonOption.taskToJson(singleTask);
        Optional<SingleTask> optional = JsonOption.getSingleTaskFromJson(json);
        assertTrue(optional.isPresent());
        SingleTask singleTask1 = optional.get();
        String json1 = JsonOption.taskToJson(singleTask);
        assertEqualsJson(json, json1);
        assertEqualsTasks(singleTask, singleTask1);
    }

    @Test
    void shouldBeEqualsSingleTaskSerializationDeserialization() {
        SingleTask singleTask = new SingleTask("", "", LocalDateTime.now(), Duration.ofMinutes(1));
        singleTask.setId(1);
        String json = JsonOption.taskToJson(singleTask);
        Optional<SingleTask> optional = JsonOption.getSingleTaskFromJson(json);
        assertTrue(optional.isPresent());
        SingleTask singleTask1 = optional.get();
        String json1 = JsonOption.taskToJson(singleTask);
        assertEqualsJson(json, json1);
        assertEqualsTasks(singleTask, singleTask1);
    }

    @Test
    void shouldBeEqualsSubTaskSerializationDeserializationNoDateNoDuration() {
        SubTask subTask = new SubTask("", "", 1);
        subTask.setId(2);
        String json = JsonOption.taskToJson(subTask);
        Optional<SubTask> optional = JsonOption.getSubTaskFromJson(json);
        assertTrue(optional.isPresent());
        SubTask subTask1 = optional.get();
        String json1 = JsonOption.taskToJson(subTask);
        assertEqualsJson(json, json1);
        assertEqualsTasks(subTask, subTask1);
        assertEquals(subTask.getEpicId(), subTask1.getEpicId());
    }

    @Test
    void shouldBeEqualsSubTaskSerializationDeserialization() {
        SubTask subTask = new SubTask("", "", LocalDateTime.now(), Duration.ofMinutes(1), 1);
        subTask.setId(2);
        String json = JsonOption.taskToJson(subTask);
        Optional<SubTask> optional = JsonOption.getSubTaskFromJson(json);
        assertTrue(optional.isPresent());
        SubTask subTask1 = optional.get();
        String json1 = JsonOption.taskToJson(subTask);
        assertEqualsJson(json, json1);
        assertEqualsTasks(subTask, subTask1);
        assertEquals(subTask.getEpicId(), subTask1.getEpicId());
    }

    @Test
    void shouldBeEqualsEpicTaskSerializationDeserializationNoDateNoDuration() {
        EpicTask epicTask = new EpicTask("", "");
        epicTask.setId(1);
        epicTask.addSubTaskId(2);
        epicTask.addSubTaskId(3);
        epicTask.addSubTaskId(4);
        epicTask.setId(2);
        String json = JsonOption.taskToJson(epicTask);
        Optional<EpicTask> optional = JsonOption.getEpicTaskFromJson(json);
        assertTrue(optional.isPresent());
        EpicTask epicTask1 = optional.get();
        String json1 = JsonOption.taskToJson(epicTask);
        assertEqualsJson(json, json1);
        assertEqualsTasks(epicTask, epicTask1);
        assertEquals(epicTask.getSubTasksId(), epicTask1.getSubTasksId());
    }

    @Test
    void shouldBeEqualsEpicTaskSerializationDeserialization() {
        EpicTask epicTask = new EpicTask("", "");
        TaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(new SubTask("",
                "",
                LocalDateTime.of(2024, 10, 15, 20, 30),
                Duration.ofMinutes(1),
                id));
        taskManager.addSubTask(new SubTask("",
                "",
                LocalDateTime.of(2024, 10, 15, 20, 35),
                Duration.ofMinutes(1),
                id));
        taskManager.addSubTask(new SubTask("",
                "",
                LocalDateTime.of(2024, 10, 15, 20, 40),
                Duration.ofMinutes(1),
                id));
        epicTask = taskManager.getEpicTask(id);
        String json = JsonOption.taskToJson(epicTask);
        Optional<EpicTask> optional = JsonOption.getEpicTaskFromJson(json);
        assertTrue(optional.isPresent());
        EpicTask epicTask1 = optional.get();
        String json1 = JsonOption.taskToJson(epicTask);
        assertEqualsJson(json, json1);
        assertEqualsTasks(epicTask, epicTask1);
        assertEquals(epicTask.getSubTasksId(), epicTask1.getSubTasksId());
    }

    @Test
    void shouldBeEmptyOptionSingleTask() {
        EpicTask epicTask = new EpicTask("", "");
        String jsonEpic = JsonOption.taskToJson(epicTask);
        Optional<SingleTask> optional1 = JsonOption.getSingleTaskFromJson(jsonEpic);
        Optional<SingleTask> optional2 = JsonOption.getSingleTaskFromJson("");
        Optional<SingleTask> optional3 = JsonOption.getSingleTaskFromJson(null);
        assertTrue(optional1.isEmpty());
        assertTrue(optional2.isEmpty());
        assertTrue(optional3.isEmpty());
    }

    @Test
    void shouldBeEmptyOptionSubTask() {
        SingleTask singleTask = new SingleTask("", "");
        String jsonSingleTask = JsonOption.taskToJson(singleTask);
        Optional<SubTask> optional1 = JsonOption.getSubTaskFromJson(jsonSingleTask);
        Optional<SubTask> optional2 = JsonOption.getSubTaskFromJson("");
        Optional<SubTask> optional3 = JsonOption.getSubTaskFromJson(null);
        assertTrue(optional1.isEmpty());
        assertTrue(optional2.isEmpty());
        assertTrue(optional3.isEmpty());
    }

    @Test
    void shouldBeEmptyOptionEpicTask() {
        SingleTask singleTask = new SingleTask("", "");
        String jsonSingleTask = JsonOption.taskToJson(singleTask);
        Optional<EpicTask> optional1 = JsonOption.getEpicTaskFromJson(jsonSingleTask);
        Optional<EpicTask> optional2 = JsonOption.getEpicTaskFromJson("");
        Optional<EpicTask> optional3 = JsonOption.getEpicTaskFromJson(null);
        assertTrue(optional1.isEmpty());
        assertTrue(optional2.isEmpty());
        assertTrue(optional3.isEmpty());
    }

    private void assertEqualsTasks(Task task1, Task task2) {
        assertEquals(task1, task2);
        assertEquals(task1.getId(), task2.getId());
        assertEquals(task1.getTaskType(), task2.getTaskType());
        assertEquals(task1.getDuration(), task2.getDuration());
        assertEquals(task1.getStartTime(), task2.getStartTime());
        assertEquals(task1.getEndTime(), task2.getEndTime());
        assertEquals(task1.getName(), task2.getName());
        assertEquals(task1.getDescription(), task2.getDescription());
    }

    private void assertEqualsJson(String json1, String json2) {
        assertEquals(json1, json2);
    }
}
