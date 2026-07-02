package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import dao.UserDAO;
import dao.db.DAOFactory;
import exceptions.UserException;

public abstract class User {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;

    private Account account;

    public User(String name, String email, String password, LocalDate birthDate, Account account) {
        this.name = name;
        this.email = email;
        this.password = String.valueOf(password.hashCode());
        this.birthDate = birthDate;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void modifiedPassword(String newPassword, String passwordVerified) {
        if (newPassword.equals(password)) {
            throw new UserException("Você não pode usar a senha atual!");
        } else if (passwordVerified.equals(password)) {
            this.password = newPassword;
            UserDAO userDao = DAOFactory.createUserDAO();
            userDao.updatePassword(this);
        } else {
            throw new UserException("Senha incorreta!");
        }
    }

    public void modifiedName(String passwordVerified, String newName) {
        if (newName.equals(name)) {
            throw new UserException("Você não pode usar o nome atual!");
        } else if (passwordVerified.equals(password)) {
            this.name = newName;
            UserDAO userDao = DAOFactory.createUserDAO();
            userDao.updateName(this);
        }
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
