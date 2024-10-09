package task.epic;

import task.TaskType;
import task.single.SingleTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends SingleTask {

    private final Integer epicId;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description, TaskType.SUB_TASK);
        this.epicId = epicId;
    }

    public SubTask(String description, String name, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(description, name, startTime, duration);
        this.epicId = epicId;
    }

    protected SubTask(SubTask subTask) {
        super(subTask);
        this.epicId = subTask.epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public SubTask getCopy() {
        return new SubTask(this);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
