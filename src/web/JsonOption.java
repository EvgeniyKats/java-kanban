package web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import task.epic.EpicTask;
import task.epic.SubTask;
import task.single.SingleTask;
import task.single.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonOption {
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private JsonOption() {
    }

    public static String taskToJson(Task task) {
        return gson.toJson(task);
    }

    public static SingleTask getSingleTaskFromJson(String json) {
        return gson.fromJson(json, SingleTask.class);
    }

    public static SubTask getSubTaskFromJson(String json) {
        return gson.fromJson(json, SubTask.class);
    }

    public static EpicTask getEpicTaskFromJson(String json) {
        return gson.fromJson(json, EpicTask.class);
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
}
