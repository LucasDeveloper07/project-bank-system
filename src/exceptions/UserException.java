package exceptions;

public class UserException extends RuntimeException {
    
    // Exceção lançada para falhas de autenticação e validação de dados de identidade do usuário

    public UserException(String msg) {
        super(msg);
    }
}
