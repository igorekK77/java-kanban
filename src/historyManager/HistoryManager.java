package historyManager;

import task.*;

import java.util.ArrayList;

public interface HistoryManager {
    public void add(Task task);

    public ArrayList<Task> getHistory();
}
