package manager;

import task.*;


import java.util.List;

public interface TaskManager {
    public void setIdCounter(int idCounter);

    public int getIdCounter();

    List<Task> getAllTask();

    void removeAllTask();

    Task getTaskById(int id);

    Task createTask(Task newTask);

    void updateTask(Task updateTask);

    void deleteTaskById(int id);

    List<Subtask> getAllSubTask();

    void removeAllSubTask();

    Subtask getSubTaskById(int id);

    Subtask createSubTask(Subtask newSubTask);

    void updateSubTask(Subtask updateSubTask);

    void deleteSubTaskById(int id);

    List<Epic> getAllEpic();

    void removeAllEpic();

    Epic getEpicById(int id);

    Epic createEpic(Epic newEpic);

    List<Subtask> getEpicSubtask(int epicid);

    void updateEpic(Epic updateEpic);

    void deleteEpicById(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
