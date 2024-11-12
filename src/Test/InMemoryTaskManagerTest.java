package Test;
import HistoryManager.HistoryManager;
import HistoryManager.InMemoryHistoryManager;
import Manager.*;
import Task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;



class InMemoryTaskManagerTest {
    private static  InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void CheckingTheWorkTaskBrowsingHistory() {
        Task task1 = new Task("Test1", "DTest1", Status.NEW);
        inMemoryTaskManager.createTask(task1);
        Epic epic1 = new Epic("Epic1", "DEpic1");
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask());
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

    @Test
    public void CheckingTheWorkTaskBrowsingHistoryAfetr10() {
        Task task1 = new Task("Test1", "DTest1", Status.NEW);
        inMemoryTaskManager.createTask(task1);
        Epic epic1 = new Epic("Epic1", "DEpic1");
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask1.getIdTask());
        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask1.getIdTask());
        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask1.getIdTask());
        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        ArrayList<Task> checkingTheHistory = new ArrayList<>();
        checkingTheHistory.add(epic1);
        checkingTheHistory.add(subtask1);
        checkingTheHistory.add(task1);
        checkingTheHistory.add(epic1);
        checkingTheHistory.add(subtask1);
        checkingTheHistory.add(task1);
        checkingTheHistory.add(epic1);
        checkingTheHistory.add(subtask1);
        checkingTheHistory.add(task1);
        checkingTheHistory.add(epic1);
        Assertions.assertEquals(checkingTheHistory, inMemoryTaskManager.getHistory());
    }

    @Test
    public void checkingMethodOfReturningEpicSubtasks() {
        Epic epic = new Epic("Epic1", "Epic1");
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("test1", "testd1", Status.NEW, epic.getIdTask());
        Subtask subtask2 = new Subtask("Test2", "dTest2", Status.NEW, epic.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);
        inMemoryTaskManager.createSubTask(subtask2);

        ArrayList<Subtask> forCheck = new ArrayList<>();
        forCheck.add(subtask1);
        forCheck.add(subtask2);

        Assertions.assertEquals(forCheck, inMemoryTaskManager.getEpicSubtask(epic.getIdTask()));
    }

    @Test
    public void utilityClassReturnsInitializedAndReadyToUseInstanceOfInMemoryTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Assertions.assertEquals(testTaskManager, taskManager);
    }

    @Test
    public void utilityClassReturnsInitializedAndReadyToUseInstanceOfInMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        InMemoryHistoryManager testHisotyManager = new InMemoryHistoryManager();
        Assertions.assertEquals(testHisotyManager, historyManager);
    }

    @Test
    public void checkingThatInMemoryTaskManagerAddsTasksOfDifferentTypesAndCanFindThemById() {
        Task task = new Task("Test1", "DTest1", Status.NEW);
        inMemoryTaskManager.createTask(task);
        Epic epic = new Epic("Epic1", "DEpic1");
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("test1", "testd1", Status.NEW, epic.getIdTask());
        inMemoryTaskManager.createSubTask(subtask);

        Assertions.assertEquals(inMemoryTaskManager.getTaskById(task.getIdTask()), task);
        Assertions.assertEquals(inMemoryTaskManager.getEpicById(epic.getIdTask()), epic);
        Assertions.assertEquals(inMemoryTaskManager.getSubTaskById(subtask.getIdTask()), subtask);
    }

    @Test
    public void immutabilityOfTheTaskIsCheckedWhenAddingTaskToManager() {
        String name = "Test1";
        String descriptionTask = "dTest1";
        Status status = Status.NEW;
        Task task = new Task(name, descriptionTask, status);
        inMemoryTaskManager.createTask(task);
        Task newTaskFromManager = inMemoryTaskManager.getTaskById(task.getIdTask());

        Assertions.assertEquals(name, newTaskFromManager.getNameTask());
        Assertions.assertEquals(descriptionTask, newTaskFromManager.getDescriptionTask());
        Assertions.assertEquals(status, newTaskFromManager.getStatus());
    }

    @Test
    public void tasksAddedToTheHistoryManagerRetainThePreviousVersionOfTaskAndItsData() {
        Task task = new Task("Test1", "DTest1", Status.NEW);
        inMemoryTaskManager.createTask(task);
        Epic epic = new Epic("Epic1", "DEpic1");
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("test1", "testd1", Status.NEW, epic.getIdTask());
        inMemoryTaskManager.createSubTask(subtask);

        inMemoryTaskManager.getTaskById(task.getIdTask());
        inMemoryTaskManager.getEpicById(epic.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask.getIdTask());

        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(0), task);
        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(1), epic);
        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(2), subtask);
    }
}