package manager.history;

import task.single.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    void clearHistory();

    List<Task> getHistory();
}
