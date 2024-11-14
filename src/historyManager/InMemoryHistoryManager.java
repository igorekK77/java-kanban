package historyManager;

import task.Task;

import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager{

    private ArrayList<Task> historySearchTask = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historySearchTask.size() == 10) {
            ArrayList<Task> temporaryHistorySearchTask = new ArrayList<>();
            for (Task task1: historySearchTask) {
                temporaryHistorySearchTask.add(task1);
            }
            historySearchTask = new ArrayList<>();
            for (int i = 1; i < temporaryHistorySearchTask.size(); i++) {
                historySearchTask.add(temporaryHistorySearchTask.get(i));
            }
            historySearchTask.add(task);
        } else {
            historySearchTask.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historySearchTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(historySearchTask, that.historySearchTask);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(historySearchTask);
    }
}
