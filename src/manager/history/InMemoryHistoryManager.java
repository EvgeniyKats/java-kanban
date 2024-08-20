package manager.history;

import task.single.SingleTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_SIZE_LIST = 10;
    private final List<SingleTask> tasks = new LinkedList<>();

    @Override
    public void add(SingleTask task) {
        if (tasks.size() == MAX_SIZE_LIST) {
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
