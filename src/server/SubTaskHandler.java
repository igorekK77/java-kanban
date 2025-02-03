package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class SubTaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubTaskHandler(TaskManager taskManager) {
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
                    sendText(httpExchange, gson.toJson(taskManager.getAllSubTask()));
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        Subtask subtask = taskManager.getSubTaskById(id);
                        if (subtask == null) {
                            sendNotFound(httpExchange, "Подзадачи с ID = " + id + " не существует");
                        } else {
                            sendText(httpExchange, gson.toJson(subtask));
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "POST":
                if (elementURI.length == 2) {
                    InputStream is = httpExchange.getRequestBody();
                    String subtaskInfo = new String(is.readAllBytes(), BaseHttpHandler.standardCharsets);
                    Subtask subtask = gson.fromJson(subtaskInfo, Subtask.class);
                    taskManager.createSubTask(subtask);
                    sendText(httpExchange, "Подзадача_создана: \n" + gson.toJson(subtask));
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        InputStream is = httpExchange.getRequestBody();
                        String subtaskInfo = new String(is.readAllBytes(), BaseHttpHandler.standardCharsets);
                        if (getSubTaskByIdNotHistory(id) != null) {
                            Subtask subtask = gson.fromJson(subtaskInfo, Subtask.class);
                            taskManager.updateSubTask(subtask);
                            sendText(httpExchange, gson.toJson(subtask));
                        } else {
                            sendNotFound(httpExchange, "Подзадачи с ID = " + id + " не существует");
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "DELETE":
                try {
                    int id = Integer.parseInt(elementURI[2]);
                    taskManager.deleteSubTaskById(id);
                    sendText(httpExchange, "Подзадача с Id = " + id + " успешно удалена!");
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private Subtask getSubTaskByIdNotHistory(int id) {
        Subtask subtask = null;
        for (Subtask subtask1: taskManager.getAllSubTask()) {
            if (subtask1.getIdTask() == id) {
                subtask = subtask1;
            }
        }
        return subtask;
    }

}
