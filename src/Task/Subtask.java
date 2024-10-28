package Task;

public class Subtask extends Task {
    private int epicId = 0;

    public Subtask(String nameTask, String descriptionTask, Status status, int epicId){
        super(nameTask, descriptionTask, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Task.Subtask{" +
                "nameTask='" + getNameTask() + '\'' +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", idTask=" + getIdTask() +
                ", status=" + getStatus() +
                ", epicId=" + getEpicId() +
                '}';
    }
}
