package manager;
import java.io.File;
import historymanager.*;

public class Managers {
    public static TaskManager getDefault(File file) {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

}
