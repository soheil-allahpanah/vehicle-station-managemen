package fi.develon.vsm.usecase.exception;

public class DuplicateStationByNameException extends ServiceException {
    public DuplicateStationByNameException(String message, int status) {
        super(message, status);
    }
}
