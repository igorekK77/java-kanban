import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import manager.*;
import task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


class InMemoryTaskManagerTest {
    private static  TaskManager inMemoryTaskManager;
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


    @Test
    public void checkingMethodOfReturningEpicSubtasks() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test1", "testd1", Status.NEW, epic1.getIdTask(), duration, startTime);
        Subtask subtask2 = new Subtask("Test2", "dTest2", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);
        inMemoryTaskManager.createSubTask(subtask2);

        ArrayList<Subtask> forCheck = new ArrayList<>();
        forCheck.add(subtask1);
        forCheck.add(subtask2);

        Assertions.assertEquals(forCheck, inMemoryTaskManager.getEpicSubtask(epic1.getIdTask()));
    }

    @Test
    public void utilityClassReturnsInitializedAndReadyToUseInstanceOfInMemoryTaskManager() {
        TaskManager taskManager = Managers.getInMemoryTaskManager();
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
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask(), duration, startTime);
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
        Task task = new Task(name, descriptionTask, status, duration, startTime);
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
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);

        inMemoryTaskManager.getTaskById(task1.getIdTask());
        inMemoryTaskManager.getEpicById(epic1.getIdTask());
        inMemoryTaskManager.getSubTaskById(subtask1.getIdTask());

        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(0), task1);
        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(1), epic1);
        Assertions.assertEquals(inMemoryTaskManager.getHistory().get(2), subtask1);
    }

    @Test
    public void checkNoConflictsBetweenTasksWithTheSpecifiedIdAndGeneratedId() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test2", "dTest2", Status.NEW, 0, duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);
        task1.setIdTask(1);
        inMemoryTaskManager.createTask(task1);

        Assertions.assertNotNull(inMemoryTaskManager.getSubTaskById(subtask1.getIdTask()));
        Assertions.assertNotNull(inMemoryTaskManager.getTaskById(task1.getIdTask()));
    }

    @Test
    public void checkImpossibilityOfMakingSubtaskYourOwnEpic() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test1", "dtest1", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("test2", "dtest2", Status.NEW, subtask1.getIdTask(), duration, startTime);

        Epic testEpic = inMemoryTaskManager.getEpicById(subtask2.getEpicId());

        Assertions.assertNull(testEpic);
    }


    @Test
    public void checkSubtasksNotStoreOldIDsInsideThemselves() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);
        int checkId = subtask.getIdTask();
        inMemoryTaskManager.deleteSubTaskById(subtask.getIdTask());

        Assertions.assertNotEquals(checkId, subtask.getIdTask());
    }

    @Test
    public void checkNoIrrelevantSubtaskIDsLeftInsideTheEpics() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask2);

        inMemoryTaskManager.deleteSubTaskById(subtask.getIdTask());
        inMemoryTaskManager.deleteSubTaskById(subtask2.getIdTask());

        ArrayList<Subtask> checkId = new ArrayList<>();
        checkId.add(subtask1);

        Assertions.assertEquals(checkId, epic1.getSubtasksEpic());
    }

    @Test
    public void taskInstanceSettersAllowChangeAnyYourFields() {
        inMemoryTaskManager.createTask(task1);
        task1.setIdTask(18);
        Task testTask2 = inMemoryTaskManager.getTaskById(task1.getIdTask());


        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);
        subtask.setIdTask(55);

        Epic epic2 = new Epic("epic2", "epic2");
        inMemoryTaskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic2.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);
        epic2.setIdTask(40);


        Assertions.assertNull(testTask2);
        Assertions.assertNull(inMemoryTaskManager.getSubTaskById(subtask.getIdTask()));
        Assertions.assertNotEquals(inMemoryTaskManager.getEpicById(subtask1.getEpicId()), inMemoryTaskManager.getEpicById(epic2.getIdTask()));
    }

    @Test
    public void checkEpicStatusCalculation() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(), duration, LocalDateTime.of(2024,6,25,21,20));
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, LocalDateTime.now());
        inMemoryTaskManager.createSubTask(subtask2);
        Assertions.assertEquals(Status.valueOf("NEW"), epic1.getStatus());

        Epic epic2 = new Epic("epic2", "epic2");
        inMemoryTaskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("subtask3", "subtask3", Status.DONE, epic2.getIdTask(), duration, LocalDateTime.of(2023,6,25,21,20));
        inMemoryTaskManager.createSubTask(subtask3);
        Subtask subtask4 = new Subtask("subtask4", "subtask4", Status.DONE, epic2.getIdTask(), duration, LocalDateTime.now());
        inMemoryTaskManager.createSubTask(subtask4);
        Assertions.assertEquals(Status.valueOf("DONE"), epic2.getStatus());

        Epic epic3 = new Epic("epic3", "epic3");
        inMemoryTaskManager.createEpic(epic3);
        Subtask subtask5 = new Subtask("subtask5", "subtask5", Status.NEW, epic3.getIdTask(), duration, LocalDateTime.of(2022,6,25,21,20));
        inMemoryTaskManager.createSubTask(subtask5);
        Subtask subtask6 = new Subtask("subtask6", "subtask6", Status.DONE, epic3.getIdTask(), duration, LocalDateTime.now());
        inMemoryTaskManager.createSubTask(subtask6);
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), epic3.getStatus());

        Epic epic4 = new Epic("epic4", "epic4");
        inMemoryTaskManager.createEpic(epic4);
        Subtask subtask7 = new Subtask("subtask7", "subtask7", Status.IN_PROGRESS, epic4.getIdTask(), duration, LocalDateTime.of(2021,6,25,21,20));
        inMemoryTaskManager.createSubTask(subtask7);
        Subtask subtask8 = new Subtask("subtask8", "subtask8", Status.IN_PROGRESS, epic4.getIdTask(), duration, LocalDateTime.now());
        inMemoryTaskManager.createSubTask(subtask8);
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), epic4.getStatus());
    }


}