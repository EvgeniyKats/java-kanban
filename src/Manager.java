import task.Status;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    private final Map<Integer, SingleTask> allSingleTasks = new HashMap<>();
    private final Map<Integer, SubTask> allSubTasks = new HashMap<>();
    private final Map<Integer, EpicTask> allEpicTasks = new HashMap<>();
    private int idNext = 1;

    public boolean addSingleTask(SingleTask singleTask) {
        if ((singleTask.getClass() == SingleTask.class) && !allSingleTasks.containsValue(singleTask)) {
            setTaskId(singleTask);
            allSingleTasks.put(singleTask.getId(), singleTask);
            return true;
        } else {
            return false;
        }
    }

    public boolean addSubTask(SubTask subTask) {
        if ((subTask.getClass() == SubTask.class) && !allSubTasks.containsValue(subTask)
                && allEpicTasks.containsKey(subTask.getEpicId())) {
            setTaskId(subTask);
            allSubTasks.put(subTask.getId(), subTask);
            EpicTask epicTaskOfSubTask = allEpicTasks.get(subTask.getEpicId());
            epicTaskOfSubTask.addSubTaskId(subTask.getId());
            updateStatusEpic(epicTaskOfSubTask);
            return true;
        } else {
            return false;
        }
    }

    public boolean addEpicTask(EpicTask epicTask) {
        if ((epicTask.getClass() == EpicTask.class) && !allEpicTasks.containsValue(epicTask)) {
            setTaskId(epicTask);
            allEpicTasks.put(epicTask.getId(), epicTask);
            return true;
        } else {
            return false;
        }
    }

    public SingleTask getSingleTask(int id) {
        if (allSingleTasks.containsKey(id)) {
            return allSingleTasks.get(id).getCopy();
        } else {
            return null;
        }
    }

    public SubTask getSubTask(int id) {
        if (allSubTasks.containsKey(id)) {
            return allSubTasks.get(id).getCopy();
        } else {
            return null;
        }
    }

    public EpicTask getEpicTask(int id) {
        if (allEpicTasks.containsKey(id)) {
            return allEpicTasks.get(id).getCopy();
        } else {
            return null;
        }
    }

    public ArrayList<SingleTask> getAllSingleTasks() {
        ArrayList<SingleTask> result = new ArrayList<>();

        for (SingleTask singleTask : allSingleTasks.values()) {
            result.add(singleTask.getCopy());
        }

        return result;
    }

    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> result = new ArrayList<>();

        for (SubTask subTask : allSubTasks.values()) {
            result.add(subTask.getCopy());
        }

        return result;
    }

    public ArrayList<SubTask> getSubTasksFromEpic(int epicId) {
        if (allEpicTasks.containsKey(epicId)) {
            ArrayList<SubTask> result = new ArrayList<>();
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

    public ArrayList<EpicTask> getAllEpicTasks() {
        ArrayList<EpicTask> result = new ArrayList<>();

        for (EpicTask epicTask : allEpicTasks.values()) {
            result.add(epicTask.getCopy());
        }

        return result;
    }

    public boolean updateSingleTask(SingleTask singleTask) {
        if ((singleTask.getClass() == SingleTask.class) && allSingleTasks.containsKey(singleTask.getId())) {
            allSingleTasks.put(singleTask.getId(), singleTask);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateSubTask(SubTask subTask) {
        if ((subTask.getClass() == SubTask.class) && allSubTasks.containsKey(subTask.getId())) {
            allSubTasks.put(subTask.getId(), subTask);
            updateStatusEpic(allEpicTasks.get(subTask.getEpicId()));
            return true;
        } else {
            return false;
        }
    }

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

    public boolean removeSingleTask(int id) {
        if (allSingleTasks.containsKey(id)) {
            allSingleTasks.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeSubTask(int id) {
        if (allSubTasks.containsKey(id)) {
            SubTask subTask = allSubTasks.remove(id);
            EpicTask epicTask = allEpicTasks.get(subTask.getEpicId());
            epicTask.removeSubTaskId(subTask.getId());
            updateStatusEpic(epicTask);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeEpicTask(int id) {
        if (allEpicTasks.containsKey(id)) {
            EpicTask epicTask = allEpicTasks.remove(id);
            List<Integer> subTasksId = epicTask.getSubTasksId();

            for (Integer subTaskId : subTasksId) {
                allSubTasks.remove(subTaskId);
            }

            return true;
        } else {
            return false;
        }
    }

    public void clearAllSingleTasks() {
        allSingleTasks.clear();
    }

    public void clearAllSubTasks() {
        for (EpicTask epicTask : allEpicTasks.values()) {
            epicTask.clearSubTasksId();
            updateStatusEpic(epicTask);
        }

        allSubTasks.clear();
    }

    public void clearAllEpicTasks() {
        allSubTasks.clear();
        allEpicTasks.clear();
    }

    public boolean clearEpicSubTasks(int id) {
        if (allEpicTasks.containsKey(id)) {
            EpicTask epicTask = allEpicTasks.get(id);
            List<Integer> subTasksId = epicTask.getSubTasksId();

            for (Integer subTaskId : subTasksId) {
                allSubTasks.remove(subTaskId);
            }

            epicTask.clearSubTasksId();
            updateStatusEpic(epicTask);
            return true;
        } else {
            return false;
        }
    }

    public void clearEveryTasks() {
        allSingleTasks.clear();
        allSubTasks.clear();
        allEpicTasks.clear();
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

    private void setTaskId(SingleTask task) {
        task.setId(idNext);
        idNext++;
    }
}
