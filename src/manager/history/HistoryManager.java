package manager.history;

import task.single.SingleTask;
import task.single.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    void clearHistory();

    List<SingleTask> getHistory();
}
