package dao;

import entities.User;

public interface UserDAO {

    void insert(User user);
    void updatePassword(User user);
    void updateName(User user);
    void delete(User user);
    User login(String cpf_cnpj, String email, String password);
}
