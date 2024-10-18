package web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
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

public class EpicTaskHandlerTest extends AbstractTasksHandlersTest {
    @BeforeEach
    void beforeEach() throws IOException {
        partPath = "epics";
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

        URI url = URI.create(BASE_URL + partPath + "/some");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());

        url = URI.create(BASE_URL + partPath + "/4/4/4");
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

    //POST TESTING:
    @Test
    void shouldBeSuccessAddedEpicTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(epicTask);

        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        List<EpicTask> tasksFromManager = manager.getAllEpicTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(epicTask.getName(), tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void shouldBeNotSuccessAddedEpicTaskAsSingle() throws IOException, InterruptedException {
        SingleTask singleTask = new SingleTask("   Test 1 $ @   ", "   Testing task 1  ");
        String taskJson = JsonTaskOption.taskToJson(singleTask);

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
    void shouldBeSuccessUpdateEpicTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("nameBefore", "descBefore");
        String taskJson = JsonTaskOption.taskToJson(epicTask);

        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        EpicTask epicTaskReceived = manager.getAllEpicTasks().getFirst();
        epicTaskReceived.setName("nameAfter");
        epicTaskReceived.setDescription("descAfter");

        taskJson = JsonTaskOption.taskToJson(epicTaskReceived);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());

        List<EpicTask> tasksFromManager = manager.getAllEpicTasks();
        assertEquals(1, tasksFromManager.size());
        EpicTask temp = tasksFromManager.getFirst();
        assertEquals(epicTaskReceived.getName(), temp.getName());
        assertEquals(epicTaskReceived.getDescription(), temp.getDescription());
    }

    @Test
    void shouldBeSuccessGetSubTasksList() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("   Test 1 $ @   ", "   Testing task 1  ");
        int id = manager.addEpicTask(epicTask);
        SubTask subTask = new SubTask("", "", id);
        for (int i = 0; i < 5; i++) {
            manager.addSubTask(subTask.getCopy());
        }
        URI url = URI.create(BASE_URL + partPath + "/" + id + "/subtasks");
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
        URI url = URI.create(BASE_URL + partPath + "/some/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_BAD_REQUEST, response.statusCode());
    }

    @Test
    void shouldBeNotFoundRequestGetSubTasksListEpicNotExist() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + partPath + "/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldBeNotUpdateTaskIfNotExistInManager() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("   Test 1 $ @   ", "   Testing task 1  ");
        epicTask.setId(1);
        String taskJson = JsonTaskOption.taskToJson(epicTask);

        URI url = URI.create(BASE_URL + partPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_HAS_INTERACTIONS, response.statusCode());

        List<EpicTask> tasksFromManager = manager.getAllEpicTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    //GET TESTING:

    @Test
    void shouldBeSuccessGetEpicTask() throws IOException, InterruptedException {
        EpicTask epicTask = new EpicTask("n", "d");
        int id = manager.addEpicTask(epicTask);
        URI url = URI.create(BASE_URL + partPath + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Optional<EpicTask> optional = JsonTaskOption.getEpicTaskFromJson(response.body());
        assertTrue(optional.isPresent());

        EpicTask received = optional.get();
        assertEquals(epicTask.getId(), received.getId());
        assertEquals(epicTask.getName(), received.getName());
        assertEquals(epicTask.getDescription(), received.getDescription());
    }

    //DELETE TESTING:

    @Test
    void shouldBeSuccessDeleted() throws IOException, InterruptedException {
        manager.addEpicTask(new EpicTask("", ""));
        URI url = URI.create(BASE_URL + partPath + "/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(BaseHttpHandler.STATUS_SUCCESS_WITHOUT_DATA, response.statusCode());
        assertEquals(0, manager.getAllEpicTasks().size());
    }
}
