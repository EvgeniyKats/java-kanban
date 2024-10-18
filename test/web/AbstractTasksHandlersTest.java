package web;

import manager.task.TaskManager;
import org.junit.jupiter.api.Test;
import task.single.Task;
import web.handle.BaseHttpHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractTasksHandlersTest {
    protected static final String BASE_URL = "http://localhost:8080/";
    protected HttpClient client;
    protected final TaskManager manager = HttpTaskServer.getInstance().getManager();
    protected String partPath = "";
    protected Task task;

    @Test
    void shouldBeBadRequestWithBadURI() throws IOException, InterruptedException {
        String taskJson = JsonTaskOption.taskToJson(task);

        URI url = URI.create(BASE_URL + partPath + "/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + partPath + "/4/4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + partPath);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeNotAddedTaskWithUnsupportedMethod() throws IOException, InterruptedException {
        String taskJson = JsonTaskOption.taskToJson(task);

        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    void shouldBeNotUpdateSingleTaskWithBadId() throws IOException, InterruptedException {
        task.setId(1999);
        String jsonTask = JsonTaskOption.taskToJson(task).replace("1999", "13x");
        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeSuccessGet3Tasks() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(JsonTaskOption.taskToJson(task)))
                .build();
        for (int i = 0; i < 3; i++) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        }

        url = URI.create(BASE_URL + partPath);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> list = JsonTaskOption.getListOfTasksFromJson(response.body());
        assertEquals(3, list.size());
    }

    @Test
    void shouldBeNotGetNotExistTask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + partPath + "/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeBadRequestGetTaskWithBadId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + partPath +  "/notNumber");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeNotFoundDeleteWithNotExistId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + partPath + "/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeBadRequestDeleteWithBadId() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + partPath + "/1/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeBadRequestDeleteWithBadURI() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + partPath + "/notNumber");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }
}
