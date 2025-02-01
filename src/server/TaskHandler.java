package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private static final Charset standartCharset = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String httpMethodName = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String[] elementURI = uri.getPath().split("/");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();

        switch (httpMethodName) {
            case "GET":
                if (elementURI.length == 2) {
                    sendText(httpExchange, gson.toJson(HttpTaskServer.taskManager.getAllTask()));
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        Task task = HttpTaskServer.taskManager.getTaskById(id);
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
                    String taskInfo = new String(is.readAllBytes(), standartCharset);
                    Task task = gson.fromJson(taskInfo, Task.class);
                    HttpTaskServer.taskManager.setIdCounter(task.getIdTask());
                    if (HttpTaskServer.taskManager.getAllTask().contains(task)) {
                        sendHasInteractions(httpExchange, "Данная задача уже существует!");
                    } else {
                        HttpTaskServer.taskManager.createTask(task);
                        HttpTaskServer.searchMaxIdCounter();
                        sendText(httpExchange, gson.toJson(task));
                    }
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        InputStream is = httpExchange.getRequestBody();
                        String taskInfo = new String(is.readAllBytes(), standartCharset);
                        if (HttpTaskServer.taskManager.getTaskById(id) != null) {
                            Task task = gson.fromJson(taskInfo, Task.class);
                            task.setIdTask(id);
                            HttpTaskServer.taskManager.updateTask(task);
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
                    Task task = HttpTaskServer.taskManager.getTaskById(id);
                    if (task != null) {
                        HttpTaskServer.taskManager.deleteTaskById(id);
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
}
