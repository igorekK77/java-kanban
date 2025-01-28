package task;

import manager.TaskTypes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Subtask> subtasksEpic = new ArrayList<>();
    private LocalDateTime endTime;
    private Duration duration = Duration.ofMinutes(0);
    private LocalDateTime startTime = LocalDateTime.MIN;

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask, Status.NEW);
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    public List<Subtask> getSubtasksEpic() {
        return subtasksEpic;
    }


    public void setSubtaskEpic(Subtask subtask) {
        if (subtask.getIdTask() != super.getIdTask()) {
            subtasksEpic.add(subtask);
        }
        duration = Duration.ofMinutes(subtask.duration.toMinutes() + duration.toMinutes());
        searchStartTime();
    }

    private void searchStartTime() {
        LocalDateTime minTime = getEndTime();
        for (Subtask subtask: subtasksEpic) {
            if (subtask.getStartTime().isBefore(minTime)) {
                minTime = subtask.getStartTime();
            }
        }
        startTime = minTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasksEpic.size() == 0) {
            endTime = startTime;
        } else {
            endTime = subtasksEpic.getFirst().getStartTime().plus(subtasksEpic.getFirst().getDuration());
        }
        for (Subtask subtask: subtasksEpic) {
            if (subtask.getStartTime().plus(duration).isAfter(endTime)) {
                endTime = subtask.getStartTime().plus(duration);
            }
        }
        return endTime;
    }

    public void setAllSubtasks(List<Subtask> newSubtasksEpic) {
        duration = Duration.ofMinutes(0);
        this.subtasksEpic = newSubtasksEpic;
        searchStartTime();
        newSubtasksEpic.stream()
                .peek(subtask -> duration = Duration.ofMinutes(subtask.duration.toMinutes() + duration.toMinutes()))
                .findFirst();
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s", getIdTask(), TaskTypes.EPIC, getNameTask(),
                getStatus(), getDescriptionTask(), duration.toMinutes(), startTime.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksEpic, epic.subtasksEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksEpic);
    }
}
