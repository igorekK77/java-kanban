import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InMemoryHistoryManagerTest {
    private static TaskManager inMemoryTaskManager;
    private static Duration duration = Duration.ofMinutes(25);
    private static LocalDateTime startTime = LocalDateTime.now();

    private static final Task task1 = new Task("Test1", "DTest1", Status.NEW, duration, startTime);
    private static Epic epic1 = new Epic("Epic1", "DEpic1");

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getInMemoryTaskManager();
        epic1 = new Epic("Epic1", "DEpic1");
    }

    @Test
    public void utilityClassReturnsInitializedAndReadyToUseInstanceOfInMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryHistoryManager testHisotyManager = new InMemoryHistoryManager();
        Assertions.assertEquals(testHisotyManager, historyManager);
    }

    @Test
    public void checkingTheFunctionalityOfAddingBrowsingHistories() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask.getIdTask());

        ArrayList<Task> checkTask = new ArrayList<>();
        checkTask.add(task1);
        checkTask.add(epic1);
        checkTask.add(subtask);

        Assertions.assertEquals(checkTask, inMemoryTaskManager.getHistory());
    }

    @Test
    public void checkingForTheAbsenceOfTheSameEntriesInTheHistory() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask.getIdTask());
        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());

        ArrayList<Task> checkTask = new ArrayList<>();
        checkTask.add(task1);
        checkTask.add(epic1);
        checkTask.add(subtask);
        checkTask.add(task1);
        checkTask.add(epic1);
        checkTask.add(task1);
        checkTask.add(task1);
        checkTask.add(task1);
        checkTask.add(epic1);

        Set<Task> checkHistory = new HashSet<>(checkTask);

        Assertions.assertEquals(checkHistory.size(), inMemoryTaskManager.getHistory().size());

    }

    @Test
    public void checkingTheDeletionOfIssueFromHistory() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask.getIdTask());

        inMemoryTaskManager.deleteTaskById(task1.getIdTask());

        ArrayList<Task> checkHistory = new ArrayList<>();
        checkHistory.add(epic1);
        checkHistory.add(subtask);

        Assertions.assertEquals(checkHistory, inMemoryTaskManager.getHistory());
    }

    @Test
    public void checkingTheDeletionAllSubtasksAfterEpicDeleted() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask2);
        Epic epic2 = new Epic("epic2", "epic2");
        inMemoryTaskManager.createEpic(epic2);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask2.getIdTask());
        inMemoryTaskManager.getEpicById(epic2.getIdTask());

        inMemoryTaskManager.deleteEpicById(epic1.getIdTask());

        ArrayList<Task> checkHistory = new ArrayList<>();
        checkHistory.add(task1);
        checkHistory.add(epic2);

        Assertions.assertEquals(checkHistory, inMemoryTaskManager.getHistory());
    }

    @Test
    public void checkTheWorkTaskBrowsingHistory() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask1.getIdTask());
        ArrayList<Task> checkingTheHistory = new ArrayList<>();
        checkingTheHistory.add(task1);
        checkingTheHistory.add(epic1);
        checkingTheHistory.add(subtask1);

        Assertions.assertEquals(checkingTheHistory, inMemoryTaskManager.getHistory());
    }

}
