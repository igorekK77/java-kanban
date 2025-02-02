package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class TaskHandler extends BaseHttpHandler {
    TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String httpMethodName = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String[] elementURI = uri.getPath().split("/");
        Gson gson = HttpTaskServer.getGson();

        switch (httpMethodName) {
            case "GET":
                if (elementURI.length == 2) {
                    sendText(httpExchange, gson.toJson(taskManager.getAllTask()));
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        Task task = taskManager.getTaskById(id);
                        if (task == null) {
                            sendNotFound(httpExchange, "Задачи с ID = " + id + " не существует");
                        } else {
                            sendText(httpExchange, gson.toJson(task));
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "POST":
                if (elementURI.length == 2) {
                    InputStream is = httpExchange.getRequestBody();
                    String taskInfo = new String(is.readAllBytes(), BaseHttpHandler.standardCharsets);
                    Task task = gson.fromJson(taskInfo, Task.class);
                    taskManager.setIdCounter(task.getIdTask());
                    if (taskManager.getAllTask().contains(task)) {
                        sendHasInteractions(httpExchange, "Данная задача уже существует!");
                    } else {
                        taskManager.createTask(task);
                        HttpTaskServer.searchMaxIdCounter(taskManager);
                        sendText(httpExchange, gson.toJson(task));
                    }
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        InputStream is = httpExchange.getRequestBody();
                        String taskInfo = new String(is.readAllBytes(), BaseHttpHandler.standardCharsets);
                        if (getTaskByIdNotHistory(id) != null) {
                            Task task = gson.fromJson(taskInfo, Task.class);
                            task.setIdTask(id);
                            taskManager.updateTask(task);
                            sendText(httpExchange,  gson.toJson(task));
                        } else {
                            sendNotFound(httpExchange, "Задачи с id = " + id + " не существует!");
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "DELETE":
                try {
                    int id = Integer.parseInt(elementURI[2]);
                    Task task = getTaskByIdNotHistory(id);
                    if (task != null) {
                        taskManager.deleteTaskById(id);
                        sendText(httpExchange, "Задача успешно удалена!");
                    } else {
                        sendNotFound(httpExchange, "Задачи с id = " + id + " не существует!");
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;

        }
    }

    private Task getTaskByIdNotHistory(int id) {
        Task task = null;
        for (Task task1: taskManager.getAllTask()) {
            if (task1.getIdTask() == id) {
                task = task1;
            }
        }
        return task;
    }
}
