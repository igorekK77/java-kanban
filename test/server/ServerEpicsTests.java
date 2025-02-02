package server;

import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ServerEpicsTests {
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
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stopServer();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        String epicJson = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(epic, manager.getAllEpic().getFirst());
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        String epicJson = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        List<Epic> checkEpic = gson.fromJson(response1.body(), new EpicTypeToken().getType());
        Epic epic1 = checkEpic.getFirst();
        Assertions.assertEquals(epic, epic1);
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        String epicJson = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/epics/0");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().GET().uri(uri1).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        Epic epic1 = gson.fromJson(response1.body(), Epic.class);
        Assertions.assertEquals(epic, epic1);
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        String epicJson = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uri1 = URI.create("http://localhost:8080/epics/0");
        HttpRequest httpRequest1 = HttpRequest.newBuilder().DELETE().uri(uri1).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(0, manager.getAllEpic().size());
    }

    @Test
    public void testFailedDeleteEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/0");
        HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void testFailedGetEpicOnId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/0");
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void testFailedAddSameEpic() throws IOException, InterruptedException {
        String epicJson = gson.toJson(epic);
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> respons = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response1 = client.send(httpRequest1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(406, response1.statusCode());
    }
}
