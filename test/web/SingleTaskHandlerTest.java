package web;

import manager.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.epic.EpicTask;
import task.single.SingleTask;
import task.single.Task;
import web.handle.BaseHttpHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SingleTaskHandlerTest {
    private HttpClient client;
    private final TaskManager manager = HttpTaskServer.getInstance().getManager();
    private static final String BASE_URL = "http://localhost:8080/";

    @BeforeEach
    void beforeEach() throws IOException {
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
    void shouldBeBadRequestWithBadURI() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("", "");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + "tasks/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + "tasks/4/4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + "tasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    //POST TESTING:
    @Test
    void shouldBeSuccessAddedSingleTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + "tasks");
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
    void shouldBeNotAddedSingleTaskWithUnsupportedMethod() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    void shouldBeNotSuccessAddedSingleTaskAsEpic() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(epicTask);

        URI url = URI.create(BASE_URL + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        List<EpicTask> tasksFromManager = manager.getAllEpicTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void shouldBeSuccessUpdateSingleTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("nameBefore", "descBefore");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + "tasks");
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
    void shouldBeNotUpdateSingleTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");
        singleTask.setId(1);
        String taskJson = JsonTaskOption.taskToJson(singleTask);

        URI url = URI.create(BASE_URL + "tasks");
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

    @Test
    void shouldBeNotUpdateSingleTaskWithBadId() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("", "");
        singleTask.setId(1999);
        String jsonTask = JsonTaskOption.taskToJson(singleTask).replace("1999", "13x");
        URI url = URI.create(BASE_URL + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    //GET TESTING:

    @Test
    void shouldBeSuccessGetSingleTask() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("n", "d");
        int id = manager.addSingleTask(singleTask);
        URI url = URI.create(BASE_URL + "tasks/" + id);
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

    @Test
    void shouldBeSuccessGet3SingleTasks() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("n", "d");
        manager.addSingleTask(singleTask.getCopy());
        manager.addSingleTask(singleTask.getCopy());
        manager.addSingleTask(singleTask.getCopy());
        URI url = URI.create(BASE_URL + "tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> list = JsonTaskOption.getListOfTasksFromJson(response.body());
        assertEquals(3, list.size());
        assertEquals(manager.getAllSingleTasks(), list);
    }

    @Test
    void shouldBeNotGetNotExistTask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeBadRequestGetTaskWithBadId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "tasks/notNumber");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    //DELETE TESTING:

    @Test
    void shouldBeSuccessDeleted() throws IOException, InterruptedException {
        manager.addSingleTask(new SingleTask("", ""));
        URI url = URI.create(BASE_URL + "tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        assertEquals(0, manager.getAllSingleTasks().size());
    }

    @Test
    void shouldBeNotFoundDeleteWithNotExistId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeBadRequestDeleteWithBadId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "tasks/1/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeBadRequestDeleteWithBadURI() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "tasks/notNumber");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }
}
