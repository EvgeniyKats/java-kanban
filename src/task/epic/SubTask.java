package task.epic;

import task.Status;
import task.single.Task;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String name, String description) {
        super(name, description);
        epicId = null;
    }

    public SubTask(String name, String description, Status status, Integer id, Integer epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public SubTask getCopy() {
        return new SubTask(getName(), getDescription(), getStatus(), getId(), getEpicId());
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", status=" + super.getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
