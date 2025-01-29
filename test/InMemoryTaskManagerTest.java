import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEachInMemoryTaskManagerTest() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void utilityClassReturnsInitializedAndReadyToUseInstanceOfInMemoryTaskManager() {
        TaskManager taskManager = Managers.getInMemoryTaskManager();
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Assertions.assertEquals(testTaskManager, taskManager);
    }

}