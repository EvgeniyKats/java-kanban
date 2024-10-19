package web.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.task.ManagerSaveException;
import manager.task.TaskManager;
import task.epic.EpicTask;
import web.JsonTaskOption;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EpicTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public EpicTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSplits = path.split("/");

        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (pathSplits.length > 4) {
                    sendBadRequest(exchange, "Путь: " + exchange.getRequestURI().getPath() + " не поддерживается.");
                }
                if (pathSplits.length == 4) {
                    if (isPositiveInteger(pathSplits[2])) {
                        int id = Integer.parseInt(pathSplits[2]);
                        EpicTask epic = manager.getEpicTask(id);
                        if (epic == null) {
                            sendNotFound(exchange, "Task id: " + id + " отсутствует.");
                        } else {
                            sendSuccessWithBody(exchange, JsonTaskOption.listOfIntegersToJson(epic.getSubTasksId()));
                        }

                    } else {
                        sendBadRequest(exchange, "Не удалось получить id задачи: " + pathSplits[2]);
                    }
                } else if (pathSplits.length == 3) {
                    if (isPositiveInteger(pathSplits[2])) {
                        int id = Integer.parseInt(pathSplits[2]);
                        EpicTask epic = manager.getEpicTask(id);
                        if (epic == null) {
                            sendNotFound(exchange, "Task id: " + id + " отсутствует.");
                        } else {
                            sendSuccessWithBody(exchange, JsonTaskOption.taskToJson(epic));
                        }

                    } else {
                        sendBadRequest(exchange, "Не удалось получить id задачи: " + pathSplits[2]);
                    }
                } else {
                    List<EpicTask> epicTasks = manager.getAllEpicTasks();
                    String text = JsonTaskOption.listOfTasksToJson(epicTasks);
                    sendSuccessWithBody(exchange, text);
                }
            }
            case "POST" -> {
                if (pathSplits.length != 2) {
                    sendBadRequest(exchange, "Путь: " + exchange.getRequestURI().getPath() + " не поддерживается.");
                    return;
                }
                String body = readBody(exchange);
                Optional<EpicTask> optional = JsonTaskOption.getEpicTaskFromJson(body);
                if (optional.isEmpty()) {
                    sendBadRequest(exchange, "Не удалось получить задачу из тела: " + body);
                    return;
                }
                EpicTask epicTask = optional.get();
                try {
                    boolean success;
                    if (epicTask.getId() == null) {
                        success = manager.addEpicTask(epicTask) > 0;
                    } else {
                        success = manager.updateEpicTask(epicTask);
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
            }
            case "DELETE" -> {
                if (pathSplits.length != 3) {
                    sendBadRequest(exchange, "Путь: " + exchange.getRequestURI().getPath() + " не поддерживается.");
                    return;
                }
                if (isPositiveInteger(pathSplits[2])) {
                    int id = Integer.parseInt(pathSplits[2]);
                    boolean success = manager.removeEpicTask(id);
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
