package manager.task;

import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    @Override
    public boolean addSingleTask(SingleTask singleTask) {
        save();
        return super.addSingleTask(singleTask);
    }

    @Override
    public boolean addSubTask(SubTask subTask) {
        save();
        return super.addSubTask(subTask);
    }

    @Override
    public boolean addEpicTask(EpicTask epicTask) {
        save();
        return super.addEpicTask(epicTask);
    }

    @Override
    public boolean updateSingleTask(SingleTask singleTask) {
        save();
        return super.updateSingleTask(singleTask);
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        save();
        return super.updateSubTask(subTask);
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTask) {
        save();
        return super.updateEpicTask(epicTask);
    }

    @Override
    public boolean removeSingleTask(int id) {
        save();
        return super.removeSingleTask(id);
    }

    @Override
    public boolean removeSubTask(int id) {
        save();
        return super.removeSubTask(id);
    }

    @Override
    public boolean removeEpicTask(int id) {
        save();
        return super.removeEpicTask(id);
    }

    @Override
    public void clearAllSingleTasks() {
        save();
        super.clearAllSingleTasks();
    }

    @Override
    public void clearAllSubTasks() {
        save();
        super.clearAllSubTasks();
    }

    @Override
    public void clearAllEpicTasks() {
        save();
        super.clearAllEpicTasks();
    }

    @Override
    public boolean clearEpicSubTasks(int id) {
        save();
        return super.clearEpicSubTasks(id);
    }

    @Override
    public void clearEveryTasks() {
        save();
        super.clearEveryTasks();
    }

    private void save() {
        //TODO
    }

    private void restore() {
        //TODO
    }
}
