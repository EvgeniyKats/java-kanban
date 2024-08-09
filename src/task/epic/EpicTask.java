package task.epic;

import task.Status;
import task.single.Task;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    private final List<SubTask> subTasks;

    public EpicTask(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
    }

    public EpicTask(String name, String description, Integer id, List<SubTask> subTasks) {
        super(name, description, null, id);
        this.subTasks = subTasks;
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void updateSubTask(SubTask subTaskUpdated, SubTask subTaskDeprecated) {
        removeSubTask(subTaskDeprecated);
        addSubTask(subTaskUpdated);
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    public void removeSubTask(int id) {
        for (SubTask subTask : subTasks) {
            if (subTask.getId().equals(id)) {
                removeSubTask(subTask);;
                return;
            }
        }
        System.out.println("SubTask не был удалён, в Epic отсутствует subTask с id=" + id);
    }

    @Override
    public EpicTask getCopy() {
        return new EpicTask(getName(), getDescription(), getId(), getSubTasks());
    }

    @Override
    public Status getStatus() {
        if (subTasks.isEmpty()) return Status.NEW;

        for (SubTask subTask : subTasks) {
            if (Status.IN_PROGRESS.equals(subTask.getStatus())) {
                return Status.IN_PROGRESS;
            }
        }

        boolean haveNew = false;
        boolean haveDone = false;

        for (SubTask subTask : subTasks) {
            if (haveDone && haveNew) {
                break;
            }

            if (!haveNew && Status.NEW.equals(subTask.getStatus())) {
                haveNew = true;
                continue;
            }

            if (!haveDone && Status.DONE.equals(subTask.getStatus())) {
                haveDone = true;
            }
        }

        if (haveDone && haveNew) {
            return Status.IN_PROGRESS;
        } else if (haveNew) {
            return Status.NEW;
        } else {
            return Status.DONE;
        }
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", status=" + getStatus() +
                ", subTasks=" + subTasks +
                '}';
    }
}
