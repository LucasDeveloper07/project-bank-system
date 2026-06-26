package dao;

import entities.SavingsAccount;

public interface SavingsAccountDAO {

    void insert(SavingsAccount savingsAccount);
    void update(SavingsAccount savingsAccount, String passkey);
    void delete(SavingsAccount savingsAccount, String passkey);
    SavingsAccount findByTransferKey(String transferKey);
}
