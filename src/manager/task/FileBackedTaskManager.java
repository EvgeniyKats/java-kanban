package manager.task;

import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final Path path;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    @Override
    public boolean addSingleTask(SingleTask singleTask) {
        boolean result = super.addSingleTask(singleTask);
        save();
        return result;
    }

    @Override
    public boolean addSubTask(SubTask subTask) {
        boolean result = super.addSubTask(subTask);
        save();
        return result;
    }

    @Override
    public boolean addEpicTask(EpicTask epicTask) {
        boolean result = super.addEpicTask(epicTask);
        save();
        return result;
    }

    @Override
    public boolean updateSingleTask(SingleTask singleTask) {
        boolean result = super.updateSingleTask(singleTask);
        save();
        return result;
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        boolean result = super.updateSubTask(subTask);
        save();
        return result;
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTask) {
        boolean result = super.updateEpicTask(epicTask);
        save();
        return result;
    }

    @Override
    public boolean removeSingleTask(int id) {
        boolean result = super.removeSingleTask(id);
        save();
        return result;
    }

    @Override
    public boolean removeSubTask(int id) {
        boolean result = super.removeSubTask(id);
        save();
        return result;
    }

    @Override
    public boolean removeEpicTask(int id) {
        boolean result = super.removeEpicTask(id);
        save();
        return result;
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
        boolean result = super.clearEpicSubTasks(id);
        save();
        return result;
    }

    @Override
    public void clearEveryTasks() {
        super.clearEveryTasks();
        save();
    }

    private void save() {
        try (Writer writer = new FileWriter(path.toString(), false); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            String title = "id,type,name,status,description,epic";
            bufferedWriter.write(title + "\n");

            for (SingleTask singleTask : getAllSingleTasks()) {
                bufferedWriter.write(singleTask.toString(singleTask) + "\n");
            }

            for (EpicTask epicTask : getAllEpicTasks()) {
                bufferedWriter.write(epicTask.toString(epicTask) + "\n");
            }

            for (SubTask subTask : getAllSubTasks()) {
                bufferedWriter.write(subTask.toString(subTask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private static void loadFromFile(Path path) {
        //TODO
    }

    static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(Throwable cause) {
            super(cause);
        }
    }
}
