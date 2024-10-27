import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private int idCounter = 0;

    public ArrayList<Task> getAllTask() {
        ArrayList<Task> allTask = new ArrayList<>();
        for (Task task: tasks.values()) {
            allTask.add(task);
        }
        return allTask;
    }

    public void removeAllTask() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public int createTask(Task newTask) {
        tasks.put(idCounter, newTask);
        newTask.setIdTask(idCounter);
        idCounter++;
        return newTask.getIdTask();
    }

    public void updateTask(int id, Task updateTask) {
        tasks.put(id, updateTask);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }



    public ArrayList<Subtask> getAllSubTask() {
        ArrayList<Subtask> allSubTask = new ArrayList<>();
        for (Subtask subTask: subtasks.values()) {
            allSubTask.add(subTask);
        }
        return allSubTask;
    }

    public void removeAllSubTask() {
        ArrayList<Epic> subtaskEpicDelete = new ArrayList<>();
        for (Subtask subtask: subtasks.values()) {
            Epic epic = getEpicById(subtask.getEpicId());
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

    public Subtask getSubTaskById(int id) {
        return subtasks.get(id);
    }

    public int createSubTask(Subtask newSubTask) {
        subtasks.put(idCounter, newSubTask);
        newSubTask.setIdTask(idCounter);
        idCounter++;
        Epic epic = getEpicById(newSubTask.getEpicId());
        epic.setSubtaskIds(newSubTask.getIdTask());
        switch (newSubTask.getStatus()){
            case NEW:
                break;
            case IN_PROGRES:
                epic.setStatus(Status.IN_PROGRES);
                break;
        }
        return newSubTask.getIdTask();
    }

    public void updateSubTask(int id, Subtask updateSubTask) {
        subtasks.put(id, updateSubTask);
        updateEpicStatus(updateSubTask);
    }

    public void updateEpicStatus (Subtask updateSubTask) {
        Epic epic = getEpicById(updateSubTask.getEpicId());
        ArrayList<Subtask> checkSubtask = new ArrayList<>();
        for (Subtask subtask: subtasks.values()) {
            if (subtask.getEpicId() == epic.getIdTask()) {
                checkSubtask.add(subtask);
            }
        }

        int countDoneSubTask = 0;
        int countInProgressSubtask = 0;
        for (Subtask subtask: checkSubtask) {
            if (subtask.getStatus() == Status.DONE) {
                countDoneSubTask++;
            } else if (subtask.getStatus() == Status.IN_PROGRES) {
                countInProgressSubtask++;
            }
        }

        if (countDoneSubTask == checkSubtask.size()) {
            epic.setStatus(Status.DONE);
        } else if (countInProgressSubtask > 0) {
            epic.setStatus(Status.IN_PROGRES);
        }
    }

    public void deleteSubTaskById(int id) {
        Subtask subtask = getSubTaskById(id);
        Epic epic = getEpicById(subtask.getEpicId());
        ArrayList<Integer> subTaskIds = epic.getSubtaskIds();
        for (Integer idSubTask: subTaskIds) {
            if (idSubTask == subtask.getIdTask()) {
                subTaskIds.remove(idSubTask);
            }
        }
        epic.setAllSubtaskIds(subTaskIds);
        subtasks.remove(id);
        updateEpicStatus(subtask);
    }



    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> allEpic = new ArrayList<>();
        for (Epic epic: epics.values()) {
            allEpic.add(epic);
        }
        return allEpic;
    }

    public void removeAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public int createEpic(Epic newEpic) {
        epics.put(idCounter, newEpic);
        newEpic.setIdTask(idCounter);
        idCounter++;

        return newEpic.getIdTask();
    }

    public void updateEpic(int id, Epic updateEpic) {
        epics.put(id, updateEpic);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);

        for (Subtask subtask: subtasks.values()) {
            if (subtask.getEpicId() == id) {
                int idSubtask = subtask.getIdTask();
                subtasks.remove(idSubtask);
            }
        }


    }

    public ArrayList<Subtask> getAllSubtaskFromEpic(int epicId) {
        ArrayList<Subtask> allSubtaskFromEpic = new ArrayList<>();
        for (Subtask subtask: subtasks.values()){
            if (subtask.getEpicId() == epicId) {
                allSubtaskFromEpic.add(subtask);
            }
        }
        return allSubtaskFromEpic;
    }



}
