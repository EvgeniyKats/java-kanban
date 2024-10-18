package web.handle;

import manager.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;
import web.HttpTaskServer;
import web.JsonTaskOption;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryHandlerTest {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String START_OF_PATH = "history";
    private HttpClient client;
    private final TaskManager manager = HttpTaskServer.getInstance().getManager();

    @BeforeEach
    void beforeEach() throws IOException {
        client = HttpClient.newHttpClient();
        HttpTaskServer.getInstance().start();
        manager.clearEveryTasks();

        SingleTask singleTask = new SingleTask("", "");
        int idSingle1 = manager.addSingleTask(singleTask.getCopy());
        int idSingle2 = manager.addSingleTask(singleTask.getCopy());
        EpicTask epicTask = new EpicTask("", "");
        int idEpic1 = manager.addEpicTask(epicTask.getCopy());
        int idEpic2 = manager.addEpicTask(epicTask.getCopy());
        SubTask subTask = new SubTask("", "", idEpic1);
        int idSub1 = manager.addSubTask(subTask.getCopy());
        int idSub2 = manager.addSubTask(subTask.getCopy());
        manager.getEpicTask(idEpic1);
        manager.getEpicTask(idEpic2);
        manager.getSubTask(idSub2);
        manager.getSubTask(idSub1);
        manager.getSingleTask(idSingle2);
        manager.getSingleTask(idSingle1);
    }

    @AfterEach
    void afterEach() {
        client.close();
        HttpTaskServer.getInstance().stop();
    }

    @Test
    void shouldBeSuccessGetHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + START_OF_PATH))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITH_DATA, response.statusCode());
        List<Task> historyReceived = JsonTaskOption.getListOfTasksFromJson(response.body());
        assertEquals(manager.getHistory(), historyReceived);
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
