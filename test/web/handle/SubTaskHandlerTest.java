package web.handle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import web.HttpTaskServer;
import web.JsonTaskOption;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskHandlerTest extends AbstractTasksHandlersTest<SubTask> {
    @BeforeEach
    void beforeEach() throws IOException {
        manager.addEpicTask(new EpicTask("", ""));
        task = new SubTask("", "", 1);
        startOfPath = "subtasks";
        client = HttpClient.newHttpClient();
        HttpTaskServer.getInstance().start();
        manager.clearEveryTasks();
        manager.addEpicTask(new EpicTask("", ""));
    }

    @AfterEach
    void afterEach() {
        client.close();
        HttpTaskServer.getInstance().stop();
    }

    @Test
    void shouldBeNotSuccessAddedSingleTaskAsSubTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");

        HttpResponse<String> response = addOrUpdateTaskResponse(client, singleTask);
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        List<SubTask> tasksFromManager = manager.getAllSubTasks();
        List<SingleTask> tasksFromManager2 = manager.getAllSingleTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(0, tasksFromManager2.size(), "Некорректное количество задач");
    }

    @Override
    public List<SubTask> getListOfTasksFromManager() {
        return manager.getAllSubTasks();
    }

    @Override
    public Optional<SubTask> getTaskFromJson(String json) {
        return JsonTaskOption.getSubTaskFromJson(json);
    }
}
