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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ServerSubTasksTests {
    TaskManager manager = Managers.getInMemoryTaskManager();
    private final HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    private final Gson gson = HttpTaskServer.getGson();
    Epic epic = new Epic("testEpic", "testEpic");
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void beforeEach() {
        httpTaskServer.startServer();
        manager.removeAllEpic();
        manager.removeAllTask();
        manager.removeAllSubTask();
        manager.setIdCounter(0);
        epic = new Epic("testEpic", "testEpic");
        manager.createEpic(epic);
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stopServer();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(subtask, manager.getAllSubTask().getFirst());
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/subtasks/0");
        Subtask subtask1 = new Subtask("subtask1 update", "subtask1 update", Status.IN_PROGRESS,
                epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson1 = gson.toJson(subtask1);
        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri1).POST(HttpRequest.BodyPublishers.ofString(subtaskJson1)).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response1.statusCode());
        Subtask subtask2 = gson.fromJson(response1.body(), Subtask.class);
        Assertions.assertEquals(subtask1, subtask2);
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        List<Subtask> checkSubTask = gson.fromJson(response1.body(), new SubTaskTypeToken().getType());
        Subtask subtask1 = checkSubTask.getFirst();
        Assertions.assertEquals(subtask, subtask1);
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        URI uri1 = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri1).GET().build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        Subtask subtask1 = gson.fromJson(response1.body(), Subtask.class);
        Assertions.assertEquals(subtask, subtask1);
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri1).DELETE().build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    public void testFailedDeleteSubTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());

    }

    @Test
    public void testFailedGetSubTaskOnId() throws IOException, InterruptedException {
        URI uri= URI.create("http://localhost:8080/subtasks/0");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response2 = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response2.statusCode());
    }

    @Test
    public void testFailedUpdateSubTaskWhichNotExist() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/0");
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void testFailedAddSameSubTask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask1", "subtask1", Status.NEW, epic.getIdTask(), Duration.ofMinutes(15),
                LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response2 = client.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(406, response2.statusCode());
    }
}
