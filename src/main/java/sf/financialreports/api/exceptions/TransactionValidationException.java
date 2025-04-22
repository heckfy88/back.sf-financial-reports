package sf.financialreports.api.exceptions;

public class TransactionValidationException extends RuntimeException {
    public TransactionValidationException(String message) {
        super("Transaction is not valid: " + message);
    }
}
