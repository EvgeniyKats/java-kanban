package manager.task;

import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;

import java.util.List;

public interface TaskManager {
    int addSingleTask(SingleTask singleTask);

    int addSubTask(SubTask subTask);

    int addEpicTask(EpicTask epicTask);

    SingleTask getSingleTask(int id);

    SubTask getSubTask(int id);

    EpicTask getEpicTask(int id);

    List<SingleTask> getAllSingleTasks();

    List<SubTask> getAllSubTasks();

    List<SubTask> getSubTasksFromEpic(int epicId);

    List<EpicTask> getAllEpicTasks();

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

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
