package manager.task;

import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.TaskType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final Path path;

    private FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager manager = new FileBackedTaskManager(path);

        try {
            String stringOfTasks = Files.readString(path);
            String[] tasksArray = stringOfTasks.split("\n");

            final int idLocation = 0;
            final int typeLocation = 1;
            final int nameLocation = 2;
            final int statusLocation = 3;
            final int descriptionLocation = 4;
            final int epicIdLocation = 5;

            for (int i = 1; i < tasksArray.length; i++) {
                String[] taskFields = tasksArray[i].split(",");
                SingleTask task;
                String typeS = taskFields[typeLocation];

                if (typeS.equals(TaskType.SINGLE_TASK.toString())) {
                    task = new SingleTask(taskFields[nameLocation], taskFields[descriptionLocation]);
                } else if (typeS.equals(TaskType.EPIC_TASK.toString())) {
                    task = new EpicTask(taskFields[nameLocation], taskFields[descriptionLocation]);
                } else {
                    task = new SubTask(taskFields[nameLocation], taskFields[descriptionLocation],
                            Integer.parseInt(taskFields[epicIdLocation]));
                }
                task.setId(Integer.parseInt(taskFields[idLocation]));
                task.setName(taskFields[nameLocation]);
                task.setDescription(taskFields[descriptionLocation]);

                Status status;
                String statusS = taskFields[statusLocation];

                if (statusS.equals(Status.NEW.toString())) {
                    status = Status.NEW;
                } else if (statusS.equals(Status.IN_PROGRESS.toString())) {
                    status = Status.IN_PROGRESS;
                } else {
                    status = Status.DONE;
                }
                task.setStatus(status);

                switch (task.getTaskType()) {
                    case SINGLE_TASK -> manager.addSingleTask(task);
                    case EPIC_TASK -> manager.addEpicTask((EpicTask) task);
                    case SUB_TASK -> manager.addSubTask((SubTask) task);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return manager;
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

    static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(Throwable cause) {
            super(cause);
        }
    }
}
