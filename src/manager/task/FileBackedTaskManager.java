package manager.task;

import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.TaskType;
import task.single.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path path;

    private FileBackedTaskManager(Path path, boolean needsToRestoreTasks) {
        this.path = path;
        if (needsToRestoreTasks) {
            restoreTasks();
        }
    }

    public static FileBackedTaskManager loadFromFile(Path path, boolean needsToRestoreTasks) {
        return new FileBackedTaskManager(path, needsToRestoreTasks);
    }

    @Override
    public int addSingleTask(SingleTask singleTask) {
        int result = super.addSingleTask(singleTask);
        save();
        return result;
    }

    @Override
    public int addSubTask(SubTask subTask) {
        int result = super.addSubTask(subTask);
        save();
        return result;
    }

    @Override
    public int addEpicTask(EpicTask epicTask) {
        int result = super.addEpicTask(epicTask);
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
        super.clearAllSingleTasks();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    @Override
    public void clearAllEpicTasks() {
        super.clearAllEpicTasks();
        save();
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

    private void restoreTasks() {
        try {
            String stringOfTasks = Files.readString(path);
            String[] tasksArray = stringOfTasks.split("\n");

            int maxId = 0;

            for (int i = 1; i < tasksArray.length; i++) {
                Task task = fromString(tasksArray[i]);
                if (maxId < task.getId()) {
                    maxId = task.getId();
                }

                switch (task.getTaskType()) {
                    case SINGLE_TASK -> {
                        SingleTask singleTask = (SingleTask) task;
                        allSingleTasks.put(singleTask.getId(), singleTask);
                    }
                    case EPIC_TASK -> {
                        EpicTask epicTask = (EpicTask) task;
                        allEpicTasks.put(epicTask.getId(), epicTask);
                        allSubTasks.values().stream()
                                .filter(subTask -> subTask.getId().equals(epicTask.getId()))
                                .forEach(subTask -> epicTask.addSubTaskId(subTask.getId()));
                    }
                    case SUB_TASK -> {
                        SubTask subTask = (SubTask) task;
                        allSubTasks.put(subTask.getId(), subTask);
                        EpicTask epicTask = allEpicTasks.get(subTask.getEpicId());
                        if (epicTask != null) {
                            epicTask.addSubTaskId(subTask.getId());
                        }
                    }
                }
            }

            idNext = maxId + 1;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Task fromString(String value) {
        final int idLocation = 0;
        final int typeLocation = 1;
        final int nameLocation = 2;
        final int statusLocation = 3;
        final int descriptionLocation = 4;
        final int epicIdLocation = 5;
        final int startTimeLocation = 6;
        final int durationLocation = 7;
        final int endTimeLocation = 8;

        String[] taskFields = value.split(",", -1);
        Task task;
        String typeS = taskFields[typeLocation];

        if (typeS.equals(TaskType.SINGLE_TASK.toString())) {
            task = new SingleTask(taskFields[nameLocation], taskFields[descriptionLocation]);
        } else if (typeS.equals(TaskType.EPIC_TASK.toString())) {
            EpicTask epicTask = new EpicTask(taskFields[nameLocation], taskFields[descriptionLocation]);
            if (!taskFields[endTimeLocation].equals("null")) {
                epicTask.setEndTime(LocalDateTime.parse(taskFields[endTimeLocation]));
            }
            task = epicTask;
        } else {
            task = new SubTask(taskFields[nameLocation], taskFields[descriptionLocation],
                    Integer.parseInt(taskFields[epicIdLocation]));
        }
        task.setId(Integer.parseInt(taskFields[idLocation]));
        task.setName(taskFields[nameLocation]);
        task.setDescription(taskFields[descriptionLocation]);
        if (!taskFields[startTimeLocation].equals("null")) {
            task.setStartTime(LocalDateTime.parse(taskFields[startTimeLocation]));
        }
        if (!taskFields[durationLocation].equals("null")) {
            task.setDuration(Duration.ofMinutes(Long.parseLong(taskFields[durationLocation])));
        }

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

        return task;
    }

    private void save() {
        try (Writer writer = new FileWriter(path.toString(), false); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            String title = "id,type,name,status,description,epic,startTime,duration,endTime";
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
}
