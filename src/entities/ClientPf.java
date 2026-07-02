package entities;

import java.time.LocalDate;

import enums.TypeAccount;

public class ClientPf extends User {

    private String cpf;

    public ClientPf(String name, String email, String password, LocalDate birthDate, long cpf, TypeAccount typeAccount, Integer transferKey) {
        super(name, email, password, birthDate, createAccount(typeAccount, transferKey));
        this.cpf = formatCpf(cpf);
        getAccount().setUser(this);
    }

    public ClientPf(String name, String email, String password, LocalDate birthDate, long cpf, Account account) {
        super(name, email, password, birthDate, account);
        this.cpf = formatCpf(cpf);
        getAccount().setUser(this);
    }

    public String getCpf() {
        return cpf;
    }

    private String formatCpf(long cpf) {
        String formatedCpf = String.format("%011d", cpf);

        return formatedCpf;
    }

    private static Account createAccount(TypeAccount typeAccount, Integer transferKey) {
        return switch (typeAccount) {
            case CURRENT -> new CurrentAccount(transferKey);
            case SAVINGS -> new SavingsAccount(transferKey);
        };
    }
}
