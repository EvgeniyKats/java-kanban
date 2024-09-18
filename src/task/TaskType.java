package task;

import task.single.Task;

public enum TaskType {
    SINGLE_TASK,
    SUB_TASK,
    EPIC_TASK;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String[] names = name().split("_");
        for (String name : names) {
            builder.append(name.charAt(0)).append(name.substring(1).toLowerCase());
        }
        return builder.toString();
    }
}
