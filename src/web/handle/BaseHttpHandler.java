package web.handle;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    private static final int MAX_INTEGER_FIRST_5_NUMBERS = 21474;
    private static final int MAX_INTEGER_LAST_5_NUMBERS = 83647;
    private static final int MAX_INTEGER_SYMBOLS_COUNT = 10;
    public static final int STATUS_SUCCESS_WITH_DATA = 200;
    public static final int STATUS_SUCCESS_WITHOUT_DATA = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int STATUS_HAS_INTERACTIONS = 406;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    public BaseHttpHandler() {
    }

    protected void sendSuccessWithBody(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(STATUS_SUCCESS_WITH_DATA, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendSuccessWithoutBody(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(STATUS_SUCCESS_WITHOUT_DATA, 0);
        httpExchange.close();
    }

    protected void sendBadRequest(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(STATUS_BAD_REQUEST, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendMethodNotAllowed(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendNotFound(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendHasInteractions(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(STATUS_HAS_INTERACTIONS, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendInternalServerError(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(STATUS_INTERNAL_SERVER_ERROR, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected boolean isPositiveInteger(String s) {
        if (s == null || s.isEmpty() || s.length() > MAX_INTEGER_SYMBOLS_COUNT) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        if (s.length() == MAX_INTEGER_SYMBOLS_COUNT) {
            int first5Symbols = Integer.parseInt(s.substring(0, 5));
            if (first5Symbols > MAX_INTEGER_FIRST_5_NUMBERS) {
                return false;
            } else if (first5Symbols < MAX_INTEGER_FIRST_5_NUMBERS) {
                return true;
            } else {
                int last6Symbols = Integer.parseInt(s.substring(5, 10));
                return last6Symbols <= MAX_INTEGER_LAST_5_NUMBERS;
            }
        } else {
            return true;
        }
    }

    protected String readBody(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}
