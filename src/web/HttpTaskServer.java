package web;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class HttpTaskServer {
    private final static int PORT = 8080;
    private final HttpServer httpServer;

    private HttpTaskServer() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpTaskServer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static final HttpTaskServer INSTANCE = new HttpTaskServer();
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) {
        try {
            getInstance().start();
        } catch (Throwable throwable) {
            System.out.println("Неизвестная ошибка: " + Arrays.toString(throwable.getStackTrace()));
        } finally {
            getInstance().stop();
        }
    }
}
