package manager;

public class ManagerSaveException extends RuntimeException {

    private String message;

    public ManagerSaveException(String message) {
        this.message = message;
    }

}
