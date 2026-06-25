package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class User {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String name;
    private String email;
    private String passkey;
    private LocalDate birthDate;

    public User(String name, String email, String passkey, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.passkey = passkey;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void modifiedPasskey(String newPasskey, String passkeyVerified) {

    }

    public void modifiedName(String passkeyVerified, String newName) {

    }

    public String viewDataUser() {
        StringBuilder sb = new StringBuilder();

        sb.append("=====DADOS DO USUARIO=====\n");
        sb.append("Nome: " + name + "\n");
        sb.append("Email: " + email + "\n");
        sb.append("Data de nascimento: " + birthDate.format(fmt));

        return sb.toString();
    }
}
