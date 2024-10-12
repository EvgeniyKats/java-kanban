package manager.task;

import task.single.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class TaskPriorityManager {
    public static final int MINIMUM_DURATION_OF_TASK_IN_MINUTES = 1;
    private final Map<LocalDateTime, Boolean> infoIsBusedThisTime;
    private final TreeSet<Task> sortedTasks;

    private TaskPriorityManager() {
        infoIsBusedThisTime = new HashMap<>();
        sortedTasks = new TreeSet<>();
    }

    public static TaskPriorityManager createTaskPriorityManager() {
        return new TaskPriorityManager();
    }

    public boolean add(Task task) {
        Duration duration = task.getDuration();
        if (duration != null && duration.toMinutes() >= MINIMUM_DURATION_OF_TASK_IN_MINUTES) {
            List<LocalDateTime> timeIntervalOfTask = new ArrayList<>();
            if (isBadTaskTime(task, timeIntervalOfTask)) {
                return false;
            } else {
                for (LocalDateTime localDateTime : timeIntervalOfTask) {
                    infoIsBusedThisTime.put(localDateTime, true);
                }
                sortedTasks.add(task);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean remove(Task task) {
        if (sortedTasks.contains(task)) {
            LocalDateTime start = task.getStartTime();
            LocalDateTime end = task.getEndTime();
            do {
                infoIsBusedThisTime.put(start, false);
                start = start.plusMinutes(MINIMUM_DURATION_OF_TASK_IN_MINUTES);
            } while (!start.equals(end));
            sortedTasks.remove(task);
            return true;
        }
        return false;
    }

    public void removeAll() {
        infoIsBusedThisTime.clear();
        sortedTasks.clear();
    }

    public boolean isBadTaskTime(Task task, List<LocalDateTime> timeIntervalToFillIn) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();
        boolean isBadTaskTime = false;
        do {
            Boolean busy = infoIsBusedThisTime.get(start);
            if (busy != null && busy) {
                isBadTaskTime = true;
                break;
            } else {
                timeIntervalToFillIn.add(start);
                start = start.plusMinutes(MINIMUM_DURATION_OF_TASK_IN_MINUTES);
            }
        } while (!start.equals(end));
        return isBadTaskTime;
    }

    public Set<Task> getPrioritizedTasks() {
        return new TreeSet<>(sortedTasks);
    }
}
