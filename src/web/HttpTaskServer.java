package web;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.task.TaskManager;
import web.handle.EpicTaskHandler;
import web.handle.SingleTaskHandler;
import web.handle.SubTaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class HttpTaskServer {
    private static boolean useFileBackendTaskManager = false;
    private static boolean needToRestoreTasks = false;
    private static Path path = null;
    private final static int PORT = 8080;
    private HttpServer httpServer;
    private final TaskManager manager;
    private boolean isAlive;

    protected HttpTaskServer() {
        isAlive = false;
        if (useFileBackendTaskManager) {
            manager = Managers.getFileBackendTaskManager(path, needToRestoreTasks);
        } else {
            manager = Managers.getDefault();
        }
    }

    public static HttpTaskServer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static final HttpTaskServer INSTANCE = new HttpTaskServer();
    }

    public boolean start() throws IOException {
        synchronized (getInstance()) {
            if (isAlive) {
                return false;
            } else {
                httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
                httpServer.createContext("/tasks", new SingleTaskHandler(manager));
                httpServer.createContext("/subtasks", new SubTaskHandler(manager));
                httpServer.createContext("/epics", new EpicTaskHandler(manager));
                httpServer.start();
                isAlive = true;
                return true;
            }
        }
    }

    public void stop() {
        synchronized (getInstance()) {
            if (httpServer != null) {
                httpServer.stop(0);
                isAlive = false;
            }
        }
    }

    public synchronized TaskManager getManager() {
        return manager;
    }

    public static void main(String[] args) {
        try {
            if (args[0].equals("true")) {
                useFileBackendTaskManager = true;
                path = Paths.get(args[1]);
                needToRestoreTasks = Boolean.parseBoolean(args[2]);
            }
            getInstance().start();
        } catch (Throwable throwable) {
            System.out.println("Неизвестная ошибка: " + Arrays.toString(throwable.getStackTrace()));
        }
    }
}
