package Manager;
import Task.*;

import Task.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getAllTask();

    void removeAllTask();

    Task getTaskById(int id);

    Task createTask(Task newTask);

    void updateTask(int id, Task updateTask);

    void deleteTaskById(int id);

    ArrayList<Subtask> getAllSubTask();

    void removeAllSubTask();

    Subtask getSubTaskById(int id);

    Subtask createSubTask(Subtask newSubTask);

    void updateSubTask(int id, Subtask updateSubTask);

    void deleteSubTaskById(int id);

    ArrayList<Epic> getAllEpic();

    void removeAllEpic();

    Epic getEpicById(int id);

    Epic createEpic(Epic newEpic);

    void updateEpic(int id, Epic updateEpic);

    void deleteEpicById(int id);
}
