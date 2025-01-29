package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("id,type,name,status,description,epic,duration,startTime\n");
            for (Task task: super.getAllTask()) {
                bufferedWriter.write(task.toString() + "\n");
            }
            for (Subtask subtask: super.getAllSubTask()) {
                bufferedWriter.write(subtask.toString() + "\n");
            }
            for (Epic epic: super.getAllEpic()) {
                bufferedWriter.write(epic.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка! Невозможно сохранить информацию в файл!");
        }
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public Task createTask(Task newTask) {
        Task createNewTask = super.createTask(newTask);
        save();
        return createNewTask;
    }

    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public Subtask createSubTask(Subtask newSubTask) {
        Subtask createNewSubtask = super.createSubTask(newSubTask);
        save();
        return createNewSubtask;
    }

    @Override
    public void updateSubTask(Subtask updateSubTask) {
        super.updateSubTask(updateSubTask);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        Epic createNewEpic = super.createEpic(newEpic);
        save();
        return createNewEpic;
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    private int generatorId = 0;

    private void createTaskFromString(String[] elements) {
        Task task = new Task(elements[2], elements[4], Status.valueOf(elements[3]),
                Duration.ofMinutes(Integer.parseInt(elements[5])), LocalDateTime.parse(elements[6]));
        idCounter = Integer.parseInt(elements[0]);
        if (idCounter > generatorId) {
            generatorId = idCounter;
        }
        super.createTask(task);
    }

    private void createSubTaskFromString(String[] elements) {
        Subtask subtask = new Subtask(elements[2], elements[4], Status.valueOf(elements[3]),
                Integer.parseInt(elements[5]), Duration.ofMinutes(Integer.parseInt(elements[6])),
                LocalDateTime.parse(elements[7]));
        idCounter = Integer.parseInt(elements[0]);
        if (idCounter > generatorId) {
            generatorId = idCounter;
        }
        super.createSubTask(subtask);
    }

    private void createEpicFromString(String[] elements) {
        Epic epic = new Epic(elements[2], elements[4]);
        idCounter = Integer.parseInt(elements[0]);
        if (idCounter > generatorId) {
            generatorId = idCounter;
        }
        super.createEpic(epic);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            String allFile = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] fileLinesWithHeader = allFile.split("\n");
            String[] fileLinesWithoutHeader = new String[fileLinesWithHeader.length - 1];
            for (int i = 0; i < fileLinesWithoutHeader.length; i++) {
                fileLinesWithoutHeader[i] = fileLinesWithHeader[i + 1];
            }
            String[] fileLines = new String[fileLinesWithoutHeader.length];

            int counterTask = 0;
            for (String str: fileLinesWithoutHeader) {
                String[] string = str.split(",");
                if (string[1].equals("TASK")) {
                    fileLines[counterTask] = str;
                    counterTask++;
                }
            }

            for (String str: fileLinesWithoutHeader) {
                String[] string = str.split(",");
                if (string[1].equals("EPIC")) {
                    fileLines[counterTask] = str;
                    counterTask++;
                }
            }

            for (String str: fileLinesWithoutHeader) {
                String[] string = str.split(",");
                if (string[1].equals("SUBTASK")) {
                    fileLines[counterTask] = str;
                    counterTask++;
                }
            }

            for (String task: fileLines) {
                String[] elements = task.split(",");
                if (elements[1].equals("TASK")) {
                    fileBackedTaskManager.createTaskFromString(elements);
                } else if (elements[1].equals("SUBTASK")) {
                    fileBackedTaskManager.createSubTaskFromString(elements);
                } else {
                    fileBackedTaskManager.createEpicFromString(elements);
                }
            }
            fileBackedTaskManager.generatorId++;
            fileBackedTaskManager.idCounter = fileBackedTaskManager.generatorId;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NegativeArraySizeException ne) {
            System.out.println("Файл пуст!");
        }
        return fileBackedTaskManager;
    }

    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(25);
        File file1 = new File("C:\\variousFolders\\codeJava\\java-kanban\\taskStorage.txt");
        TaskManager fileBackedTaskManager1 = Managers.getDefault(file1);
        Task testTask1 = new Task("testTask1", "testTask1", Status.NEW, duration, startTime);
        Epic testEpic1 = new Epic("testEpic1", "testEpic1");
        fileBackedTaskManager1.createEpic(testEpic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, testEpic1.getIdTask(), duration, startTime);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.IN_PROGRESS, testEpic1.getIdTask(), duration, startTime);
        fileBackedTaskManager1.createTask(testTask1);
        fileBackedTaskManager1.createSubTask(subtask1);
        fileBackedTaskManager1.createSubTask(subtask2);
        Epic epic2 = new Epic("epic2", "epic2");
        fileBackedTaskManager1.createEpic(epic2);
        Subtask subtask3 = new Subtask("subtask3", "subtask3", Status.NEW, epic2.getIdTask(), duration, startTime);
        fileBackedTaskManager1.createSubTask(subtask3);

        System.out.println(fileBackedTaskManager1.getAllTask());
        System.out.println(fileBackedTaskManager1.getAllEpic());
        System.out.println(fileBackedTaskManager1.getAllSubTask());

        System.out.println(fileBackedTaskManager1.getTaskById(testTask1.getIdTask()));
        System.out.println(fileBackedTaskManager1.getEpicById(epic2.getIdTask()));
        System.out.println(fileBackedTaskManager1.getSubTaskById(subtask1.getIdTask()));


    }

}
