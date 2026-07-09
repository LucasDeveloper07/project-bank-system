package dao;

import java.time.LocalDate;

import entities.Account;
import entities.CurrentAccount;

public interface CurrentAccountDAO {

    void insert(CurrentAccount currentAccount);
    void updateBalance(CurrentAccount currentAccount);
    void updateMaintenanceDate(LocalDate date, String num);
    CurrentAccount findByUserId(int id);
    CurrentAccount findByTransferKey(int transferKey);
    CurrentAccount findByNum(String num);
    void transfer(CurrentAccount originAccount, Account destinationAccount, double value);
}
