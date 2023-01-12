package fi.develon.vsm.usecase.exception;

public class NewParentCompanyNotRegisteredException extends ServiceException {
    public NewParentCompanyNotRegisteredException(String message, int status) {
        super(message, status);
    }
}
