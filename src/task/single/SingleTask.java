package task.single;

import task.Status;
import task.TaskType;
import task.epic.SubTask;

import java.util.Objects;

public class SingleTask implements Task {

    protected final TaskType taskType;
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;

    public SingleTask(String name, String description) {
        taskType = TaskType.SINGLE_TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    protected SingleTask(String name, String description, TaskType taskType) {
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    protected SingleTask(SingleTask singleTask) {
        taskType = singleTask.getTaskType();
        this.name = singleTask.name;
        this.description = singleTask.description;
        this.status = singleTask.status;
        this.id = singleTask.id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SingleTask getCopy() {
        return new SingleTask(this);
    }

    public TaskType getTaskType() {
        return taskType;
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
