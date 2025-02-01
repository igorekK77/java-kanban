package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import historymanager.*;
import server.DurationAdapter;
import server.LocalDateTimeAdapter;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        //Создание
        LocalDateTime localDateTime = LocalDateTime.of(2024, 5, 25, 23,30);
        Duration duration = Duration.ofMinutes(25);
        Task task1 = new Task("Сегодня нужно срочно сдать проект",
                "Доделать проект и отправить его на проверку",
                Status.NEW, duration, localDateTime);
        final int idTask1 = manager.createTask(task1).getIdTask();

        Task task2 = new Task("Отправить отчет по лабораторной работе",
                "Написать выводы в работе и отправить ее на проверку",
                Status.NEW, duration, localDateTime);
        final int idTask2 = manager.createTask(task2).getIdTask();

        Epic epic1 = new Epic("Разработка мобильного приложения для планирования задач.",
                "Разработка мобильного приложения для планирования задач.");

        final int idEpic1 = manager.createEpic(epic1).getIdTask();

        Subtask subtask1 = new Subtask("Создание пользовательского интерфейса (UI) для приложения.",
                "Создание пользовательского интерфейса (UI) для приложения.",
                Status.NEW, idEpic1, duration, localDateTime);
        final int idSubtask1 = manager.createSubTask(subtask1).getIdTask();

        Subtask subtask2 = new Subtask(" Реализация функции уведомлений и напоминаний о задачах.",
                " Реализация функции уведомлений и напоминаний о задачах.", Status.NEW, idEpic1, duration, LocalDateTime.now());
        final int idSubtask2 = manager.createSubTask(subtask2).getIdTask();

        Epic epic2 = new Epic("Подготовка презентации для конференции",
                "Подготовка презентации для конференции.");
        final int idEpic2 = manager.createEpic(epic2).getIdTask();

        Subtask subtask3 = new Subtask("Сбор материалов и данных для слайдов презентации.",
                "Сбор материалов и данных для слайдов презентации.", Status.NEW, idEpic2, duration, localDateTime);
        final int idSubtask3 = manager.createSubTask(subtask3).getIdTask();


        printAllTasks(manager);

        System.out.println(" ");
        System.out.println(" ");

        manager.getTaskById(task1.getIdTask());
        manager.getTaskById(task2.getIdTask());
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubTaskById(idSubtask1));
        System.out.println(manager.getSubTaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubTaskById(idSubtask3));

        //Обновление
        task1.setStatus(Status.IN_PROGRESS);
        task1.setIdTask(idTask1);
        manager.updateTask(task1);
        task2.setStatus(Status.DONE);
        task2.setIdTask(idTask2);
        manager.updateTask(task2);

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setIdTask(idSubtask1);
        manager.updateSubTask(subtask1);

        subtask2.setStatus(Status.DONE);
        subtask2.setIdTask(idSubtask2);
        manager.updateSubTask(subtask2);

        subtask3.setStatus(Status.DONE);
        subtask3.setIdTask(idSubtask3);
        manager.updateSubTask(subtask3);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubTaskById(idSubtask1));
        System.out.println(manager.getSubTaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubTaskById(idSubtask3));


        //Удаление
        manager.deleteTaskById(idTask2);
        manager.deleteSubTaskById(idSubtask2);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubTaskById(idSubtask1));
        System.out.println(manager.getSubTaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubTaskById(idSubtask3));

        manager.deleteEpicById(idEpic2);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubTaskById(idSubtask1));
        System.out.println(manager.getSubTaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubTaskById(idSubtask3));

        System.out.println("----------------------------------");
        System.out.println("----------------------------------");

        InMemoryTaskManager manager2 = new InMemoryTaskManager();

        Task testTask1 = new Task("testTask1", "testTask1", Status.NEW, duration, localDateTime);
        Task testTask2 = new Task("testTask2", "testTask2", Status.NEW, duration, localDateTime);
        Epic testEpic1 = new Epic("testEpic1", "testEpic1");
        Epic testEpic2 = new Epic("testEpic2", "testEpic2");
        manager2.createEpic(testEpic2);
        Subtask testSubtask1 = new Subtask("testSubtask1", "testSubtask1", Status.NEW, testEpic2.getIdTask(), duration, localDateTime);
        Subtask testSubtask2 = new Subtask("testSubtask2", "testSubtask2", Status.NEW, testEpic2.getIdTask(), duration, localDateTime);
        Subtask testSubtask3 = new Subtask("testSubtask3", "testSubtask3", Status.NEW, testEpic2.getIdTask(), duration, localDateTime);

        manager2.createTask(testTask1);
        manager2.createTask(testTask2);
        manager2.createEpic(testEpic1);
        manager2.createSubTask(testSubtask1);
        manager2.createSubTask(testSubtask2);
        manager2.createSubTask(testSubtask3);

        manager2.getTaskById(testTask1.getIdTask());
        manager2.getTaskById(testTask2.getIdTask());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        manager2.getEpicById(testEpic1.getIdTask());
        manager2.getEpicById(testEpic2.getIdTask());
        manager2.getSubTaskById(testSubtask1.getIdTask());
        manager2.getTaskById(testTask2.getIdTask());
        manager2.getTaskById(testTask2.getIdTask());
        manager2.getSubTaskById(testSubtask1.getIdTask());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        manager2.getSubTaskById(testSubtask2.getIdTask());
        manager2.getSubTaskById(testSubtask3.getIdTask());
        manager2.deleteTaskById(testTask1.getIdTask());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        manager2.deleteEpicById(testEpic2.getIdTask());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        System.out.println("-----------------------------");
        System.out.println("-----------------------------");


        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task newTask1 = new Task("newTask1", "newTask1", Status.NEW, duration, localDateTime);
        Task newTask2 = new Task("newTask2", "newTask2", Status.NEW, duration, LocalDateTime.of(2024,5,29,23,29));
        Epic newEpic = new Epic("newEpic", "newEpic");
        taskManager.createTask(newTask1);
        taskManager.createTask(newTask2);
        taskManager.createEpic(newEpic);
        Subtask newSubtask1 = new Subtask("newSubtask1", "newSubtask1", Status.NEW, newEpic.getIdTask(), duration, LocalDateTime.of(2024,5,20,23,10));
        Subtask newSubtask2 = new Subtask("newSubtask2", "newSubtask2", Status.NEW, newEpic.getIdTask(), duration, LocalDateTime.of(2024,5,21,23,15));
        taskManager.createSubTask(newSubtask1);
        taskManager.createSubTask(newSubtask2);
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        taskManager.deleteSubTaskById(newSubtask1.getIdTask());
        System.out.println(taskManager.getAllSubTask());
        Epic epic12 = new Epic("tEpic", "tEpic");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String toJson = gson.toJson(newSubtask2);
        System.out.println(toJson);
        Subtask subtask = gson.fromJson(toJson, Subtask.class);
        System.out.println(subtask);
    }

    private static void printAllTasks(TaskManager manager) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        System.out.println("Задачи:");
        for (Task task : manager.getAllTask()) {
            System.out.println(manager.getTaskById(task.getIdTask()));
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpic()) {

            System.out.println(manager.getEpicById(epic.getIdTask()));

            for (Task task : manager.getEpicSubtask(epic.getIdTask())) {
                System.out.println("--> " + manager.getSubTaskById(task.getIdTask()));
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubTask()) {
            System.out.println(manager.getSubTaskById(subtask.getIdTask()));
        }

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("Кол-во записей в истории: ");
        System.out.println(historyManager.getHistory().size());
    }
}
