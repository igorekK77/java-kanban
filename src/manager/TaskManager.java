package manager;

import task.*;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getAllTask();

    void removeAllTask();

    Task getTaskById(int id);

    Task createTask(Task newTask);

    void updateTask(int id, Task updateTask);

    void deleteTaskById(int id);

    List<Subtask> getAllSubTask();

    void removeAllSubTask();

    Subtask getSubTaskById(int id);

    Subtask createSubTask(Subtask newSubTask);

    void updateSubTask(int id, Subtask updateSubTask);

    void deleteSubTaskById(int id);

    List<Epic> getAllEpic();

    void removeAllEpic();

    Epic getEpicById(int id);

    Epic createEpic(Epic newEpic);

    List<Subtask> getEpicSubtask(int epicid);

    void updateEpic(int id, Epic updateEpic);

    void deleteEpicById(int id);

    List<Task> getHistory();

}
