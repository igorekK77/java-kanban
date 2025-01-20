package test.manager;

import HistoryManager.HistoryManager;
import HistoryManager.InMemoryHistoryManager;
import manager.*;
import task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


class InMemoryTaskManagerTest {
    private static  InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    private static final Task task1 = new Task("Test1", "DTest1", Status.NEW);
    private static Epic epic1 = new Epic("Epic1", "DEpic1");

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic1 = new Epic("Epic1", "DEpic1");
    }

    @Test
    public void checkTheWorkTaskBrowsingHistory() {
        inMemoryTaskManager.createTask(task1);
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
    public void checkingMethodOfReturningEpicSubtasks() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test1", "testd1", Status.NEW, epic1.getIdTask());
        Subtask subtask2 = new Subtask("Test2", "dTest2", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);
        inMemoryTaskManager.createSubTask(subtask2);

        ArrayList<Subtask> forCheck = new ArrayList<>();
        forCheck.add(subtask1);
        forCheck.add(subtask2);

        Assertions.assertEquals(forCheck, inMemoryTaskManager.getEpicSubtask(epic1.getIdTask()));
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
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);

        Assertions.assertEquals(inMemoryTaskManager.getTaskById(task1.getIdTask()), task1);
        Assertions.assertEquals(inMemoryTaskManager.getEpicById(epic1.getIdTask()), epic1);
        Assertions.assertEquals(inMemoryTaskManager.getSubTaskById(subtask1.getIdTask()), subtask1);
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
    public void shouldRetainTaskVersionInHistory() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask1.getIdTask());

        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(0), task1);
        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(1), epic1);
        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(2), subtask1);
    }

    @Test
    public void checkImpossibilityOfAddingEpicToItselfAsSubtask() {
        inMemoryTaskManager.createEpic(epic1);
        epic1.setSubtaskIds(epic1.getIdTask());

        Assertions.assertTrue(epic1.getSubtaskIds().size() == 0);
    }

    @Test
    public void checkNoConflictsBetweenTasksWithTheSpecifiedIdAndGeneratedId() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test2", "dTest2", Status.NEW, 0);
        inMemoryTaskManager.createSubTask(subtask1);
        task1.setIdTask(1);
        inMemoryTaskManager.createTask(task1);

        Assertions.assertNotNull(inMemoryTaskManager.getSubTaskById(subtask1.getIdTask()));
        Assertions.assertNotNull(inMemoryTaskManager.getTaskById(task1.getIdTask()));
    }

    @Test
    public void checkImpossibilityOfMakingSubtaskYourOwnEpic() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test1", "dtest1", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("test2", "dtest2", Status.NEW, subtask1.getIdTask());

        Epic testEpic = inMemoryTaskManager.getEpicById(subtask2.getEpicId());

        Assertions.assertNull(testEpic);
    }

    @Test
    public void checkingTheFunctionalityOfAddingBrowsingHistories() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask());
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
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask());
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
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask());
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
    public void checkingTheDeletionAllSabtasksAfterEpicDeleted() {
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask());
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
    public void checkSubtasksNotStoreOldIDsInsideThemselves() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask);
        int checkId = subtask.getIdTask();
        inMemoryTaskManager.deleteSubTaskById(subtask.getIdTask());

        Assertions.assertNotEquals(checkId, subtask.getIdTask());
    }

    @Test
    public void checkNoIrrelevantSubtaskIDsLeftInsideTheEpics() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask2);

        inMemoryTaskManager.deleteSubTaskById(subtask.getIdTask());
        inMemoryTaskManager.deleteSubTaskById(subtask2.getIdTask());

        ArrayList<Integer> checkId = new ArrayList<>();
        checkId.add(subtask1.getIdTask());

        Assertions.assertEquals(checkId, epic1.getSubtaskIds());
    }

    @Test
    public void taskInstanceSettersAllowChangeAnyYourFields() {
        inMemoryTaskManager.createTask(task1);
        task1.setIdTask(18);
        Task testTask2 = inMemoryTaskManager.getTaskById(task1.getIdTask());


        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask());
        inMemoryTaskManager.createSubTask(subtask);
        subtask.setIdTask(55);
        ArrayList<Integer> subtaskIds = epic1.getSubtaskIds();

        Epic epic2 = new Epic("epic2", "epic2");
        inMemoryTaskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic2.getIdTask());
        inMemoryTaskManager.createSubTask(subtask1);
        epic2.setIdTask(40);


        Assertions.assertNull(testTask2);
        Assertions.assertNull(inMemoryTaskManager.getSubTaskById(subtask.getIdTask()));
        Assertions.assertNotEquals(inMemoryTaskManager.getEpicById(subtask1.getEpicId()), inMemoryTaskManager.getEpicById(epic2.getIdTask()));
    }


}