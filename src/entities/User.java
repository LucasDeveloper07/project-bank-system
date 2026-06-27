package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class User {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;

    public User(String name, String email, String password, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.password = String.valueOf(password.hashCode());
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void modifiedPassword(String newPassword, String passwordVerified) {

    }

    public void modifiedName(String passwordVerified, String newName) {

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
