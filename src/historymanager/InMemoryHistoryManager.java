package historymanager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager{

    private HashMap<Integer, Node<Task>> historySearchTask = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    private void linkLast(Task task) {
        if (task != null) {
            final Node<Task> oldTail = tail;
            final Node<Task> newTail = new Node<>(oldTail, task, null);
            tail = newTail;
            if (oldTail == null) {
                head = newTail;
            } else {
                oldTail.next = newTail;
            }
            size++;
            historySearchTask.put(task.getIdTask(), newTail);
            if (newTail.prev != null) {
                Node<Task> correctNode = historySearchTask.get(newTail.prev.data.getIdTask());
                if (correctNode != null) {
                    correctNode.next = newTail;
                    historySearchTask.put(newTail.prev.data.getIdTask(), correctNode);
                }
            }
        }
    }

    @Override
    public void add(Task task) {
        if (task != null && historySearchTask.containsKey(task.getIdTask())) {
            remove(task.getIdTask());
            linkLast(task);
        } else {
            linkLast(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> taskNode = historySearchTask.get(id);
        if (taskNode != null) {
            removeNode(taskNode);
            historySearchTask.remove(id);
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Node<Task> taskNode: historySearchTask.values()) {
            listTask.add(taskNode.data);
        }
        return listTask;
    }

    public void removeNode(Node<Task> taskNode) {
        Node<Task> predTask = taskNode.prev;
        Node<Task> nextTask = taskNode.next;
        if (predTask != null) {
            if (predTask.next != null) {
                predTask.next = nextTask;
            } else {
                predTask.next = null;
            }
        }
        if (nextTask != null) {
            if (nextTask.prev != null) {
                nextTask.prev = predTask;
            } else {
                nextTask.prev = null;
            }
        }

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
