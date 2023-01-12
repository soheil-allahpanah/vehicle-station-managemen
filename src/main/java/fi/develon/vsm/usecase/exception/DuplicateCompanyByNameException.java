package fi.develon.vsm.usecase.exception;

public class DuplicateCompanyByNameException extends ServiceException {
    public DuplicateCompanyByNameException(String message, int status) {
        super(message, status);
    }
}
