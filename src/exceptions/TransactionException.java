package exceptions;

public class TransactionException extends RuntimeException {

    /* Exceção lançada para violações de regras de negócio durante uma
    transação bancária — como senha incorreta ou saldo insuficiente */

    public TransactionException(String msg) {
        super(msg);
    }
}
