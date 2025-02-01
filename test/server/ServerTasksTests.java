package server;

import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.*;
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

public class ServerTasksTests {
    TaskManager manager = Managers.getInMemoryTaskManager();
    private final HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    private final Gson gson = HttpTaskServer.getGson();
    private final Task task = new Task("Test 1", "Testing task 1",
            Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void beforeEach() {
        httpTaskServer.startServer();
        manager.removeAllEpic();
        manager.removeAllTask();
        manager.removeAllSubTask();
        manager.setIdCounter(0);
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stopServer();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        String taskJson = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(task, manager.getAllTask().getFirst());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        URI uri1 = URI.create("http://localhost:8080/tasks");
        String taskJson = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri1).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Task task1 = new Task("Test 1 update", "Testing task 1 update",
                Status.IN_PROGRESS, Duration.ofMinutes(12), LocalDateTime.now());
        URI uri2 = URI.create("http://localhost:8080/tasks/0");
        String taskJson1 = gson.toJson(task1);
        HttpRequest httpRequest1 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson1)).uri(uri2).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response1.statusCode());
        Task task2 = gson.fromJson(response1.body(), Task.class);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<Task> checkTask = gson.fromJson(response.body(), new TaskTypeToken().getType());
        Task task1 = checkTask.getFirst();
        Assertions.assertEquals(task, task1);
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/tasks/0");
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri1).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Task checkTask = gson.fromJson(response.body(), Task.class);
        Assertions.assertEquals(task, checkTask);
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/tasks/0");
        HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(uri1).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(0, manager.getAllTask().size());
    }

    @Test
    public void testFailedTasks() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks/0");
        HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());

        HttpRequest httpRequest1 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response1.statusCode());

        HttpRequest httpRequest2 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response2 = client.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response2.statusCode());

        URI uri1 = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest3 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri1).build();
        HttpResponse<String> response3 = client.send(httpRequest3, HttpResponse.BodyHandlers.ofString());
        HttpRequest httpRequest4 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri1).build();
        HttpResponse<String> response4 = client.send(httpRequest4, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(406, response4.statusCode());
    }
}
