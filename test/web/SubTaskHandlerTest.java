package web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.epic.EpicTask;
import task.single.SingleTask;
import web.handle.BaseHttpHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SingleTaskHandlerTest extends AbstractTasksHandlersTest {
    @BeforeEach
    void beforeEach() throws IOException {
        task = new SingleTask("", "");
        partPath = "tasks";
        client = HttpClient.newHttpClient();
        HttpTaskServer.getInstance().start();
        manager.clearEveryTasks();
    }

    @AfterEach
    void afterEach() {
        client.close();
        HttpTaskServer.getInstance().stop();
    }

    //POST TESTING:
    @Test
    void shouldBeSuccessAddedSingleTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        List<SingleTask> tasksFromManager = manager.getAllSingleTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(singleTask.getName(), tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void shouldBeNotSuccessAddedSingleTaskAsEpic() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(epicTask);

        URI url = URI.create(BASE_URL + partPath);
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
    void shouldBeSuccessUpdateSingleTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("nameBefore", "descBefore");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        SingleTask singleTaskReceived = manager.getAllSingleTasks().getFirst();
        singleTaskReceived.setName("nameAfter");
        singleTaskReceived.setStatus(Status.IN_PROGRESS);
        singleTaskReceived.setDescription("descAfter");

        taskJson = JsonTaskOption.taskToJson(singleTaskReceived);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        List<SingleTask> tasksFromManager = manager.getAllSingleTasks();
        assertEquals(1, tasksFromManager.size());
        SingleTask temp = tasksFromManager.getFirst();
        assertEquals(singleTaskReceived.getName(), temp.getName());
        assertEquals(singleTaskReceived.getDescription(), temp.getDescription());
        assertEquals(singleTaskReceived.getStatus(), temp.getStatus());
    }

    @Test
    void shouldBeNotUpdateTaskIfNotExistInManager() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");
        singleTask.setId(1);
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_HAS_INTERACTIONS, response.statusCode());

        List<SingleTask> tasksFromManager = manager.getAllSingleTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    //GET TESTING:

    @Test
    void shouldBeSuccessGetSingleTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("n", "d");
        int id = manager.addSingleTask(singleTask);
        URI url = URI.create(BASE_URL + partPath + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Optional<SingleTask> optional = JsonTaskOption.getSingleTaskFromJson(response.body());
        assertTrue(optional.isPresent());

        SingleTask received = optional.get();
        assertEquals(singleTask.getId(), received.getId());
        assertEquals(singleTask.getName(), received.getName());
        assertEquals(singleTask.getDescription(), received.getDescription());
    }

    //DELETE TESTING:

    @Test
    void shouldBeSuccessDeleted() throws IOException, InterruptedException {
        manager.addSingleTask(new SingleTask("", ""));
        URI url = URI.create(BASE_URL + partPath + "/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        assertEquals(0, manager.getAllSingleTasks().size());
    }
}
