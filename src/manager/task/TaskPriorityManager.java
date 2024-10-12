package manager.task;

import task.single.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class TaskPriorityManager {
    public static final int MINIMUM_DURATION_OF_TASK_IN_MINUTES = 1;
    private final Map<LocalDateTime, InfoAboutTime> infoIsBusedThisTime;
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
                timeIntervalOfTask.forEach(localDateTime -> setBusy(localDateTime, task));
                sortedTasks.add(task);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean updateTask(Task task) {
        Task old = null;

        for (Task t : sortedTasks) {
            if (t.equals(task)) {
                old = t;
                break;
            }
        }

        if (old == null) {
            return false;
        } else {
            ArrayList<LocalDateTime> datesToAdd = new ArrayList<>();
            if (isBadTaskTime(task, datesToAdd)) {
                return false;
            } else {
                List<LocalDateTime> datesToDelete = getIntervalsOfTask(old);
                datesToDelete.forEach(localDateTime -> setNotBusy(localDateTime, task));
                datesToAdd.forEach(localDateTime -> setBusy(localDateTime, task));
                sortedTasks.remove(task);
                sortedTasks.add(task);
                return true;
            }
        }
    }

    public boolean remove(Task task) {
        if (sortedTasks.contains(task)) {
            LocalDateTime start = task.getStartTime();
            LocalDateTime end = task.getEndTime();
            do {
                setNotBusy(start, task);
                start = start.plusMinutes(MINIMUM_DURATION_OF_TASK_IN_MINUTES);
            } while (!start.equals(end));
            sortedTasks.remove(task);
            return true;
        }
        return false;
    }

    public void clearAll() {
        infoIsBusedThisTime.clear();
        sortedTasks.clear();
    }

    public boolean isBadTaskTime(Task task, List<LocalDateTime> timeIntervalToFillIn) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();
        boolean isBadTaskTime = false;
        do {
            InfoAboutTime info = infoIsBusedThisTime.get(start);
            if (info != null && info.busy && !info.task.equals(task)) {
                isBadTaskTime = true;
                break;
            } else {
                timeIntervalToFillIn.add(start);
                start = start.plusMinutes(MINIMUM_DURATION_OF_TASK_IN_MINUTES);
            }
        } while (!start.equals(end));
        return isBadTaskTime;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    private List<LocalDateTime> getIntervalsOfTask(Task task) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();
        List<LocalDateTime> result = new ArrayList<>();
        do {
            result.add(start);
            start = start.plusMinutes(MINIMUM_DURATION_OF_TASK_IN_MINUTES);
        } while (!start.equals(end));
        return result;
    }

    private void setNotBusy(LocalDateTime localDateTime, Task task) {
        infoIsBusedThisTime.put(localDateTime, new InfoAboutTime(task, false));
    }

    private void setBusy(LocalDateTime localDateTime, Task task) {
        infoIsBusedThisTime.put(localDateTime, new InfoAboutTime(task, true));
    }

    private static class InfoAboutTime {
        final Task task;
        final Boolean busy;

        public InfoAboutTime(Task task, boolean busy) {
            this.task = task;
            this.busy = busy;
        }
    }
}
