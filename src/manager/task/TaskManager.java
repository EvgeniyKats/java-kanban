package manager.task;

import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    boolean addSingleTask(SingleTask singleTask);

    boolean addSubTask(SubTask subTask);

    boolean addEpicTask(EpicTask epicTask);

    SingleTask getSingleTask(int id);

    SubTask getSubTask(int id);

    EpicTask getEpicTask(int id);

    ArrayList<SingleTask> getAllSingleTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<SubTask> getSubTasksFromEpic(int epicId);

    ArrayList<EpicTask> getAllEpicTasks();

    boolean updateSingleTask(SingleTask singleTask);

    boolean updateSubTask(SubTask subTask);

    boolean updateEpicTask(EpicTask epicTask);

    boolean removeSingleTask(int id);

    boolean removeSubTask(int id);

    boolean removeEpicTask(int id);

    void clearAllSingleTasks();

    void clearAllSubTasks();

    void clearAllEpicTasks();

    boolean clearEpicSubTasks(int id);

    void clearEveryTasks();

    List<SingleTask> getHistory();
}
