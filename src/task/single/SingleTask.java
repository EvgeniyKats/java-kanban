package task.single;

import task.Status;

import java.util.Objects;

public class SingleTask {

    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;

    public SingleTask(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    protected SingleTask(SingleTask singleTask) {
        this.name = singleTask.name;
        this.description = singleTask.description;
        this.status = singleTask.status;
        this.id = singleTask.id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SingleTask getCopy() {
        return new SingleTask(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleTask singleTask = (SingleTask) o;
        return Objects.equals(id, singleTask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
