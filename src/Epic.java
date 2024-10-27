import java.util.ArrayList;

public class Epic extends Task {
    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask, Status.NEW);
    }

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void setAllSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "nameTask='" + getNameTask() + '\'' +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", idTask=" + getIdTask() +
                ", status=" + getStatus() +
                '}';
    }

}
