package Manager;
import Task.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private ArrayList<Task> historySearchTask = new ArrayList<>();

    private int idCounter = 0;

    @Override
    public ArrayList<Task> getAllTask() {
        ArrayList<Task> allTask = new ArrayList<>();
        for (Task task: tasks.values()) {
            allTask.add(task);
        }
        return allTask;
    }

    @Override
    public void removeAllTask() {
        tasks.clear();
    }


    @Override
    public Task getTaskById(int id) {
        if (historySearchTask.size() == 10) {
            ArrayList<Task> temporaryHistorySearchTask = new ArrayList<>();
            for (Task task: historySearchTask) {
                temporaryHistorySearchTask.add(task);
            }
            historySearchTask = new ArrayList<>();
            for (int i = 1; i < temporaryHistorySearchTask.size(); i++) {
                historySearchTask.add(temporaryHistorySearchTask.get(i));
            }
            historySearchTask.add(tasks.get(id));
        } else {
            historySearchTask.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Task createTask(Task newTask) {
        tasks.put(idCounter, newTask);
        newTask.setIdTask(idCounter);
        idCounter++;
        return newTask;
    }

    @Override
    public void updateTask(int id, Task updateTask) {
        tasks.put(id, updateTask);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }



    @Override
    public ArrayList<Subtask> getAllSubTask() {
        ArrayList<Subtask> allSubTask = new ArrayList<>();
        for (Subtask subTask: subtasks.values()) {
            allSubTask.add(subTask);
        }
        return allSubTask;
    }

    @Override
    public void removeAllSubTask() {
        for (Subtask subtask: subtasks.values()) {
            Epic epic = epicSearchByIdInsideTheClass(subtask.getEpicId());
            ArrayList<Integer> subTaskIds = epic.getSubtaskIds();
            for (Integer integer: subTaskIds) {
                if (integer == subtask.getIdTask()) {
                    subTaskIds.remove(integer);
                }
            }
            epic.setAllSubtaskIds(subTaskIds);
        }
        subtasks.clear();
    }

    @Override
    public Subtask getSubTaskById(int id) {
        if (historySearchTask.size() == 10) {
            ArrayList<Task> temporaryHistorySearchTask = new ArrayList<>();
            for (Task task: historySearchTask) {
                temporaryHistorySearchTask.add(task);
            }
            historySearchTask = new ArrayList<>();
            for (int i = 1; i < temporaryHistorySearchTask.size(); i++) {
                historySearchTask.add(temporaryHistorySearchTask.get(i));
            }
            historySearchTask.add(subtasks.get(id));
        } else {
            historySearchTask.add(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    @Override
    public Subtask createSubTask(Subtask newSubTask) {
        subtasks.put(idCounter, newSubTask);
        newSubTask.setIdTask(idCounter);
        idCounter++;
        Epic epic = epicSearchByIdInsideTheClass(newSubTask.getEpicId());
        epic.setSubtaskIds(newSubTask.getIdTask());
        switch (newSubTask.getStatus()){
            case NEW:
                break;
            case IN_PROGRESS:
                epic.setStatus(Status.IN_PROGRESS);
                break;
        }
        return newSubTask;
    }

    @Override
    public void updateSubTask(int id, Subtask updateSubTask) {
        subtasks.put(id, updateSubTask);
        updateEpicStatus(updateSubTask);
    }

    private void updateEpicStatus (Subtask updateSubTask) {
        Epic epic = epicSearchByIdInsideTheClass(updateSubTask.getEpicId());
        ArrayList<Subtask> checkSubtask = new ArrayList<>();
        for (Subtask subtask: subtasks.values()) {
            if (subtask.getEpicId() == epic.getIdTask()) {
                checkSubtask.add(subtask);
            }
        }

        int countDoneSubTask = 0;
        int countInProgressSubtask = 0;
        int countNewTask = 0;
        for (Subtask subtask: checkSubtask) {
            if (subtask.getStatus() == Status.DONE) {
                countDoneSubTask++;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                countInProgressSubtask++;
            } else if (subtask.getStatus() == Status.NEW) {
                countNewTask++;
            }
        }

        if (countDoneSubTask == checkSubtask.size()) {
            epic.setStatus(Status.DONE);
        } else if (countInProgressSubtask > 0 || countDoneSubTask > 0 && countNewTask > 0) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private Subtask subTaskSearchByIdInsideTheClass(int id) {
        return subtasks.get(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        Subtask subtask = subTaskSearchByIdInsideTheClass(id);
        Epic epic = epicSearchByIdInsideTheClass(subtask.getEpicId());
        ArrayList<Integer> subTaskIds = epic.getSubtaskIds();
        for (Integer idSubTask: subTaskIds) {
            if (idSubTask == subtask.getIdTask()) {
                subTaskIds.remove(idSubTask);
                break;
            }
        }
        epic.setAllSubtaskIds(subTaskIds);
        subtasks.remove(id);
        updateEpicStatus(subtask);
    }



    @Override
    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> allEpic = new ArrayList<>();
        for (Epic epic: epics.values()) {
            allEpic.add(epic);
        }
        return allEpic;
    }

    @Override
    public void removeAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        if (historySearchTask.size() == 10) {
            ArrayList<Task> temporaryHistorySearchTask = new ArrayList<>();
            for (Task task: historySearchTask) {
                temporaryHistorySearchTask.add(task);
            }
            historySearchTask = new ArrayList<>();
            for (int i = 1; i < temporaryHistorySearchTask.size(); i++) {
                historySearchTask.add(temporaryHistorySearchTask.get(i));
            }
            historySearchTask.add(epics.get(id));
        } else {
            historySearchTask.add(epics.get(id));
        }
        return epics.get(id);
    }

    private Epic epicSearchByIdInsideTheClass(int id) {
        return epics.get(id);
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        epics.put(idCounter, newEpic);
        newEpic.setIdTask(idCounter);
        idCounter++;

        return newEpic;
    }

    @Override
    public void updateEpic(int id, Epic updateEpic) {
        epics.put(id, updateEpic);
    }

    @Override
    public void deleteEpicById(int id) {
        epics.remove(id);

        for (Subtask subtask: subtasks.values()) {
            if (subtask.getEpicId() == id) {
                int idSubtask = subtask.getIdTask();
                subtasks.remove(idSubtask);
            }
        }


    }

    public ArrayList<Task> getHistory() {
        return historySearchTask;
    }




}
