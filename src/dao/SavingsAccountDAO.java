package dao;

import entities.Account;
import entities.SavingsAccount;

public interface SavingsAccountDAO {

    void insert(SavingsAccount savingsAccount);
    void updateBalance(SavingsAccount savingsAccount);
    void updateInterestDate(SavingsAccount savingsAccount);
    void delete(SavingsAccount savingsAccount, String password);
    SavingsAccount findByUserId(int id);
    SavingsAccount findByTransferKey(int transferKey);
    void transfer(SavingsAccount originAccount, Account destinationAccount, double value);
}
