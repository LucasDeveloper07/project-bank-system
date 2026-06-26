package dao;

import java.util.List;

import entities.Transaction;

public interface TransactionDAO {

    void insert(Transaction transaction);
    Transaction findById(int id);
    List<Transaction> findAll();
}
