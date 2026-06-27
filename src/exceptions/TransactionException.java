package exceptions;

public class TransactionException extends RuntimeException {

    public TransactionException(String msg) {
        super(msg);
    }
}
