package HistoryManager;

import Task.Task;

import java.util.ArrayList;

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
}
