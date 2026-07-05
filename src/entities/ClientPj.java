package entities;

import java.time.LocalDate;

import enums.TypeAccount;

public class ClientPj extends User {

    private String cnpj; 

    public ClientPj(String name, String email, String password, LocalDate birthDate, long cnpj, TypeAccount typeAccount, Integer transferKey) {
        super(name, email, password, birthDate, createAccount(typeAccount, transferKey));
        this.cnpj = formatCnpj(cnpj);
        getAccount().setUser(this);
    }

    public ClientPj(String name, String email, String password, LocalDate birthDate, long cnpj, Account account) {
        super(name, email, password, birthDate, account);
        this.cnpj = formatCnpj(cnpj);
        getAccount().setUser(this);
    }

    public String getCnpj() {
        return cnpj;
    }

    // Método para formatar o CNPJ em String
    private String formatCnpj(long cnpj) {
        String formatedCnpj = String.format("%014d", cnpj);

        return formatedCnpj;
    }

    // Método estático para criar a conta na instância de ClientPj
    private static Account createAccount(TypeAccount typeAccount, Integer transferKey) {
        return switch (typeAccount) {
            case CURRENT -> new CurrentAccount(transferKey);
            case SAVINGS -> new SavingsAccount(transferKey);
        };
    }
}
