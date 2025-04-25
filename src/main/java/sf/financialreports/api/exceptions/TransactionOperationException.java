package sf.financialreports.api.exceptions;

public class TransactionOperationException extends RuntimeException {
    public TransactionOperationException(String message) {
        super("Unable to process the operation: " +message);
    }
}
