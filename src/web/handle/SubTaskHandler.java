package web.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.task.ManagerSaveException;
import manager.task.TaskManager;
import task.epic.SubTask;
import web.JsonTaskOption;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public SubTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSplits = path.split("/");
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (pathSplits.length > 3) {
                    sendBadRequest(exchange, "Путь: " + exchange.getRequestURI().getPath() + " не поддерживается.");
                }
                if (pathSplits.length == 3) {
                    if (isPositiveInteger(pathSplits[2])) {
                        int id = Integer.parseInt(pathSplits[2]);
                        SubTask st = manager.getSubTask(id);
                        if (st == null) {
                            sendNotFound(exchange, "Task id: " + id + " отсутствует.");
                        } else {
                            sendSuccessWithBody(exchange, JsonTaskOption.taskToJson(st));
                        }

                    } else {
                        sendBadRequest(exchange, "Не удалось получить id задачи: " + pathSplits[2]);
                    }
                } else {
                    List<SubTask> subTasks = manager.getAllSubTasks();
                    String text = JsonTaskOption.listOfTasksToJson(subTasks);
                    sendSuccessWithBody(exchange, text);
                }
            }
            case "POST" -> {
                if (pathSplits.length != 2) {
                    sendBadRequest(exchange, "Путь: " + exchange.getRequestURI().getPath() + " не поддерживается.");
                    return;
                }
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                Optional<SubTask> optional = JsonTaskOption.getSubTaskFromJson(body);
                if (optional.isPresent()) {
                    SubTask subTask = optional.get();
                    try {
                        boolean success;
                        if (subTask.getId() == null) {
                            success = manager.addSubTask(subTask) > 0;
                        } else {
                            success = manager.updateSubTask(subTask);
                        }

                        if (success) {
                            sendSuccessWithoutBody(exchange);
                        } else {
                            sendHasInteractions(exchange, "Ошибка, время задачи пересекается с существующими " +
                                    "или задача отсутствует.");
                        }
                    } catch (ManagerSaveException e) {
                        sendInternalServerError(exchange, Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    sendBadRequest(exchange, "Не удалось получить задачу из тела: " + body);
                }
            }
            case "DELETE" -> {
                if (pathSplits.length != 3) {
                    sendBadRequest(exchange, "Путь: " + exchange.getRequestURI().getPath() + " не поддерживается.");
                    return;
                }
                if (isPositiveInteger(pathSplits[2])) {
                    int id = Integer.parseInt(pathSplits[2]);
                    boolean success = manager.removeSubTask(id);
                    if (success) {
                        sendSuccessWithoutBody(exchange);
                    } else {
                        sendNotFound(exchange, "Task id: " + id + " отсутствует.");
                    }
                } else {
                    sendBadRequest(exchange, "Не удалось получить id задачи: " + pathSplits[2]);
                }
            }
            default -> sendMethodNotAllowed(exchange,
                    "Метод " + exchange + " не поддерживается для " + exchange.getRequestURI());
        }
    }
}
