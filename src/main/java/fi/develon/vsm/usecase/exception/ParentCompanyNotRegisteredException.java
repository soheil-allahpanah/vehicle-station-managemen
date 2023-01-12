package fi.develon.vsm.usecase.exception;

public class ParentCompanyNotRegisteredException extends ServiceException {
    public ParentCompanyNotRegisteredException(String message, int status) {
        super(message, status);
    }
}
