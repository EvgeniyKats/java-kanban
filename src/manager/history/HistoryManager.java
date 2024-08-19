package manager.history;

import task.single.SingleTask;

import java.util.List;

public interface HistoryManager {
    void add(SingleTask task);

    List<SingleTask> getHistory();
}
