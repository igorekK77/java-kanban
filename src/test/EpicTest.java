package test;

import task.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void epicChecksEqualToEachOtherIfTheirIdEqual() {
        Epic epic1 = new Epic("Epic1", "DEpic1");
        Epic epic2 = new Epic("Epic2", "DEpic2");

        Assertions.assertTrue(epic1.equals(epic2));
    }

}