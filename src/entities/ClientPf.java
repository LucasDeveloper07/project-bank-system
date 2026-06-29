package entities;

import java.time.LocalDate;

public class ClientPf extends User {

    private String cpf;

    private Account account;

    public ClientPf(String name, String email, String passkey, LocalDate birthDate, long cpf, String typeAccount, Integer transferKey) {
        super(name, email, passkey, birthDate);
        this.cpf = formatCpf(cpf);

        if (typeAccount.equals("CURRENT")) {
            this.account = new CurrentAccount(transferKey);
            account.setUser(this);
            
        } else if (typeAccount.equals("SAVINGS")) {
            this.account = new SavingsAccount(transferKey);
            account.setUser(this);
        }
    }

    public ClientPf(String name, String email, String passkey, LocalDate birthDate, long cpf, Account account) {
        super(name, email, passkey, birthDate);
        this.cpf = formatCpf(cpf);
        this.account = account;
    }

    public String getCpf() {
        return cpf;
    }

    public Account getAccount() {
        return account;
    }

    private String formatCpf(long cpf) {
        String formatedCpf = String.format("%011d", cpf);

        return formatedCpf;
    }
}
