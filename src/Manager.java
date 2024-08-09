import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    private final Map<Integer, Task> allTasks = new HashMap<>();

    public Map<Integer, Task> getAllTasks() {
        return new HashMap<>(allTasks);
    }

    public void clearAllTasks() {
        allTasks.clear();
    }

    public void addTaskToMap(Task task) {
        if (task.getId() != null) {
            System.out.println("Ошибка при добавлении Task. id != null. " +
                    "Для обновления стоит использовать update. name=" + task.getName());
            return;
        }

        if (Task.class == task.getClass()) {
            addTask(task);
        } else if (EpicTask.class == task.getClass()) {
            addTask((EpicTask) task);
        } else {
            addTask((SubTask) task);
        }
    }

    public void updateTask(Task newTask, int id, Task oldTask) {
        if (allTasks.containsKey(id)
                && (allTasks.get(id).equals(oldTask))
                && (allTasks.get(id).getClass() == newTask.getClass())) {
            if (newTask.getClass() == EpicTask.class) {
                updateTask((EpicTask) newTask, id, (EpicTask) oldTask);
            } else if (newTask.getClass() == SubTask.class) {
                updateTask((SubTask) newTask, id, (SubTask) oldTask);
            } else {
                updateTask(newTask, id);
            }
        } else {
            System.out.println("Ошибка обновления. " +
                    "Task отсутствует в хранилище или задача не соотвествует обновляемой. name=" + newTask.getName());
        }
    }

    public void removeTask(int id) {
        if (allTasks.containsKey(id)) {
            Task task = allTasks.get(id);

            if (task.getClass() == Task.class) {
                allTasks.remove(id);
            } else if (task.getClass() == EpicTask.class) {
                EpicTask epicTask = (EpicTask) task;
                List<SubTask> subTasks = epicTask.getSubTasks();
                for (SubTask subTask : subTasks) {
                    allTasks.remove(subTask.getId());
                }
                allTasks.remove(id);
            } else {
                SubTask subTask = (SubTask) task;
                EpicTask epicTask = (EpicTask) allTasks.get(subTask.getEpicId());
                epicTask.removeSubTask(subTask);
                allTasks.remove(id);
            }
        } else {
            System.out.println("Ошибка при удалении. Указанный id=" + id + " отсутствует в хранилище.");
        }
    }

    public Task getTask(int id) {
        if (allTasks.containsKey(id)) {
            Task task = allTasks.get(id);
            if (task.getClass() == Task.class) {
                return task.getCopy();
            } else if (task.getClass() == SubTask.class) {
                SubTask subTask = (SubTask) task;
                return  subTask.getCopy();
            } else {
                EpicTask epicTask = (EpicTask) task;
                return epicTask.getCopy();
            }
        } else {
            System.out.println("Ошибка получения. В хранилище отсутствует указанный id" + id);
            return null;
        }
    }

    private void addTask(Task task) {
        Integer id = getIdForTask();
        Task result = new Task(task.getName(), task.getDescription(), task.getStatus(), id);
        allTasks.put(result.getId(), result);
    }

    private void addTask(SubTask subTask) {
        EpicTask epicTask = (EpicTask) allTasks.get(subTask.getEpicId());
        if (epicTask != null) {
            Integer id = getIdForTask();
            SubTask result = new SubTask(
                    subTask.getName(),
                    subTask.getDescription(),
                    subTask.getStatus(),
                    id,
                    subTask.getEpicId()
            );
            epicTask.addSubTask(result);
            allTasks.put(result.getId(), result);
        } else {
            System.out.println("Ошибка при добавлении. " +
                    "Epic данного SubTask отсутствует в хранилище. name=" + subTask.getName());
        }
    }

    private void addTask(EpicTask epicTask) {
        Integer id = getIdForTask();
        EpicTask resultEpic = new EpicTask(epicTask.getName(), epicTask.getDescription(), id, epicTask.getSubTasks());
        allTasks.put(resultEpic.getId(), resultEpic);

        if (!epicTask.getSubTasks().isEmpty()) {
            List<SubTask> subTasks = epicTask.getSubTasks();
            for (SubTask subTask : subTasks) {
                Integer idForSubTask = getIdForTask();
                SubTask resultSubTask = new SubTask(
                        subTask.getName(),
                        subTask.getDescription(),
                        subTask.getStatus(),
                        idForSubTask,
                        id
                );
                allTasks.put(resultSubTask.getId(), resultSubTask);
                resultEpic.updateSubTask(resultSubTask, subTask);
            }
        }
    }

    private void updateTask(Task task, int id) {
        allTasks.put(id, task);
    }

    private void updateTask(SubTask newSubTask, int id, SubTask oldSubTask) {
        SubTask result = new SubTask(
                newSubTask.getName(),
                newSubTask.getDescription(),
                newSubTask.getStatus(),
                id,
                newSubTask.getEpicId()
        );
        EpicTask epicTask = (EpicTask) allTasks.get(result.getEpicId());
        epicTask.updateSubTask(result, oldSubTask);
        allTasks.put(result.getId(), result);
    }

    private void updateTask(EpicTask epicTask, int id, EpicTask epicOld) {
        List<SubTask> oldSubTasks = epicOld.getSubTasks();
        List<SubTask> newSubTasks = epicTask.getSubTasks();

        for (SubTask newSubTask : newSubTasks) { // Идентификация и добавление СабТасков в Map, Epic
            if (newSubTask.getId() == null) {
                Integer idSub = getIdForTask();
                SubTask result = new SubTask(
                        newSubTask.getName(),
                        newSubTask.getDescription(),
                        newSubTask.getStatus(),
                        idSub,
                        id
                );
                epicTask.removeSubTask(newSubTask);
                epicTask.addSubTask(result);
                allTasks.put(result.getId(), result);
            }
        }

        for (SubTask oldSubTask : oldSubTasks) { // Удалить старые сабтаски из map, которых нет в новом List
            boolean newSubTasksContainsOldId = false;
            for (SubTask newSubTask : newSubTasks) {
                if (newSubTask.getId() != null && oldSubTask.getId().equals(newSubTask.getId())) {
                    newSubTasksContainsOldId = true;
                    break;
                }
            }
            if (!newSubTasksContainsOldId) {
                allTasks.remove(oldSubTask.getId());
            }
        }

        allTasks.put(id, epicTask);
    }

    private Integer getIdForTask() {
        Integer index = null;
        for (int i = 0; i < allTasks.size(); i++) {
            if (!allTasks.containsKey(i)) {
                index = i;
                break;
            }
        }

        if (index == null) {
            index = allTasks.size();
        }

        return index;
    }
}
