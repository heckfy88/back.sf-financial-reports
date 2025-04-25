package sf.financialreports.api.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message + " not found.");
    }
}
