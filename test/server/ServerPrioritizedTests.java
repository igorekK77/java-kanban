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
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ServerPrioritizedTests {
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
    public void testGetPrioritized() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.of(2023, 2,23,12,45));
        subtask.setIdTask(2);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.of(2020, 7,11,10,15));
        subtask1.setIdTask(3);

        URI uri = URI.create("http://localhost:8080/tasks");
        String taskJson = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/epics");
        String taskJson2 = gson.toJson(epic);
        HttpRequest httpRequest1 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson2)).uri(uri1).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());

        URI uri2 = URI.create("http://localhost:8080/subtasks");
        String subtaskJson = gson.toJson(subtask);
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uri2).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response2 = client.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        String subtaskJson3 = gson.toJson(subtask1);
        HttpRequest httpRequest3 = HttpRequest.newBuilder().uri(uri2).POST(HttpRequest.BodyPublishers.ofString(subtaskJson3)).build();
        HttpResponse<String> response3 = client.send(httpRequest3, HttpResponse.BodyHandlers.ofString());

        URI uri3 = URI.create("http://localhost:8080/prioritized");
        HttpRequest httpRequest4 = HttpRequest.newBuilder().GET().uri(uri3).build();
        HttpResponse<String> response4 = client.send(httpRequest4, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response4.statusCode());
        List<Subtask> checkSubtask = gson.fromJson(response4.body(), new SubTaskTypeToken().getType());
        List<Task> checkTask = gson.fromJson(response4.body(), new TaskTypeToken().getType());
        Assertions.assertEquals(subtask1, checkSubtask.getFirst());
        Assertions.assertEquals(subtask, checkSubtask.get(1));
        Assertions.assertEquals(task, checkTask.get(2));
    }
}
