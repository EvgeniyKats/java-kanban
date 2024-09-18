package task.single;

import task.Status;
import task.TaskType;

public interface Task {
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
}
