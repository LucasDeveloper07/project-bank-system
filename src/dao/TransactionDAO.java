package dao;

import java.util.List;

import entities.Transaction;

public interface TransactionDAO {

    void insert(Transaction transaction);
    List<Transaction> findAll(int id);
}
