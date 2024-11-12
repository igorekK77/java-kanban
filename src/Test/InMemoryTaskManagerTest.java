package Test;
import HistoryManager.InMemoryHistoryManager;
import Manager.*;
import Task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;



class InMemoryTaskManagerTest {
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

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

}