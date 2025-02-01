package manager;

import task.*;
import historymanager.*;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));
    private HistoryManager historyManager = Managers.getDefaultHistory();

    protected int idCounter = 0;

    @Override
    public int getIdCounter() {
        return idCounter;
    }

    @Override
    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTask() {
        for (Task task: tasks.values()) {
            if (historyManager.getHistory().contains(task)) {
                historyManager.remove(task.getIdTask());
            }
            if (prioritizedTasks.contains(task)) {
                prioritizedTasks.remove(task);
            }
        }
        tasks.clear();
    }


    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    private boolean isTimeSegmentsIntersect(Task newTask, Task task) {
        LocalDateTime startsTimeNewTask = newTask.getStartTime();
        LocalDateTime finishTimeNewTask = newTask.getEndTime();

        LocalDateTime startsTimeTask = task.getStartTime();
        LocalDateTime finishTimeTask = task.getEndTime();

        if (startsTimeNewTask.isAfter(finishTimeTask) || finishTimeNewTask.isBefore(startsTimeTask)) {
            return false;
        } else {
            return true;
        }

    }

    private boolean isNotIntersectionContain(Task newTask) {
        return prioritizedTasks.stream()
                .noneMatch(task -> isTimeSegmentsIntersect(newTask, task));
    }

    @Override
    public Task createTask(Task newTask) {
        tasks.put(idCounter, newTask);
        newTask.setIdTask(idCounter);
        idCounter++;
        if (isNotIntersectionContain(newTask)) {
            prioritizedTasks.add(newTask);
        }
        return newTask;
    }

    @Override
    public void updateTask(Task updateTask) {
        prioritizedTasks.remove(tasks.get(updateTask.getIdTask()));
        tasks.put(updateTask.getIdTask(), updateTask);
        if (isNotIntersectionContain(updateTask)) {
            prioritizedTasks.add(updateTask);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }



    @Override
    public List<Subtask> getAllSubTask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllSubTask() {
        subtasks.values().stream()
                        .forEach(subtask -> {
                            if (prioritizedTasks.contains(subtask)) {
                                prioritizedTasks.remove(subtask);
                            }
                            Epic epic = epicSearchByIdInsideTheClass(subtask.getIdTask());
                            List<Subtask> subTasks = epic.getSubtasksEpic();
                            subTasks = subTasks.stream()
                                    .filter(subtask1 -> subtask1.getIdTask() != subtask.getIdTask())
                                    .toList();
                            epic.setAllSubtasks(subTasks);
                            if (historyManager.getHistory().contains(subtask)) {
                                historyManager.remove(subtask.getIdTask());
                            }
                        });
        subtasks.clear();
    }

    @Override
    public Subtask getSubTaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Subtask createSubTask(Subtask newSubTask) {
        subtasks.put(idCounter, newSubTask);
        newSubTask.setIdTask(idCounter);
        updateEpicStatus(newSubTask);
        if (isNotIntersectionContain(newSubTask)) {
            prioritizedTasks.add(newSubTask);
        }
        idCounter++;
        Epic epic = epicSearchByIdInsideTheClass(newSubTask.getEpicId());
        if (epic != null) {
            epic.setSubtaskEpic(newSubTask);
            switch (newSubTask.getStatus()) {
                case NEW:
                    break;
                case IN_PROGRESS:
                    epic.setStatus(Status.IN_PROGRESS);
                    break;
            }
        }
        return newSubTask;
    }

    @Override
    public void updateSubTask(Subtask updateSubTask) {
        prioritizedTasks.remove(subtasks.get(updateSubTask.getIdTask()));
        subtasks.put(updateSubTask.getIdTask(), updateSubTask);
        updateEpicStatus(updateSubTask);
        if (isNotIntersectionContain(updateSubTask)) {
            prioritizedTasks.add(updateSubTask);
        }
    }


    private void updateEpicStatus(Subtask updateSubTask) {
        Epic epic = epicSearchByIdInsideTheClass(updateSubTask.getEpicId());
        List<Subtask> checkSubtask = subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getIdTask())
                .toList();

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
        if (historyManager.getHistory().contains(subtask)) {
            historyManager.remove(subtask.getIdTask());
        }
        Epic epic = epicSearchByIdInsideTheClass(subtask.getEpicId());
        if (epic != null) {
            List<Subtask> subTasks = epic.getSubtasksEpic();
            for (Subtask sTask: subTasks) {
                if (sTask.getIdTask() == subtask.getIdTask()) {
                    subTasks.remove(sTask);
                    break;
                }
            }
            epic.setAllSubtasks(subTasks);
        }
        prioritizedTasks.remove(subtasks.get(id));
        subtasks.remove(id);
        subtask.setIdTask(-1);
        updateEpicStatus(subtask);
    }



    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpic() {
        epics.values().stream()
                        .forEach(epic -> {
                            if (prioritizedTasks.contains(epic)) {
                                prioritizedTasks.remove(epic);
                            }
                            if (historyManager.getHistory().contains(epic)) {
                                epic.getSubtasksEpic().stream()
                                        .forEach(subtask -> {
                                            if (historyManager.getHistory().contains(subtask)) {
                                                historyManager.remove(subtask.getIdTask());
                                            }
                                        });
                                historyManager.remove(epic.getIdTask());
                            }
                        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
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
    public List<Subtask> getEpicSubtask(int epicid) {
        Epic epic = epicSearchByIdInsideTheClass(epicid);
        return epic.getSubtasksEpic();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        prioritizedTasks.remove(epics.get(updateEpic.getIdTask()));
        epics.put(updateEpic.getIdTask(), updateEpic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicSearchByIdInsideTheClass(id);
        epics.remove(id);
        historyManager.remove(id);
        epic.getSubtasksEpic().stream()
                .forEach(subtask -> {
                    subtasks.remove(subtask.getIdTask());
                    if (historyManager.getHistory().contains(subtask)) {
                        historyManager.remove(subtask.getIdTask());
                    }
                });

    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


}
