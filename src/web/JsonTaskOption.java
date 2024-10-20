package web;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import task.TaskType;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class JsonTaskOption {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .create();

    private JsonTaskOption() {
    }

    public static String listOfIntegersToJson(List<Integer> list) {
        return gson.toJson(list);
    }

    public static String taskToJson(Task task) {
        return gson.toJson(task);
    }

    public static String listOfTasksToJson(List<? extends Task> tasks) {
        return gson.toJson(tasks, new TaskTypeToken().getType());
    }

    public static List<Integer> getListOfIntegersFromJson(String json) {
        return gson.fromJson(json, new IntegerTypeToken().getType());
    }

    public static List<Task> getListOfTasksFromJson(String json) {
        return gson.fromJson(json, new TaskTypeToken().getType());
    }

    public static Optional<SingleTask> getSingleTaskFromJson(String json) {
        if (isStringNullOrEmpty(json)) {
            return Optional.empty();
        }
        try {
            SingleTask singleTask = gson.fromJson(json, SingleTask.class);
            if (singleTask.getTaskType().equals(TaskType.SINGLE_TASK)) {
                return Optional.of(singleTask);
            } else {
                return Optional.empty();
            }
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    public static Optional<SubTask> getSubTaskFromJson(String json) {
        if (isStringNullOrEmpty(json)) {
            return Optional.empty();
        }
        try {
            SubTask subTask = gson.fromJson(json, SubTask.class);
            if (subTask.getTaskType().equals(TaskType.SUB_TASK)) {
                return Optional.of(subTask);
            } else {
                return Optional.empty();
            }
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    public static Optional<EpicTask> getEpicTaskFromJson(String json) {
        if (isStringNullOrEmpty(json)) {
            return Optional.empty();
        }
        try {
            EpicTask epicTask = gson.fromJson(json, EpicTask.class);
            if (epicTask.getTaskType().equals(TaskType.EPIC_TASK)) {
                return Optional.of(epicTask);
            } else {
                return Optional.empty();
            }
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    private static boolean isStringNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if (localDateTime == null) {
                jsonWriter.value((String) null);
            } else {
                jsonWriter.value(formatter.format(localDateTime));
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {

            return LocalDateTime.parse(jsonReader.nextString(), formatter);

        }
    }

    static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            if (duration == null) {
                jsonWriter.value((String) null);
            } else {
                jsonWriter.value(duration.toMinutes());
            }
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
        }
    }

    static class TaskAdapter extends TypeAdapter<Task> {
        @Override
        public void write(JsonWriter jsonWriter, Task task) throws IOException {
            jsonWriter.value(gson.toJson(task));
        }

        @Override
        public Task read(JsonReader jsonReader) throws IOException {
            String json = jsonReader.nextString();
            JsonElement jsonElement = JsonParser.parseString(json);
            String taskType = jsonElement.getAsJsonObject().get("taskType").getAsString();
            if (taskType.equals(TaskType.SINGLE_TASK.name())) {
                return getSingleTaskFromJson(json).orElse(null);
            } else if (taskType.equals(TaskType.SUB_TASK.name())) {
                return getSubTaskFromJson(json).orElse(null);
            } else if (taskType.equals(TaskType.EPIC_TASK.name())) {
                return getEpicTaskFromJson(json).orElse(null);
            } else {
                return null;
            }
        }
    }

    static class TaskTypeToken extends TypeToken<List<Task>> {

    }

    static class IntegerTypeToken extends TypeToken<List<Integer>> {

    }
}
