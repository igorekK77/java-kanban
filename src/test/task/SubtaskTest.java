package test.task;

import task.Epic;
import task.Status;
import task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    @Test
    public void subTaskChecksEqualToEachOtherIfTheirIdEqual() {
        Epic epic1 = new Epic("test1", "dtest1");
        Subtask subtask1 = new Subtask("SubTask1", "DSubTask1", Status.NEW, epic1.getIdTask());
        Subtask subtask2 = new Subtask("SubTask2", "DSubTask2", Status.NEW, epic1.getIdTask());
        subtask1.setIdTask(1);
        subtask2.setIdTask(1);

        Assertions.assertTrue(subtask1.equals(subtask2));
    }
}