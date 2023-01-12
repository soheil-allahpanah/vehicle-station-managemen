package fi.develon.vsm.usecase.exception;

public class ServiceException extends RuntimeException {
    private final int status;

    public ServiceException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
