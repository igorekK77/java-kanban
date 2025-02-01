package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
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
                    sendText(httpExchange, gson.toJson(HttpTaskServer.taskManager.getAllSubTask()));
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        Subtask subtask = HttpTaskServer.taskManager.getSubTaskById(id);
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
                    String subtaskInfo = new String(is.readAllBytes(), standartCharset);
                    Subtask subtask = gson.fromJson(subtaskInfo, Subtask.class);
                    HttpTaskServer.taskManager.setIdCounter(subtask.getIdTask());
                    if (HttpTaskServer.taskManager.getAllSubTask().contains(subtask)) {
                        sendHasInteractions(httpExchange, "Данная подзадача уже существует!");
                    } else {
                        HttpTaskServer.taskManager.createSubTask(subtask);
                        HttpTaskServer.searchMaxIdCounter();
                        sendText(httpExchange, "Подзадача_создана: \n" + gson.toJson(subtask));
                    }
                } else {
                    try {
                        int id = Integer.parseInt(elementURI[2]);
                        InputStream is = httpExchange.getRequestBody();
                        String subtaskInfo = new String(is.readAllBytes(), standartCharset);
                        if (HttpTaskServer.taskManager.getSubTaskById(id) != null) {
                            Subtask subtask = gson.fromJson(subtaskInfo, Subtask.class);
                            subtask.setIdTask(id);
                            HttpTaskServer.taskManager.updateSubTask(subtask);
                            sendText(httpExchange,  gson.toJson(subtask));
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
                    if (HttpTaskServer.taskManager.getSubTaskById(id) != null) {
                        HttpTaskServer.taskManager.deleteSubTaskById(id);
                        sendText(httpExchange, "Подзадача с Id = " + id + " успешно удалена!");
                    } else {
                        sendNotFound(httpExchange, "Подзадачи с ID = " + id + " не существует");
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

}
