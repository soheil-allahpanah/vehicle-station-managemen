package fi.develon.vsm.usecase.exception;

public class DuplicateCompanyByIdentifierException extends ServiceException{
    public DuplicateCompanyByIdentifierException(String message, int status) {
        super(message, status);
    }
}
