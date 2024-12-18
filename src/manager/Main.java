package manager;

import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import task.*;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        //Создание
        Task task1 = new Task("Сегодня нужно срочно сдать проект",
                "Доделать проект и отправить его на проверку",
                Status.NEW);
        final int idTask1 = manager.createTask(task1).getIdTask();

        Task task2 = new Task("Отправить отчет по лабораторной работе",
                "Написать выводы в работе и отправить ее на проверку",
                Status.NEW);
        final int idTask2 = manager.createTask(task2).getIdTask();

        Epic epic1 = new Epic("Разработка мобильного приложения для планирования задач.",
                "Разработка мобильного приложения для планирования задач.");

        final int idEpic1 = manager.createEpic(epic1).getIdTask();

        Subtask subtask1 = new Subtask("Создание пользовательского интерфейса (UI) для приложения.",
                "Создание пользовательского интерфейса (UI) для приложения.",
                Status.NEW, idEpic1);
        final int idSubtask1 = manager.createSubTask(subtask1).getIdTask();

        Subtask subtask2 = new Subtask(" Реализация функции уведомлений и напоминаний о задачах.",
                " Реализация функции уведомлений и напоминаний о задачах.", Status.NEW, idEpic1);
        final int idSubtask2 = manager.createSubTask(subtask2).getIdTask();

        Epic epic2 = new Epic("Подготовка презентации для конференции",
                "Подготовка презентации для конференции.");
        final int idEpic2 = manager.createEpic(epic2).getIdTask();

        Subtask subtask3 = new Subtask("Сбор материалов и данных для слайдов презентации.",
                "Сбор материалов и данных для слайдов презентации.", Status.NEW, idEpic2);
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
        manager.updateTask(idTask1, task1);
        task2.setStatus(Status.DONE);
        manager.updateTask(idTask2, task2);

        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(idSubtask1, subtask1);

        subtask2.setStatus(Status.DONE);
        manager.updateSubTask(idSubtask2, subtask2);

        subtask3.setStatus(Status.DONE);
        manager.updateSubTask(idSubtask3, subtask3);

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
