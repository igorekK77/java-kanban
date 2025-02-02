package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;


public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Gson gson = HttpTaskServer.getGson();
        sendText(httpExchange, gson.toJson(taskManager.getPrioritizedTasks()));
    }
}
