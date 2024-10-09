package task.single;

import task.Status;
import task.TaskType;
import task.epic.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SingleTask implements Task {

    protected final TaskType taskType;
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;

    public SingleTask(String name, String description) {
        taskType = TaskType.SINGLE_TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public SingleTask(String description, String name, LocalDateTime startTime, Duration duration) {
        taskType = TaskType.SINGLE_TASK;
        this.description = description;
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
    }

    protected SingleTask(String name, String description, TaskType taskType) {
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    protected SingleTask(String name, String description, TaskType taskType, LocalDateTime startTime, Duration duration) {
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    protected SingleTask(SingleTask singleTask) {
        taskType = singleTask.getTaskType();
        this.name = singleTask.name;
        this.description = singleTask.description;
        this.status = singleTask.status;
        this.id = singleTask.id;
        this.startTime = singleTask.startTime;
        this.duration = singleTask.duration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public SingleTask getCopy() {
        return new SingleTask(this);
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleTask singleTask = (SingleTask) o;
        return Objects.equals(id, singleTask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public String toString(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId()).append(",");
        builder.append(task.getTaskType()).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getStatus()).append(",");
        builder.append(task.getDescription()).append(",");

        if (task.getTaskType().equals(TaskType.SUB_TASK)) {
            SubTask subTask = (SubTask) task;
            builder.append(subTask.getEpicId());
        }

        return builder.toString();
    }
}
