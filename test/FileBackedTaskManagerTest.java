import manager.FileBackedTaskManager;
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

public class FileBackedTaskManagerTest {
    private static FileBackedTaskManager fileBackedTaskManager;
    private File file;
    private static Duration duration = Duration.ofMinutes(25);
    private static LocalDateTime startTime = LocalDateTime.now();
    private static final Task task1 = new Task("Test1", "DTest1", Status.NEW, duration, startTime);
    private static Epic epic1 = new Epic("Epic1", "DEpic1");

    @BeforeEach
    public void beforeEach() throws IOException {
        file = File.createTempFile("test", ".txt");
        fileBackedTaskManager = new FileBackedTaskManager(file);
        epic1 = new Epic("Epic1", "DEpic1");
    }

    @AfterEach
    public void afterEach() {
        file.deleteOnExit();
    }

    @Test
    public void checkSavingEmptyFile() {
        fileBackedTaskManager.createTask(task1);
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
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createEpic(epic2);
        Subtask subtask = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, startTime);
        fileBackedTaskManager.createSubTask(subtask);

        Assertions.assertEquals(2, fileBackedTaskManager.loadFromFile(file).getAllTask().size());
        Assertions.assertEquals(2, fileBackedTaskManager.loadFromFile(file).getAllEpic().size());
        Assertions.assertEquals(1, fileBackedTaskManager.loadFromFile(file).getAllSubTask().size());
    }

    @Test
    public void checkSavingVariousTasksToFile() {
        Task task2 = new Task("task2", "task2", Status.NEW, duration, startTime);
        Epic epic2 = new Epic("epic2", "epic2");
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createEpic(epic2);
        Subtask subtask = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask(), duration, startTime);
        fileBackedTaskManager.createSubTask(subtask);

        TaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertEquals(fileBackedTaskManager.getAllTask(), fileBackedTaskManager1.getAllTask());
        Assertions.assertEquals(fileBackedTaskManager.getAllSubTask(), fileBackedTaskManager1.getAllSubTask());
        Assertions.assertEquals(fileBackedTaskManager.getAllEpic(), fileBackedTaskManager1.getAllEpic());

    }
}
