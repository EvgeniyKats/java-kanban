package task.single;

import task.Status;
import task.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Task extends Comparable<Task> {
    String getName();

    String getDescription();

    Integer getId();

    Status getStatus();

    void setName(String name);

    void setDescription(String description);

    void setId(Integer id);

    void setStatus(Status status);

    SingleTask getCopy();

    TaskType getTaskType();

    LocalDateTime getStartTime();

    void setStartTime(LocalDateTime startTime);

    Duration getDuration();

    void setDuration(Duration duration);

    LocalDateTime getEndTime();
}
