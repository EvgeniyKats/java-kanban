package manager.history;

import task.single.SingleTask;

import java.util.List;

public interface HistoryManager {
    void add(SingleTask task);
    void remove(int id);
    void clearHistory();
    List<SingleTask> getHistory();
}
