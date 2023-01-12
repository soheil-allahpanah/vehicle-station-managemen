package fi.develon.vsm.usecase.exception;

public class DuplicateStationByLocationException extends ServiceException{
    public DuplicateStationByLocationException(String message, int status) {
        super(message, status);
    }
}
