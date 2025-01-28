import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class TaskManagerTest<T extends TaskManager> {

    private static  TaskManager inMemoryTaskManager;
    private static FileBackedTaskManager fileBackedTaskManager;
    private File file;
    private static Duration duration = Duration.ofMinutes(25);
    private static LocalDateTime startTime = LocalDateTime.now();

    private static final Task task1 = new Task("Test1", "DTest1", Status.NEW, duration, startTime);
    private static Epic epic1 = new Epic("Epic1", "DEpic1");

    @BeforeEach
    public void beforeEach() throws IOException {
        inMemoryTaskManager = Managers.getInMemoryTaskManager();
        epic1 = new Epic("Epic1", "DEpic1");
        file = File.createTempFile("test", ".txt");
        fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @AfterEach
    public void afterEach() {
        file.deleteOnExit();
    }

    @Test
    public void checkTheSubtaskRelatedEpic() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask);
        Assertions.assertEquals(epic1.getIdTask(), subtask.getEpicId());
    }

    @Test
    public void checkCorrectnessStatusEpicBasedStatusSubtasks() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask1);
        Assertions.assertEquals(Status.valueOf("NEW"), epic1.getStatus());
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.IN_PROGRESS, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask2);
        Assertions.assertEquals(Status.valueOf("IN_PROGRESS"), epic1.getStatus());
        inMemoryTaskManager.deleteSubTaskById(subtask1.getIdTask());
        inMemoryTaskManager.deleteSubTaskById(subtask2.getIdTask());
        Subtask subtask3 = new Subtask("subtask3", "subtask3", Status.DONE, epic1.getIdTask(), duration, startTime);
        inMemoryTaskManager.createSubTask(subtask3);
        Assertions.assertEquals(Status.valueOf("DONE"), epic1.getStatus());
    }

    @Test
    public void checkCorrectInterceptionExceptionsWhenWorkingWithFiles() {
        File file1 = new File("C:\\testEmpty.txt");
        Assertions.assertThrows(ManagerSaveException.class, () -> Managers.getDefault(file1).createTask(task1));
        Assertions.assertThrows(ManagerSaveException.class, () -> Managers.getDefault(file1).createEpic(epic1));
        Assertions.assertDoesNotThrow(() -> Managers.getDefault(file).createTask(task1));
        Assertions.assertDoesNotThrow(() -> Managers.getDefault(file).createEpic(epic1));
    }

    @Test
    public void checkingOutputOfTaskListInOrderOfPriority() {
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("task2", "task2", Status.NEW, duration, LocalDateTime.of(2025,
                1,23,11,30));
        inMemoryTaskManager.createTask(task2);
        Task task3 = new Task("task3", "task3", Status.NEW, duration, LocalDateTime.of(2025,
                1,24,17,55));
        inMemoryTaskManager.createTask(task3);
        Set<Task> checkSetTask = new HashSet<>();
        checkSetTask.add(task1);
        checkSetTask.add(task3);
        checkSetTask.add(task2);
        Assertions.assertEquals(checkSetTask, inMemoryTaskManager.getPrioritizedTasks());
    }

    @Test
    public void checkingCorrectCalculationIntersectionOfTaskTimeIntervals() {
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("task2", "task3", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2025,1,19,19,30));
        inMemoryTaskManager.createTask(task2);
        Task task3 = new Task("task3", "task3", Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2025,1,19,19,20));
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(20), LocalDateTime.of(2025,1,11,16,30));
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(25), LocalDateTime.of(2025,1,11,16,51));
        inMemoryTaskManager.createSubTask(subtask2);

        Set<Task> setCheckTask = new HashSet<>();
        setCheckTask.add(epic1);
        setCheckTask.add(subtask1);
        setCheckTask.add(subtask2);
        setCheckTask.add(task1);
        setCheckTask.add(task2);

        Assertions.assertEquals(setCheckTask, inMemoryTaskManager.getPrioritizedTasks());

    }

    @Test
    public void checkingTheCalculationOfEpicTotalTimeBySubtasks() {
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(20), LocalDateTime.of(2025,1,11,16,30));
        inMemoryTaskManager.createSubTask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(),
                Duration.ofMinutes(25), LocalDateTime.of(2025,1,11,16,51));
        inMemoryTaskManager.createSubTask(subtask2);
        Assertions.assertEquals(45, epic1.getDuration().toMinutes());
    }


}
