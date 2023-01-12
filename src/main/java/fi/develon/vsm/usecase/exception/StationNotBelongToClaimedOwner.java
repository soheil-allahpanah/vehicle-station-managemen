package fi.develon.vsm.usecase.exception;

public class StationNotBelongToClaimedOwner extends ServiceException {
    public StationNotBelongToClaimedOwner(String message, int status) {
        super(message, status);
    }
}
