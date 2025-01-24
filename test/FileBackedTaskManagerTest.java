import manager.FileBackedTaskManager;
import manager.Managers;
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

public class FileBackedTaskManagerTest {
    private static FileBackedTaskManager fileBackedTaskManager;
    private File file;
    private static final Task task1 = new Task("Test1", "DTest1", Status.NEW);
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
        FileBackedTaskManager fileBackedTaskManager1 = Managers.getFileBackedTaskManager(file);
        Assertions.assertEquals(task1, fileBackedTaskManager1.getAllTask().get(0));
    }

    @Test
    public void checkUploadingEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager1 = Managers.getFileBackedTaskManager(file);
        Assertions.assertEquals(0, fileBackedTaskManager1.getAllSubTask().size());
        Assertions.assertEquals(0, fileBackedTaskManager1.getAllTask().size());
        Assertions.assertEquals(0, fileBackedTaskManager1.getAllEpic().size());
    }

    @Test
    public void checkCreationVariousTasksToFile() {
        Task task2 = new Task("task2", "task2", Status.NEW);
        Epic epic2 = new Epic("epic2", "epic2");
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createEpic(epic2);
        Subtask subtask = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask());
        fileBackedTaskManager.createSubTask(subtask);

        Assertions.assertEquals(2, fileBackedTaskManager.loadFromFile(file).getAllTask().size());
        Assertions.assertEquals(2, fileBackedTaskManager.loadFromFile(file).getAllEpic().size());
        Assertions.assertEquals(1, fileBackedTaskManager.loadFromFile(file).getAllSubTask().size());
    }

    @Test
    public void checkSavingVariousTasksToFile() {
        Task task2 = new Task("task2", "task2", Status.NEW);
        Epic epic2 = new Epic("epic2", "epic2");
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createEpic(epic2);
        Subtask subtask = new Subtask("subtask2", "subtask2", Status.NEW, epic1.getIdTask());
        fileBackedTaskManager.createSubTask(subtask);

        FileBackedTaskManager fileBackedTaskManager1 = Managers.getFileBackedTaskManager(file);
        Assertions.assertEquals(task1.getNameTask(),fileBackedTaskManager1.getAllTask().get(0).getNameTask());
        Assertions.assertEquals(task1.getDescriptionTask(), fileBackedTaskManager1.getAllTask().get(0).getDescriptionTask());
        Assertions.assertEquals(task1.getStatus(), fileBackedTaskManager1.getAllTask().get(0).getStatus());

        Assertions.assertEquals(epic1.getNameTask(), fileBackedTaskManager1.getAllEpic().get(0).getNameTask());
        Assertions.assertEquals(epic1.getDescriptionTask(), fileBackedTaskManager1.getAllEpic().get(0).getDescriptionTask());

        Assertions.assertEquals(subtask.getNameTask(), fileBackedTaskManager1.getAllSubTask().get(0).getNameTask());
        Assertions.assertEquals(subtask.getDescriptionTask(), fileBackedTaskManager1.getAllSubTask().get(0).getDescriptionTask());

    }
}
