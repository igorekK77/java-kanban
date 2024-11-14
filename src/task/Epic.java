package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask, Status.NEW);
    }


    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int subtaskId) {
        if (subtaskId != super.getIdTask()){
            subtaskIds.add(subtaskId);
        }
    }

    public void setAllSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public String toString() {
        return "Task.Epic{" +
                "nameTask='" + getNameTask() + '\'' +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", idTask=" + getIdTask() +
                ", status=" + getStatus() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }
}
