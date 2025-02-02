package server;

import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ServerHistoryTests {

    TaskManager manager = Managers.getInMemoryTaskManager();
    private final HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    private final Gson gson = HttpTaskServer.getGson();
    private final Task task = new Task("Test 1", "Testing task 1",
            Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
    private final HttpClient client = HttpClient.newHttpClient();
    Epic epic = new Epic("testEpic", "testEpic");

    @BeforeEach
    public void beforeEach() {
        httpTaskServer.startServer();
        manager.removeAllEpic();
        manager.removeAllTask();
        manager.removeAllSubTask();
        manager.setIdCounter(0);
        epic.setIdTask(1);
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stopServer();
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        String taskJson = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/tasks/0");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().GET().uri(uri1).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());

        URI uri3 = URI.create("http://localhost:8080/epics");
        String taskJson2 = gson.toJson(epic);
        HttpRequest httpRequest3 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson2)).uri(uri3).build();
        HttpResponse<String> response3 = client.send(httpRequest3, HttpResponse.BodyHandlers.ofString());

        URI uri4 = URI.create("http://localhost:8080/epics/1");
        HttpRequest httpRequest4 = HttpRequest.newBuilder().GET().uri(uri4).build();
        HttpResponse<String> response4 = client.send(httpRequest4, HttpResponse.BodyHandlers.ofString());


        URI uri2 = URI.create("http://localhost:8080/history");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().GET().uri(uri2).build();
        HttpResponse<String> response2 = client.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response2.statusCode());
        List<Task> checkTask = gson.fromJson(response2.body(), new TaskTypeToken().getType());
        List<Epic> checkTask1 = gson.fromJson(response2.body(), new EpicTypeToken().getType());
        Task task1 = checkTask.getFirst();
        Epic epic1 = checkTask1.get(1);
        Assertions.assertEquals(task, task1);
        Assertions.assertEquals(epic, epic1);

    }
}
