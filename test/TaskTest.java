package test.task;

import task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    private  Duration duration = Duration.ofMinutes(25);
    private  LocalDateTime startTime = LocalDateTime.now();
    @Test
    public void taskChecksEqualToEachOtherIfTheirIdEqual() {
        Task task1 = new Task("Test1", "DTest1", Status.NEW, duration, startTime);
        Task task2 = new Task("Test2", "DTest2", Status.NEW, duration, startTime);

        task1.setIdTask(1);
        task2.setIdTask(1);

        Assertions.assertTrue(task1.equals(task2));
    }
}