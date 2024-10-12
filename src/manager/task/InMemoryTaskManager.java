package manager.task;

import manager.Managers;
import manager.history.HistoryManager;
import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public static int DEFAULT_ID_NEXT = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TaskPriorityManager taskPriorityManager = Managers.getTaskPriorityManager();
    protected final Map<Integer, SingleTask> allSingleTasks = new HashMap<>();
    protected final Map<Integer, SubTask> allSubTasks = new HashMap<>();
    protected final Map<Integer, EpicTask> allEpicTasks = new HashMap<>();
    protected int idNext = DEFAULT_ID_NEXT;

    @Override
    public int addSingleTask(SingleTask singleTask) {
        if ((singleTask.getClass() == SingleTask.class) && !allSingleTasks.containsValue(singleTask)) {
            if (singleTask.getStartTime() != null) {
                if (!taskPriorityManager.add(singleTask)) {
                    return -2;
                }
            }
            if (singleTask.getId() == null) {
                setTaskId(singleTask);
            }
            allSingleTasks.put(singleTask.getId(), singleTask);
            return singleTask.getId();
        } else {
            return -1;
        }
    }

    @Override
    public int addSubTask(SubTask subTask) {
        if ((subTask.getClass() == SubTask.class) && !allSubTasks.containsValue(subTask)
                && allEpicTasks.containsKey(subTask.getEpicId())) {
            if (subTask.getStartTime() != null) {
                if (!taskPriorityManager.add(subTask)) {
                    return -2;
                }
            }
            if (subTask.getId() == null) {
                setTaskId(subTask);
            }
            allSubTasks.put(subTask.getId(), subTask);
            EpicTask epicTaskOfSubTask = allEpicTasks.get(subTask.getEpicId());
            epicTaskOfSubTask.addSubTaskId(subTask.getId());
            updateStatusEpic(epicTaskOfSubTask);
            return subTask.getId();
        } else {
            return -1;
        }
    }

    @Override
    public int addEpicTask(EpicTask epicTask) {
        if ((epicTask.getClass() == EpicTask.class) && !allEpicTasks.containsValue(epicTask)) {
            if (epicTask.getId() == null) {
                setTaskId(epicTask);
            }
            allEpicTasks.put(epicTask.getId(), epicTask);
            return epicTask.getId();
        } else {
            return -1;
        }
    }

    @Override
    public SingleTask getSingleTask(int id) {
        if (allSingleTasks.containsKey(id)) {
            historyManager.add(allSingleTasks.get(id).getCopy());
            return allSingleTasks.get(id).getCopy();
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        if (allSubTasks.containsKey(id)) {
            historyManager.add(allSubTasks.get(id).getCopy());
            return allSubTasks.get(id).getCopy();
        } else {
            return null;
        }
    }

    @Override
    public EpicTask getEpicTask(int id) {
        if (allEpicTasks.containsKey(id)) {
            historyManager.add(allEpicTasks.get(id).getCopy());
            return allEpicTasks.get(id).getCopy();
        } else {
            return null;
        }
    }

    @Override
    public List<SingleTask> getAllSingleTasks() {
        List<SingleTask> result = new ArrayList<>();

        for (SingleTask singleTask : allSingleTasks.values()) {
            result.add(singleTask.getCopy());
        }

        return result;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        List<SubTask> result = new ArrayList<>();

        for (SubTask subTask : allSubTasks.values()) {
            result.add(subTask.getCopy());
        }

        return result;
    }

    @Override
    public List<SubTask> getSubTasksFromEpic(int epicId) {
        if (allEpicTasks.containsKey(epicId)) {
            List<SubTask> result = new ArrayList<>();
            EpicTask epicTask = allEpicTasks.get(epicId);
            List<Integer> subTasksId = epicTask.getSubTasksId();

            for (Integer subTaskId : subTasksId) {
                SubTask subTask = allSubTasks.get(subTaskId);
                result.add(subTask.getCopy());
            }

            return result;
        } else {
            return null;
        }
    }

    @Override
    public List<EpicTask> getAllEpicTasks() {
        List<EpicTask> result = new ArrayList<>();

        for (EpicTask epicTask : allEpicTasks.values()) {
            result.add(epicTask.getCopy());
        }

        return result;
    }

    @Override
    public boolean updateSingleTask(SingleTask singleTask) {
        if ((singleTask.getClass() == SingleTask.class) && allSingleTasks.containsKey(singleTask.getId())) {
            allSingleTasks.put(singleTask.getId(), singleTask);
            taskPriorityManager.updateTask(singleTask);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        if ((subTask.getClass() == SubTask.class) && allSubTasks.containsKey(subTask.getId())) {
            allSubTasks.put(subTask.getId(), subTask);
            taskPriorityManager.updateTask(subTask);
            updateStatusEpic(allEpicTasks.get(subTask.getEpicId()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTask) {
        if ((epicTask.getClass() == EpicTask.class) && allEpicTasks.containsKey(epicTask.getId())) {
            EpicTask epicTaskFromMap = allEpicTasks.get(epicTask.getId());
            epicTaskFromMap.setName(epicTask.getName());
            epicTaskFromMap.setDescription(epicTask.getDescription());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeSingleTask(int id) {
        if (allSingleTasks.containsKey(id)) {
            taskPriorityManager.remove(allSingleTasks.get(id));
            allSingleTasks.remove(id);
            historyManager.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeSubTask(int id) {
        if (allSubTasks.containsKey(id)) {
            SubTask subTask = allSubTasks.remove(id);
            taskPriorityManager.remove(subTask);
            historyManager.remove(id);
            EpicTask epicTask = allEpicTasks.get(subTask.getEpicId());
            epicTask.removeSubTaskId(subTask.getId());
            updateStatusEpic(epicTask);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeEpicTask(int id) {
        if (allEpicTasks.containsKey(id)) {
            EpicTask epicTask = allEpicTasks.remove(id);
            historyManager.remove(id);
            List<Integer> subTasksId = epicTask.getSubTasksId();

            for (Integer subTaskId : subTasksId) {
                SubTask subTask = allSubTasks.remove(subTaskId);
                taskPriorityManager.remove(subTask);
                historyManager.remove(subTaskId);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clearAllSingleTasks() {
        for (SingleTask singleTask : allSingleTasks.values()) {
            taskPriorityManager.remove(singleTask);
            historyManager.remove(singleTask.getId());
        }
        allSingleTasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        for (SubTask subTask : allSubTasks.values()) {
            taskPriorityManager.remove(subTask);
            historyManager.remove(subTask.getId());
        }
        for (EpicTask epicTask : allEpicTasks.values()) {
            epicTask.clearSubTasksId();
            updateStatusEpic(epicTask);
        }
        allSubTasks.clear();
    }

    @Override
    public void clearAllEpicTasks() {
        for (EpicTask epicTask : allEpicTasks.values()) {
            historyManager.remove(epicTask.getId());
        }
        for (SubTask subTask : allSubTasks.values()) {
            taskPriorityManager.remove(subTask);
            historyManager.remove(subTask.getId());
        }
        allSubTasks.clear();
        allEpicTasks.clear();
    }

    @Override
    public boolean clearEpicSubTasks(int id) {
        if (allEpicTasks.containsKey(id)) {
            EpicTask epicTask = allEpicTasks.get(id);
            List<Integer> subTasksId = epicTask.getSubTasksId();

            for (Integer subTaskId : subTasksId) {
                SubTask subTask = allSubTasks.remove(subTaskId);
                taskPriorityManager.remove(subTask);
                historyManager.remove(subTaskId);
            }

            epicTask.clearSubTasksId();
            updateStatusEpic(epicTask);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clearEveryTasks() {
        taskPriorityManager.clearAll();
        historyManager.clearHistory();
        allSingleTasks.clear();
        allSubTasks.clear();
        allEpicTasks.clear();
        idNext = DEFAULT_ID_NEXT;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateStatusEpic(EpicTask epicTask) {
        List<Integer> subTasksId = epicTask.getSubTasksId();

        if (subTasksId.isEmpty()) {
            epicTask.setStatus(Status.NEW);
            return;
        }

        for (Integer subTaskId : subTasksId) {
            if (Status.IN_PROGRESS.equals(allSubTasks.get(subTaskId).getStatus())) {
                epicTask.setStatus(Status.IN_PROGRESS);
                return;
            }
        }

        boolean haveNew = false;
        boolean haveDone = false;

        for (Integer subTaskId : subTasksId) {
            if (haveDone && haveNew) {
                break;
            }

            if (!haveNew && Status.NEW.equals(allSubTasks.get(subTaskId).getStatus())) {
                haveNew = true;
                continue;
            }

            if (!haveDone && Status.DONE.equals(allSubTasks.get(subTaskId).getStatus())) {
                haveDone = true;
            }
        }

        if (haveDone && haveNew) {
            epicTask.setStatus(Status.IN_PROGRESS);
        } else if (haveNew) {
            epicTask.setStatus(Status.NEW);
        } else {
            epicTask.setStatus(Status.DONE);
        }
    }

    private void updateEndTimeEpic(EpicTask epicTask) {
        //TODO
    }

    private void setTaskId(SingleTask task) {
        task.setId(idNext);
        idNext++;
    }
}
