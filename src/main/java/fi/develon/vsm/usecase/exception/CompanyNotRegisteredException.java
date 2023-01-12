package fi.develon.vsm.usecase.exception;

public class CompanyNotRegisteredException extends ServiceException {
    public CompanyNotRegisteredException(String message, int status) {
        super(message, status);
    }
}
