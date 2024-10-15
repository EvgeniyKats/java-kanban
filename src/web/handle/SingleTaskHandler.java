package web.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SingleTaskHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> System.out.println("GET");
            case "POST" -> System.out.println("");
            case "DELETE" -> System.out.println("");
            default -> sendNotFound(exchange, "Метод " + exchange + " не поддерживается для " + exchange.getRequestURI());
        }
    }
}
