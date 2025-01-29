import manager.*;
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
import java.util.List;


public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    private static Duration duration = Duration.ofMinutes(25);
    private static LocalDateTime startTime = LocalDateTime.now();
    private static final Task task1 = new Task("Test1", "DTest1", Status.NEW, duration, startTime);
    private static Epic epic1 = new Epic("Epic1", "DEpic1");

    @BeforeEach
    public void beforeEach() {
        epic1 = new Epic("Epic1", "DEpic1");
    }

    @Test
    public void checkingMethodOfReturningEpicSubtasks() {
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test1", "testd1", Status.NEW, epic1.getIdTask(), duration, startTime);
        Subtask subtask2 = new Subtask("Test2", "dTest2", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        ArrayList<Subtask> forCheck = new ArrayList<>();
        forCheck.add(subtask1);
        forCheck.add(subtask2);

        Assertions.assertEquals(forCheck, taskManager.getEpicSubtask(epic1.getIdTask()));
    }

    @Test
    public void checkingThatInMemoryTaskManagerAddsTasksOfDifferentTypesAndCanFindThemById() {
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask1);

        Assertions.assertEquals(taskManager.getTaskById(task1.getIdTask()), task1);
        Assertions.assertEquals(taskManager.getEpicById(epic1.getIdTask()), epic1);
        Assertions.assertEquals(taskManager.getSubTaskById(subtask1.getIdTask()), subtask1);
    }

    @Test
    public void immutabilityOfTheTaskIsCheckedWhenAddingTaskToManager() {
        String name = "Test1";
        String descriptionTask = "dTest1";
        Status status = Status.NEW;
        Task task = new Task(name, descriptionTask, status, duration, startTime);
        taskManager.createTask(task);
        Task newTaskFromManager = taskManager.getTaskById(task.getIdTask());

        Assertions.assertEquals(name, newTaskFromManager.getNameTask());
        Assertions.assertEquals(descriptionTask, newTaskFromManager.getDescriptionTask());
        Assertions.assertEquals(status, newTaskFromManager.getStatus());
    }

    @Test
    public void shouldRetainTaskVersionInHistory() {
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask1);

        taskManager.getTaskById(task1.getIdTask());
        taskManager.getEpicById(epic1.getIdTask());
        taskManager.getSubTaskById(subtask1.getIdTask());

        Assertions.assertEquals(taskManager.getHistory().get(0), task1);
        Assertions.assertEquals(taskManager.getHistory().get(1), epic1);
        Assertions.assertEquals(taskManager.getHistory().get(2), subtask1);
    }

    @Test
    public void checkNoConflictsBetweenTasksWithTheSpecifiedIdAndGeneratedId() {
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test2", "dTest2", Status.NEW, 0, duration, startTime);
        taskManager.createSubTask(subtask1);
        task1.setIdTask(1);
        taskManager.createTask(task1);

        Assertions.assertNotNull(taskManager.getSubTaskById(subtask1.getIdTask()));
        Assertions.assertNotNull(taskManager.getTaskById(task1.getIdTask()));
    }

    @Test
    public void checkImpossibilityOfMakingSubtaskYourOwnEpic() {
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("test1", "dtest1", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("test2", "dtest2", Status.NEW, subtask1.getIdTask(), duration, startTime);

        Epic testEpic = taskManager.getEpicById(subtask2.getEpicId());

        Assertions.assertNull(testEpic);
    }


    @Test
    public void checkSubtasksNotStoreOldIDsInsideThemselves() {
        taskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask);
        int checkId = subtask.getIdTask();
        taskManager.deleteSubTaskById(subtask.getIdTask());

        Assertions.assertNotEquals(checkId, subtask.getIdTask());
    }

    @Test
    public void checkNoIrrelevantSubtaskIDsLeftInsideTheEpics() {
        taskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask2);

        taskManager.deleteSubTaskById(subtask.getIdTask());
        taskManager.deleteSubTaskById(subtask2.getIdTask());

        ArrayList<Subtask> checkId = new ArrayList<>();
        checkId.add(subtask1);

        Assertions.assertEquals(checkId, epic1.getSubtasksEpic());
    }

    @Test
    public void taskInstanceSettersAllowChangeAnyYourFields() {
        taskManager.createTask(task1);
        task1.setIdTask(18);
        Task testTask2 = taskManager.getTaskById(task1.getIdTask());


        taskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask);
        subtask.setIdTask(55);

        Epic epic2 = new Epic("epic2", "epic2");
        taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic2.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask1);
        epic2.setIdTask(40);


        Assertions.assertNull(testTask2);
        Assertions.assertNull(taskManager.getSubTaskById(subtask.getIdTask()));
        Assertions.assertNotEquals(taskManager.getEpicById(subtask1.getEpicId()), taskManager.getEpicById(epic2.getIdTask()));
    }

    @Test
    public void checkEpicStatusCalculation() {
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(), duration, LocalDateTime.of(2024,6,25,21,20));
        taskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, LocalDateTime.now());
        taskManager.createSubTask(subtask2);
        Assertions.assertEquals(Status.valueOf("NEW"), epic1.getStatus());

        Epic epic2 = new Epic("epic2", "epic2");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("subtask3", "subtask3", Status.DONE, epic2.getIdTask(), duration, LocalDateTime.of(2023,6,25,21,20));
        taskManager.createSubTask(subtask3);
        Subtask subtask4 = new Subtask("subtask4", "subtask4", Status.DONE, epic2.getIdTask(), duration, LocalDateTime.now());
        taskManager.createSubTask(subtask4);
        Assertions.assertEquals(Status.valueOf("DONE"), epic2.getStatus());

        Epic epic3 = new Epic("epic3", "epic3");
        taskManager.createEpic(epic3);
        Subtask subtask5 = new Subtask("subtask5", "subtask5", Status.NEW, epic3.getIdTask(), duration, LocalDateTime.of(2022,6,25,21,20));
        taskManager.createSubTask(subtask5);
        Subtask subtask6 = new Subtask("subtask6", "subtask6", Status.DONE, epic3.getIdTask(), duration, LocalDateTime.now());
        taskManager.createSubTask(subtask6);
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), epic3.getStatus());

        Epic epic4 = new Epic("epic4", "epic4");
        taskManager.createEpic(epic4);
        Subtask subtask7 = new Subtask("subtask7", "subtask7", Status.IN_PROGRESS, epic4.getIdTask(), duration, LocalDateTime.of(2021,6,25,21,20));
        taskManager.createSubTask(subtask7);
        Subtask subtask8 = new Subtask("subtask8", "subtask8", Status.IN_PROGRESS, epic4.getIdTask(), duration, LocalDateTime.now());
        taskManager.createSubTask(subtask8);
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), epic4.getStatus());
    }

    @Test
    public void checkOutputOfTaskListInOrderOfPriority() {
        taskManager.createTask(task1);
        Task task2 = new Task("task2", "task2", Status.NEW, duration, LocalDateTime.of(2025,
                1,23,11,30));
        taskManager.createTask(task2);
        Task task3 = new Task("task3", "task3", Status.NEW, duration, LocalDateTime.of(2025,
                1,24,17,55));
        taskManager.createTask(task3);
        List<Task> checkSetTask = new ArrayList<>();
        checkSetTask.add(task2);
        checkSetTask.add(task3);
        checkSetTask.add(task1);
        Assertions.assertEquals(checkSetTask, taskManager.getPrioritizedTasks());
    }

    @Test
    public void checkCorrectCalculationIntersectionOfTaskTimeIntervals() {
        taskManager.createTask(task1);
        Task task2 = new Task("task2", "task3", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2025,1,19,19,30));
        taskManager.createTask(task2);
        Task task3 = new Task("task3", "task3", Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2025,1,19,19,20));
        taskManager.createTask(task3);
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(20), LocalDateTime.of(2025,1,11,16,30));
        taskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(25), LocalDateTime.of(2025,1,11,16,51));
        taskManager.createSubTask(subtask2);

        List<Task> setCheckTask = new ArrayList<>();
        setCheckTask.add(subtask1);
        setCheckTask.add(subtask2);
        setCheckTask.add(task2);
        setCheckTask.add(task1);

        Assertions.assertEquals(setCheckTask, taskManager.getPrioritizedTasks());

    }

    @Test
    public void checkTheCalculationOfEpicTotalTimeBySubtasks() {
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(20), LocalDateTime.of(2025,1,11,16,30));
        taskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(25), LocalDateTime.of(2025,1,11,16,51));
        taskManager.createSubTask(subtask2);
        Assertions.assertEquals(45, epic1.getDuration().toMinutes());
    }

    @Test
    public void checkTheSubtaskRelatedEpic() {
        taskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask);
        Assertions.assertEquals(epic1.getIdTask(), subtask.getEpicId());
    }

    @Test
    public void checkCorrectnessStatusEpicBasedStatusSubtasks() {
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask1);
        Assertions.assertEquals(Status.valueOf("NEW"), epic1.getStatus());
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.IN_PROGRESS, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask2);
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), epic1.getStatus());
        taskManager.deleteSubTaskById(subtask1.getIdTask());
        taskManager.deleteSubTaskById(subtask2.getIdTask());
        Subtask subtask3 = new Subtask("subtask3", "subtask3", Status.DONE, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask3);
        Assertions.assertEquals(Status.valueOf("DONE"), epic1.getStatus());
    }

}
