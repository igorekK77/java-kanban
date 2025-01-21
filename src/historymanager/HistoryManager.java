package historymanager;

import task.*;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    public void add(Task task);

    public List<Task> getHistory();

    public void removeNode(Node<Task> taskNode);


    public void remove(int id);

}
