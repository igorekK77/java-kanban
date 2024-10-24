public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        //Создание
        Task task1 = new Task("Сегодня нужно срочно сдать проект",
                "Доделать проект и отправить его на проверку",
                Status.NEW);
        final int idTask1 = manager.createTask(task1);

        Task task2 = new Task("Отправить отчет по лабораторной работе",
                "Написать выводы в работе и отправить ее на проверку",
                Status.NEW);
        final int idTask2 = manager.createTask(task2);

        Epic epic1 = new Epic("Разработка мобильного приложения для планирования задач.",
                "Разработка мобильного приложения для планирования задач.");

        final int idEpic1 = manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Создание пользовательского интерфейса (UI) для приложения.",
                "Создание пользовательского интерфейса (UI) для приложения.",
                Status.NEW, idEpic1);
        final int idSubtask1 = manager.createSubTask(subtask1);

        Subtask subtask2 = new Subtask(" Реализация функции уведомлений и напоминаний о задачах.",
                " Реализация функции уведомлений и напоминаний о задачах.", Status.NEW, idEpic1);
        final int idSubtask2 = manager.createSubTask(subtask2);

        Epic epic2 = new Epic("Подготовка презентации для конференции",
                "Подготовка презентации для конференции.");
        final int idEpic2 = manager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Сбор материалов и данных для слайдов презентации.",
                "Сбор материалов и данных для слайдов презентации.", Status.NEW, idEpic2);
        final int idSubtask3 = manager.createSubTask(subtask3);

        System.out.println(manager.getTaskOnId(idTask1));
        System.out.println(manager.getTaskOnId(idTask2));
        System.out.println(manager.getEpicOnId(idEpic1));
        System.out.println(manager.getSubTaskOnId(idSubtask1));
        System.out.println(manager.getSubTaskOnId(idSubtask2));
        System.out.println(manager.getEpicOnId(idEpic2));
        System.out.println(manager.getSubTaskOnId(idSubtask3));

        //Обновление
        task1.setStatus(Status.IN_PROGRES);
        manager.updateTask(idTask1, task1);
        task2.setStatus(Status.DONE);
        manager.updateTask(idTask2, task2);

        subtask1.setStatus(Status.IN_PROGRES);
        manager.updateSubTask(idSubtask1, subtask1);

        subtask2.setStatus(Status.DONE);
        manager.updateSubTask(idSubtask2, subtask2);

        subtask3.setStatus(Status.DONE);
        manager.updateSubTask(idSubtask3, subtask3);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskOnId(idTask1));
        System.out.println(manager.getTaskOnId(idTask2));
        System.out.println(manager.getEpicOnId(idEpic1));
        System.out.println(manager.getSubTaskOnId(idSubtask1));
        System.out.println(manager.getSubTaskOnId(idSubtask2));
        System.out.println(manager.getEpicOnId(idEpic2));
        System.out.println(manager.getSubTaskOnId(idSubtask3));


        //Удаление
        manager.deleteTaskOnId(idTask2);
        manager.deleteEpicOnId(idEpic2);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskOnId(idTask1));
        System.out.println(manager.getTaskOnId(idTask2));
        System.out.println(manager.getEpicOnId(idEpic1));
        System.out.println(manager.getSubTaskOnId(idSubtask1));
        System.out.println(manager.getSubTaskOnId(idSubtask2));
        System.out.println(manager.getEpicOnId(idEpic2));
        System.out.println(manager.getSubTaskOnId(idSubtask3));
    }
}
