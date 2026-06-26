package dao;

import entities.CurrentAccount;

public interface CurrentAccountDAO {

    void insert(CurrentAccount currentAccount);
    void update(CurrentAccount currentAccount, String passkey);
    void delete(CurrentAccount currentAccount, String passkey);
    CurrentAccount findByTransferKey(String transferKey);
}
