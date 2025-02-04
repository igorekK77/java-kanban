package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

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
                InputStream is = httpExchange.getRequestBody();
                String taskInfo = new String(is.readAllBytes(), BaseHttpHandler.standardCharsets);
                Task task = gson.fromJson(taskInfo, Task.class);
                int taskId = task.getIdTask();
                if (taskId > 0) {
                    taskManager.updateTask(task);
                    sendText(httpExchange,  gson.toJson(task));
                } else {
                    taskManager.createTask(task);
                    sendText(httpExchange, gson.toJson(task));
                }
                break;

            case "DELETE":
                try {
                    int id = Integer.parseInt(elementURI[2]);
                    taskManager.deleteTaskById(id);
                    sendText(httpExchange, "Задача успешно удалена!");
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;

        }
    }

}
