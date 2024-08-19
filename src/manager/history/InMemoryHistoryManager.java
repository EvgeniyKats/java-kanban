package manager.history;

import task.single.SingleTask;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<SingleTask> tasks = new ArrayList<>();

    @Override
    public void add(SingleTask task) {
        if (tasks.size() == 10) {
            tasks.removeFirst();
        }
        tasks.add(task);
    }

    @Override
    public List<SingleTask> getHistory() {
        List<SingleTask> result = new ArrayList<>();

        for (SingleTask task : tasks) {
            result.add(task.getCopy());
        }

        return result;
    }
}
