package Test;

import Task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void taskChecksEqualToEachOtherIfTheirIdEqual() {
        Task task1 = new Task("Test1", "DTest1", Status.NEW);
        Task task2 = new Task("Test2", "DTest2", Status.NEW);

        task1.setIdTask(1);
        task2.setIdTask(1);

        Assertions.assertTrue(task1.equals(task2));
    }
}