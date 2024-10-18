package web.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.task.TaskManager;
import task.single.Task;
import web.JsonTaskOption;

import java.io.IOException;
import java.util.List;

public class PrioritizedTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public PrioritizedTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!method.equals("GET")) {
            sendMethodNotAllowed(exchange, "Метод " + method + " не поддерживается для "
                    + exchange.getRequestURI());
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String[] pathSplits = path.split("/");
        if (pathSplits.length > 2) {
            sendBadRequest(exchange, "Путь: " + exchange.getRequestURI().getPath() + " не поддерживается.");
        }
        List<Task> prioritized = manager.getPrioritizedTasks();
        sendSuccessWithBody(exchange, JsonTaskOption.listOfTasksToJson(prioritized));
    }
}
