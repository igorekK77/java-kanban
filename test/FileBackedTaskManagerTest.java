import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
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
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;
    private static Duration duration = Duration.ofMinutes(25);
    private static LocalDateTime startTime = LocalDateTime.now();
    private static final Task task1 = new Task("Test1", "DTest1", Status.NEW, duration, startTime);
    private static Epic epic1 = new Epic("Epic1", "DEpic1");

    @BeforeEach
    public void beforeEachFileTaskManagerTest() throws IOException {
        file = File.createTempFile("test", ".txt");
        taskManager = new FileBackedTaskManager(file);
        epic1 = new Epic("Epic1", "DEpic1");
    }

    @AfterEach
    public void afterEach() {
        file.deleteOnExit();
    }

    @Test
    public void checkSavingEmptyFile() {
        taskManager.createTask(task1);
        TaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(task1, fileBackedTaskManager1.getAllTask().get(0));
    }

    @Test
    public void checkUploadingEmptyFile() {
        TaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(0, fileBackedTaskManager1.getAllSubTask().size());
        Assertions.assertEquals(0, fileBackedTaskManager1.getAllTask().size());
        Assertions.assertEquals(0, fileBackedTaskManager1.getAllEpic().size());
    }

    @Test
    public void checkCreationVariousTasksToFile() {
        Task task2 = new Task("task2", "task2", Status.NEW, duration, startTime);
        Epic epic2 = new Epic("epic2", "epic2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        Subtask subtask = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask);

        Assertions.assertEquals(2, taskManager.loadFromFile(file).getAllTask().size());
        Assertions.assertEquals(2, taskManager.loadFromFile(file).getAllEpic().size());
        Assertions.assertEquals(1, taskManager.loadFromFile(file).getAllSubTask().size());
    }

    @Test
    public void checkSavingVariousTasksToFile() {
        Task task2 = new Task("task2", "task2", Status.NEW, duration, startTime);
        Epic epic2 = new Epic("epic2", "epic2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        Subtask subtask = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, startTime);
        taskManager.createSubTask(subtask);

        TaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(taskManager.getAllTask(), fileBackedTaskManager1.getAllTask());
        Assertions.assertEquals(taskManager.getAllSubTask(), fileBackedTaskManager1.getAllSubTask());
        Assertions.assertEquals(taskManager.getAllEpic(), fileBackedTaskManager1.getAllEpic());
    }

    @Test
    public void checkCorrectInterceptionExceptionsWhenWorkingWithFiles() {
        File file1 = new File("");
        Assertions.assertThrows(ManagerSaveException.class, () -> Managers.getDefault(file1).createTask(task1));
        Assertions.assertThrows(ManagerSaveException.class, () -> Managers.getDefault(file1).createEpic(epic1));
        Assertions.assertDoesNotThrow(() -> Managers.getDefault(file).createTask(task1));
        Assertions.assertDoesNotThrow(() -> Managers.getDefault(file).createEpic(epic1));
    }

    @Test
    public void checkingTheSortedListTasksUploadedFromFile() {
        taskManager.createTask(task1);
        Task task2 = new Task("task2", "task2", Status.NEW, duration,
                LocalDateTime.of(2023,11,24,11,56));
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        Subtask subtask = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(),
                duration, LocalDateTime.of(2024,5,23,14,20));
        taskManager.createSubTask(subtask);

        TaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        List<Task> checkSortedTask = new ArrayList<>();
        checkSortedTask.add(task2);
        checkSortedTask.add(subtask);
        checkSortedTask.add(task1);

        Assertions.assertEquals(checkSortedTask, fileBackedTaskManager1.getPrioritizedTasks());
    }
}
