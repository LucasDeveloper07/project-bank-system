package entities;

import java.time.LocalDate;

public class ClientPj extends User {

    private String cnpj; 

    public ClientPj(String name, String email, String passkey, LocalDate birthDate, long cnpj) {
        super(name, email, passkey, birthDate);
        this.cnpj = formatCnpj(cnpj);
    }

    public String getCnpj() {
        return cnpj;
    }

    private String formatCnpj(long cnpj) {
        String formatedCnpj = String.format("%014d", cnpj);

        return formatedCnpj;
    }
}
