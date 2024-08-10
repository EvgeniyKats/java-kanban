package task.epic;

import task.single.SingleTask;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends SingleTask {

    private final List<Integer> subTasksId;

    public EpicTask(String name, String description) {
        super(name, description);
        subTasksId = new ArrayList<>();
    }

    protected EpicTask(EpicTask epicTask) {
        super(epicTask);
        this.subTasksId = epicTask.subTasksId;
    }

    public List<Integer> getSubTasksId() {
        return new ArrayList<>(subTasksId);
    }

    public void addSubTaskId(Integer id) {
        subTasksId.add(id);
    }

    public void removeSubTaskId(Integer id) {
        subTasksId.remove(id);
    }

    public void clearSubTasksId() {
        subTasksId.clear();
    }

    @Override
    public EpicTask getCopy() {
        return new EpicTask(this);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subTasksId=" + subTasksId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
