package manager.task;

import task.single.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class TaskPriorityManager {
    private final Map<Long, Boolean> timeStampInMinutesAndIsBusy;
    private final TreeSet<Task> sortedTasks;

    private TaskPriorityManager() {
        timeStampInMinutesAndIsBusy = new HashMap<>();
        sortedTasks = new TreeSet<>();
    }

    public static TaskPriorityManager createTaskPriorityManager() {
        return new TaskPriorityManager();
    }

    public boolean add(Task task) {
        //TODO
        return false;
    }

    public boolean remove(Task task) {
        //TODO
        return false;
    }

    public void removeAll() {
        timeStampInMinutesAndIsBusy.clear();
        sortedTasks.clear();
    }

    public boolean isValidTaskTime(Task task) {
        //TODO
        return false;
    }
}
