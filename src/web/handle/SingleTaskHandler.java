package web.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.single.SingleTask;
import task.single.Task;
import web.HttpTaskServer;
import web.JsonTaskOption;

import java.io.IOException;
import java.util.List;

public class SingleTaskHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                List<SingleTask> singleTasks = HttpTaskServer.getInstance().getManager().getAllSingleTasks();
                String text = JsonTaskOption.listOfTasksToJson(singleTasks);
                sendText(exchange, text, BaseHttpHandler.STATUS_SUCCESS_WITH_DATA);
            }
            case "POST" -> System.out.println("");
            case "DELETE" -> System.out.println("");
            default -> sendNotFound(exchange, "Метод " + exchange + " не поддерживается для " + exchange.getRequestURI());
        }
    }
}
