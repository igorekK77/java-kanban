package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private static final Charset standartCharset = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String httpMethodName = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String[] elementsURI = uri.getPath().split("/");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();

        switch (httpMethodName) {
            case "GET":
                if (elementsURI.length == 2) {
                    sendText(httpExchange, gson.toJson(HttpTaskServer.taskManager.getAllEpic()));
                } else if (elementsURI.length == 3) {
                    try {
                        int id = Integer.parseInt(elementsURI[2]);
                        Epic epic = HttpTaskServer.taskManager.getEpicById(id);
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
                        Epic epic = HttpTaskServer.taskManager.getEpicById(id);
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
                InputStream is = httpExchange.getRequestBody();
                String epicInfo = new String(is.readAllBytes(), standartCharset);
                Epic epic = gson.fromJson(epicInfo, Epic.class);
                HttpTaskServer.taskManager.setIdCounter(epic.getIdTask());
                if (HttpTaskServer.taskManager.getAllEpic().contains(epic)) {
                    sendHasInteractions(httpExchange, "Данная задача уже существует!");
                } else {
                    HttpTaskServer.taskManager.createEpic(epic);
                    HttpTaskServer.searchMaxIdCounter();
                    sendText(httpExchange, "Задача_создана: \n" + gson.toJson(epic));
                }

                break;
            case "DELETE":
                try {
                    int id = Integer.parseInt(elementsURI[2]);
                    if (HttpTaskServer.taskManager.getEpicById(id) != null) {
                        HttpTaskServer.taskManager.deleteEpicById(id);
                        sendText(httpExchange, "Задача с Id = " + id + " успешно удалена!");
                    } else {
                        sendNotFound(httpExchange, "Задачи с ID = " + id + " не существует");
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}
