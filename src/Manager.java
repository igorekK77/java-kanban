import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private int AllTypeTaskId = 0;

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

    public Task getTaskOnId(int id) {
        return tasks.get(id);
    }

    public int createTask(Task newTask) {
        tasks.put(AllTypeTaskId, newTask);
        newTask.setIdTask(AllTypeTaskId);
        AllTypeTaskId++;
        return newTask.getIdTask();
    }

    public void updateTask(int id, Task updateTask) {
        tasks.put(id, updateTask);
    }

    public void deleteTaskOnId(int id) {
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
        subtasks.clear();
    }

    public Task getSubTaskOnId(int id) {
        return subtasks.get(id);
    }

    public int createSubTask(Subtask newSubTask) {
        subtasks.put(AllTypeTaskId, newSubTask);
        newSubTask.setIdTask(AllTypeTaskId);
        AllTypeTaskId++;
        Epic epic = getEpicOnId(newSubTask.getEpicId());
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

        Epic epic = getEpicOnId(updateSubTask.getEpicId());
        ArrayList<Subtask> checkSubtask = new ArrayList<>();
        for (Subtask subtask: subtasks.values()) {
            if (subtask.getEpicId() == epic.getIdTask()) {
                checkSubtask.add(subtask);
            }
        }

        int countDoneSubTask = 0;
        int countInProgressTask = 0;
        for (Subtask subtask: checkSubtask) {
            if (subtask.getStatus() == Status.DONE) {
                countDoneSubTask++;
            } else if (subtask.getStatus() == Status.IN_PROGRES) {
                countInProgressTask++;
            }
        }

        if (countDoneSubTask == checkSubtask.size()) {
            epic.setStatus(Status.DONE);
        } else if (countInProgressTask > 0) {
            epic.setStatus(Status.IN_PROGRES);
        }
    }

    public void deleteSubTaskOnId(int id) {
        subtasks.remove(id);
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
    }

    public Epic getEpicOnId(int id) {
        return epics.get(id);
    }

    public int createEpic(Epic newEpic) {
        epics.put(AllTypeTaskId, newEpic);
        newEpic.setIdTask(AllTypeTaskId);
        AllTypeTaskId++;

        return newEpic.getIdTask();
    }

    public void updateEpic(int id, Epic updateEpic) {
        epics.put(id, updateEpic);
    }

    public void deleteEpicOnId(int id) {
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
