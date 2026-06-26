package dao;

import entities.User;

public interface UserDAO {

    void insert(User user);
    void update(User user, String passkey);
    void delete(User user, String passkey);
    User login(String cpf_cnpj, String email, String passkey);
}
