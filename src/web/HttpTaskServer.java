package web;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.task.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final static int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager manager;
    private boolean isAlive;

    private HttpTaskServer() {
        try {
            isAlive = false;
            manager = Managers.getDefault();
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
        synchronized (getInstance()) {
            if (isAlive) {
                System.out.println("Сервер уже запущен: " + httpServer.getAddress());
            } else {
                httpServer.start();
                isAlive = true;
            }
        }
    }

    public void stop() {
        synchronized (getInstance()) {
            httpServer.stop(0);
            isAlive = false;
        }
    }

    public synchronized TaskManager getManager() {
        return manager;
    }

    public static void main(String[] args) {
        getInstance().start();
    }
}
