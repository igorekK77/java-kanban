package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String httpMethodName = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String[] elementsURI = uri.getPath().split("/");
        Gson gson = HttpTaskServer.getGson();

        switch (httpMethodName) {
            case "GET":
                if (elementsURI.length == 2) {
                    sendText(httpExchange, gson.toJson(taskManager.getAllEpic()));
                } else if (elementsURI.length == 3) {
                    try {
                        int id = Integer.parseInt(elementsURI[2]);
                        Epic epic = taskManager.getEpicById(id);
                        if (epic == null) {
                            sendNotFound(httpExchange, "Задачи с ID = " + id + " не существует");
                        } else {
                            sendText(httpExchange, gson.toJson(epic));
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        int id = Integer.parseInt(elementsURI[2]);
                        Epic epic = getEpicByIdNotHistory(id);
                        if (epic == null) {
                            sendNotFound(httpExchange, "Задачи с ID = " + id + " не существует");
                        } else {
                            sendText(httpExchange, gson.toJson(epic));
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "POST":
                if (elementsURI.length == 2) {
                    InputStream is = httpExchange.getRequestBody();
                    String epicInfo = new String(is.readAllBytes(), BaseHttpHandler.standardCharsets);
                    Epic epic = gson.fromJson(epicInfo, Epic.class);
                    taskManager.createEpic(epic);
                    sendText(httpExchange, "Задача_создана: \n" + gson.toJson(epic));
                } else {
                    try {
                      int id = Integer.parseInt(elementsURI[2]);
                      InputStream is = httpExchange.getRequestBody();
                      String epicInfo = new String(is.readAllBytes(), standardCharsets);
                      if (getEpicByIdNotHistory(id) != null) {
                          Epic epic = gson.fromJson(epicInfo, Epic.class);
                          taskManager.updateEpic(epic);
                          sendText(httpExchange, gson.toJson(epic));
                      } else {
                          sendNotFound(httpExchange, "Задачи с ID = " + id + " не существует");
                      }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "DELETE":
                try {
                    int id = Integer.parseInt(elementsURI[2]);
                    taskManager.deleteEpicById(id);
                    sendText(httpExchange, "Задача с Id = " + id + " успешно удалена!");
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private Epic getEpicByIdNotHistory(int id) {
        Epic epic = null;
        for (Epic epic1: taskManager.getAllEpic()) {
            if (epic1.getIdTask() == id) {
                epic = epic1;
            }
        }
        return epic;
    }
}
