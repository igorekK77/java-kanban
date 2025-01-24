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
import java.util.HashMap;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
            for (Task task: super.getAllTask()) {
                bufferedWriter.write(toString(task) + "\n");
            }
            for (Subtask subtask: super.getAllSubTask()) {
                bufferedWriter.write(toString(subtask) + "\n");
            }
            for (Epic epic: super.getAllEpic()) {
                bufferedWriter.write(toString(epic) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка! Невозможно сохранить информацию в файл!");
        }
    }

    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,", task.getIdTask(), taskTypes.TASK, task.getNameTask(),
                task.getStatus(), task.getDescriptionTask());
    }

    private String toString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%s,", subtask.getIdTask(), taskTypes.SUBTASK, subtask.getNameTask(),
                subtask.getStatus(), subtask.getDescriptionTask(), subtask.getEpicId());
    }

    private String toString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,", epic.getIdTask(), taskTypes.EPIC, epic.getNameTask(),
                epic.getStatus(), epic.getDescriptionTask());
    }

    @Override
    public void removeAllTask(){
        super.removeAllTask();
        save();
    }

    @Override
    public Task createTask(Task newTask) {
        Task createNewTask = super.createTask(newTask);
        save();
        return createNewTask;
    }

    private Task createWriteTask(Task newTask) {
        return super.createTask(newTask);
    }

    @Override
    public void updateTask(int id, Task updateTask) {
        super.updateTask(id, updateTask);
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

    private Subtask createWriteSubTask(Subtask newSubTask) {
        return super.createSubTask(newSubTask);
    }

    @Override
    public void updateSubTask(int id, Subtask updateSubTask) {
        super.updateSubTask(id, updateSubTask);
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

    private Epic createWriteEpic(Epic newEpic) {
        return super.createEpic(newEpic);
    }

    @Override
    public void updateEpic(int id, Epic updateEpic) {
        super.updateEpic(id, updateEpic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    private static Status getStatus(String status) {
        if (status.equals("NEW")) {
            return Status.NEW;
        } else if (status.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        Map<Integer, Integer> oldNewEpicId = new HashMap<>();
        try {
            String allFile = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] fileLines2 = allFile.split("\n");
            String[] fileLines1 = new String[fileLines2.length - 1];
            for (int i = 0; i < fileLines1.length; i++) {
                fileLines1[i] = fileLines2[i+1];
            }
            String[] fileLines = new String[fileLines1.length];

            int counterTask = 0;
            for (String str: fileLines1) {
                String[] string = str.split(",");
                if (string[1].equals("TASK")) {
                    fileLines[counterTask] = str;
                    counterTask++;
                }
            }

            for (String str: fileLines1) {
                String[] string = str.split(",");
                if (string[1].equals("EPIC")) {
                    fileLines[counterTask] = str;
                    counterTask++;
                }
            }

            for (String str: fileLines1) {
                String[] string = str.split(",");
                if (string[1].equals("SUBTASK")) {
                    fileLines[counterTask] = str;
                    counterTask++;
                }
            }

            for (String task: fileLines) {
                String[] elements = task.split(",");
                if (elements[1].equals("TASK")) {
                    Task task1 = new Task(elements[2], elements[4], getStatus(elements[3]));
                    fileBackedTaskManager.createWriteTask(task1);
                } else if (elements[1].equals("SUBTASK")) {
                    int forEpicId = 0;
                    for (Integer epicId: oldNewEpicId.keySet()) {
                        if (epicId == Integer.parseInt(elements[5])) {
                            forEpicId = oldNewEpicId.get(epicId);
                        }
                    }
                    Subtask subtask1 = new Subtask(elements[2], elements[4], getStatus(elements[3]),
                            forEpicId);
                    fileBackedTaskManager.createWriteSubTask(subtask1);
                } else {
                    Epic epic1 = new Epic(elements[2], elements[4]);
                    int keys = Integer.parseInt(elements[0]);
                    fileBackedTaskManager.createWriteEpic(epic1);
                    oldNewEpicId.put(keys, epic1.getIdTask());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NegativeArraySizeException ne) {
            System.out.println("Файл пуст!");
        }
        return fileBackedTaskManager;
    }

    public static void main(String[] args) {
        File file1 = new File("");
        TaskManager fileBackedTaskManager1 = Managers.getFileBackedTaskManager(file1);
        Task testTask1 = new Task("testTask1", "testTask1", Status.NEW);
        Epic testEpic1 = new Epic("testEpic1", "testEpic1");
        fileBackedTaskManager1.createEpic(testEpic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, testEpic1.getIdTask());
        Subtask subtask2 = new Subtask("subtask2", "subtask2", Status.IN_PROGRESS, testEpic1.getIdTask());
        fileBackedTaskManager1.createTask(testTask1);
        fileBackedTaskManager1.createSubTask(subtask1);
        fileBackedTaskManager1.createSubTask(subtask2);
        Epic epic2 = new Epic("epic2", "epic2");
        fileBackedTaskManager1.createEpic(epic2);
        Subtask subtask3 = new Subtask("subtask3", "subtask3", Status.NEW, epic2.getIdTask());
        fileBackedTaskManager1.createSubTask(subtask3);

        System.out.println(fileBackedTaskManager1.getAllTask());
        System.out.println(fileBackedTaskManager1.getAllEpic());
        System.out.println(fileBackedTaskManager1.getAllSubTask());

        System.out.println(fileBackedTaskManager1.getTaskById(testTask1.getIdTask()));
        System.out.println(fileBackedTaskManager1.getEpicById(epic2.getIdTask()));
        System.out.println(fileBackedTaskManager1.getSubTaskById(subtask1.getIdTask()));




    }

}
