public class Epic extends Task {
    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask, Status.NEW);
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
