package fi.develon.vsm.usecase.exception;

public class StationNotFoundException extends ServiceException{
    public StationNotFoundException(String message, int status) {
        super(message, status);
    }
}
