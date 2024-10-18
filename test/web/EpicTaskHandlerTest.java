package web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.epic.EpicTask;
import task.epic.SubTask;
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

public class EpicTaskHandlerTest extends AbstractTasksHandlersTest<EpicTask> {
    @BeforeEach
    void beforeEach() throws IOException {
        startOfPath = "epics";
        task = new EpicTask("", "");
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
    @Override
    void shouldBeBadRequestWithBadURI() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("", "");
        String taskJson = JsonTaskOption.taskToJson(epicTask);

        URI url = URI.create(BASE_URL + startOfPath + "/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + startOfPath + "/4/4/4");
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

    @Test
    void shouldBeNotSuccessAddedEpicTaskAsSingle() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

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
    void shouldBeSuccessGetSubTasksList() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("   Test 1 $ @   ", "   Testing task 1  ");
        int id = manager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("", "", id);
        for (int i = 0; i < 5; i++) {
            manager.addSubTask(subTask.getCopy());
        }
        URI url = URI.create(BASE_URL + startOfPath + "/" + id + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITH_DATA, response.statusCode());
        List<Integer> list = JsonTaskOption.getListOfIntegersFromJson(response.body());
        assertEquals(5, epicTask.getSubTasksId().size());
        assertEquals(epicTask.getSubTasksId(), list);
    }

    @Test
    void shouldBeBadRequestGetSubTasksListBadURI() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/some/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeNotFoundRequestGetSubTasksListIfEpicNotExist() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + startOfPath + "/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Override
    public List<EpicTask> getListOfTasksFromManager() {
        return manager.getAllEpicTasks();
    }

    @Override
    public Optional<EpicTask> getTaskFromJson(String json) {
        return JsonTaskOption.getEpicTaskFromJson(json);
    }
}
