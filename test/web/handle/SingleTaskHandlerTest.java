package web.handle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.epic.EpicTask;
import task.single.SingleTask;
import web.HttpTaskServer;
import web.JsonTaskOption;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SingleTaskHandlerTest extends AbstractTasksHandlersTest<SingleTask> {
    @BeforeEach
    void beforeEach() throws IOException {
        task = new SingleTask("", "");
        startOfPath = "tasks";
        client = HttpClient.newHttpClient();
        HttpTaskServer.getInstance().start();
        manager.clearEveryTasks();
    }

    @AfterEach
    void afterEach() {
        client.close();
        HttpTaskServer.getInstance().stop();
    }

    @Test
    void shouldBeNotSuccessAddedSingleTaskAsEpic() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(epicTask);

        URI url = URI.create(BASE_URL + startOfPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        List<EpicTask> tasksFromManager = manager.getAllEpicTasks();
        List<SingleTask> tasksFromManager2 = manager.getAllSingleTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(0, tasksFromManager2.size(), "Некорректное количество задач");
    }

    @Test
    void shouldBeInteractionsIfTimeOverlay() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("",
                "",
                LocalDateTime.of(2024, 10, 18, 23, 30),
                Duration.ofMinutes(1));
        HttpResponse<String> response = addOrUpdateTaskResponse(client, singleTask);
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        response = addOrUpdateTaskResponse(client, singleTask);
        assertEquals(BaseHttpHandler.STATUS_HAS_INTERACTIONS, response.statusCode());
    }

    @Override
    public List<SingleTask> getListOfTasksFromManager() {
        return manager.getAllSingleTasks();
    }

    @Override
    public Optional<SingleTask> getTaskFromJson(String json) {
        return JsonTaskOption.getSingleTaskFromJson(json);
    }
}
