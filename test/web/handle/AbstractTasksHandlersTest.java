package web.handle;

import manager.task.TaskManager;
import org.junit.jupiter.api.Test;
import task.single.Task;
import web.HttpTaskServer;
import web.JsonTaskOption;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTasksHandlersTest<T extends Task> {
    protected static final String BASE_URL = "http://localhost:8080/";
    protected HttpClient client;
    protected final TaskManager manager = HttpTaskServer.getInstance().getManager();
    protected String startOfPath = "";
    protected T task;

    //POST TEST
    @Test
    void shouldBeSuccessAddedTask() throws IOException, InterruptedException {
        HttpResponse<String> response = addOrUpdateTaskResponse(client, task);
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        List<T> tasksFromManager = getListOfTasksFromManager();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task.getName(), tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void shouldBeNotAddedTaskWithUnsupportedMethod() throws IOException, InterruptedException {
        String taskJson = JsonTaskOption.taskToJson(task);

        URI url = URI.create(BASE_URL + startOfPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    void shouldBeSuccessUpdateTask() throws IOException, InterruptedException {
        HttpResponse<String> response = addOrUpdateTaskResponse(client, task);
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        T taskReceived = getListOfTasksFromManager().getFirst();
        taskReceived.setName("nameAfter");
        taskReceived.setDescription("descAfter");
        response = addOrUpdateTaskResponse(client, taskReceived);
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        List<T> tasksFromManager = getListOfTasksFromManager();
        assertEquals(1, tasksFromManager.size());
        T temp = tasksFromManager.getFirst();
        assertEquals(taskReceived.getName(), temp.getName());
        assertEquals(taskReceived.getDescription(), temp.getDescription());
        assertEquals(taskReceived.getId(), temp.getId());
    }

    @Test
    void shouldBeNotUpdateTaskIfNotExistInManager() throws IOException, InterruptedException {
        task.setId(1);
        HttpResponse<String> response = addOrUpdateTaskResponse(client, task);
        assertEquals(BaseHttpHandler.STATUS_HAS_INTERACTIONS, response.statusCode());
        List<T> tasksFromManager = getListOfTasksFromManager();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void shouldBeNotUpdateTaskWithBadId() throws IOException, InterruptedException {
        task.setId(1999);
        String jsonTask = JsonTaskOption.taskToJson(task).replace("1999", "13x");
        URI url = URI.create(BASE_URL + startOfPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    //GET TEST
    @Test
    void shouldBeSuccessGetTask() throws IOException, InterruptedException {
        task.setName("n");
        task.setDescription("d");
        HttpResponse<String> response = addOrUpdateTaskResponse(client, task);
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        T taskInManager = getListOfTasksFromManager().getFirst();
        response = getTaskResponse(client, taskInManager.getId());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITH_DATA, response.statusCode());
        Optional<T> optional = getTaskFromJson(response.body());
        assertTrue(optional.isPresent());

        T received = optional.get();
        assertEquals(taskInManager.getId(), received.getId());
        assertEquals(taskInManager.getName(), received.getName());
        assertEquals(taskInManager.getDescription(), received.getDescription());
    }

    @Test
    void shouldBeNotFoundWithMaxInteger() throws IOException, InterruptedException {
        HttpResponse<String> response = getTaskResponse(client, Integer.MAX_VALUE);
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeBadRequestIfIdMoreThanMaxInteger() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + startOfPath + "/" + "2147483648")) // 2147483647 = MAX
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeSuccessGet3Tasks() throws IOException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            HttpResponse<String> response = addOrUpdateTaskResponse(client, task);
            assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        }

        HttpResponse<String> response = getListOfTasksResponse(client);
        List<Task> list = JsonTaskOption.getListOfTasksFromJson(response.body());
        assertEquals(3, list.size());
    }

    @Test
    void shouldBeNotGetNotExistTask() throws IOException, InterruptedException {
        HttpResponse<String> response = getTaskResponse(client, 100);
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeBadRequestGetTaskResponseWithBadId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/notNumber");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    //DELETE TEST
    @Test
    void shouldBeNotFoundDeleteWithNotExistId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/100");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeBadRequestDeleteWithBadId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/1/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeBadRequestDeleteWithBadURI() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/notNumber");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeSuccessDeleted() throws IOException, InterruptedException {
        HttpResponse<String> response = addOrUpdateTaskResponse(client, task);
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        int id = getListOfTasksFromManager().getFirst().getId();

        response = removeTaskResponse(client, id);
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        assertEquals(0, manager.getAllSingleTasks().size());
    }

    @Test
    void shouldBeBadRequestWithBadURI() throws IOException, InterruptedException {
        String taskJson = JsonTaskOption.taskToJson(task);

        URI url = URI.create(BASE_URL + startOfPath + "/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + startOfPath + "/4/4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + startOfPath);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    public HttpResponse<String> addOrUpdateTaskResponse(HttpClient client, Task task) throws IOException, InterruptedException {
        String taskJson = JsonTaskOption.taskToJson(task);
        URI url = URI.create(BASE_URL + startOfPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getTaskResponse(HttpClient client, int id) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getListOfTasksResponse(HttpClient client) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> removeTaskResponse(HttpClient client, int id) throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public abstract List<T> getListOfTasksFromManager();

    public abstract Optional<T> getTaskFromJson(String json);
}
