package web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import task.epic.EpicTask;
import task.single.SingleTask;

import java.io.IOException;
import java.net.http.HttpClient;

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
}
