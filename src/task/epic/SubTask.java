package task.epic;

import task.single.SingleTask;

public class SubTask extends SingleTask {

    private final Integer epicId;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description);
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
