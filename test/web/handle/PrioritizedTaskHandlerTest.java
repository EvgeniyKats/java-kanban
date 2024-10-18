package web.handle;

import manager.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.single.SingleTask;
import task.single.Task;
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

import static org.junit.jupiter.api.Assertions.*;

public class PrioritizedTaskHandlerTest {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String START_OF_PATH = "prioritized";
    private HttpClient client;
    private final TaskManager manager = HttpTaskServer.getInstance().getManager();

    @BeforeEach
    void beforeEach() throws IOException {
        client = HttpClient.newHttpClient();
        HttpTaskServer.getInstance().start();
        manager.clearEveryTasks();

        SingleTask singleTask = new SingleTask("",
                "",
                LocalDateTime.of(2024, 10, 18, 23, 30),
                Duration.ofMinutes(1));

        for (int i = 0; i < 20; i++) {
            LocalDateTime localDateTime = singleTask.getStartTime();
            singleTask.setStartTime(localDateTime.plusMinutes(1));
            manager.addSingleTask(singleTask.getCopy());
        }
    }

    @AfterEach
    void afterEach() {
        client.close();
        HttpTaskServer.getInstance().stop();
    }

    @Test
    void shouldBeSuccessGetPrioritized() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + START_OF_PATH))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITH_DATA, response.statusCode());
        List<Task> prioritizedReceived = JsonTaskOption.getListOfTasksFromJson(response.body());
        assertEquals(manager.getPrioritizedTasks(), prioritizedReceived);
    }

    @Test
    void shouldBeMethodNotAllowedWithDelete() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + START_OF_PATH))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    void shouldBeBadRequestWithBadURI() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + START_OF_PATH + "/some"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }
}
