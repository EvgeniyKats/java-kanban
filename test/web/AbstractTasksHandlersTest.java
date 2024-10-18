package web;

import manager.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import task.single.SingleTask;
import web.handle.BaseHttpHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AllTasksHandlersTest {
    private HttpClient client;
    private final TaskManager manager = HttpTaskServer.getInstance().getManager();
    private static final String BASE_URL = "http://localhost:8080/";
    protected String partPath = "";

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
}
