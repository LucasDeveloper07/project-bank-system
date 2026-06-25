package entities;

import java.time.LocalDate;

public class ClientPf extends User {

    private String cpf;

    public ClientPf(String name, String email, String passkey, LocalDate birthDate, long cpf) {
        super(name, email, passkey, birthDate);
        this.cpf = formatCpf(cpf);
    }

    public String getCpf() {
        return cpf;
    }

    private String formatCpf(long cpf) {
        String formatedCpf = String.format("%011d", cpf);

        return formatedCpf;
    }
}
