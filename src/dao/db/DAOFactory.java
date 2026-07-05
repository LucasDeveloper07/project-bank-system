package dao.db;

import dao.CurrentAccountDAO;
import dao.SavingsAccountDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import dao.daoImpl.CurrentAccountDAOJDBC;
import dao.daoImpl.SavingsAccountDAOJDBC;
import dao.daoImpl.TransactionDAOJDBC;
import dao.daoImpl.UserDAOJDBC;

// Criação das implementações JDBC das interfaces DAO

public class DAOFactory {

    public static UserDAO createUserDAO() {
        return new UserDAOJDBC(DB.getConnection());
    }

    public static CurrentAccountDAO createCurrentAccountDAO() {
        return new CurrentAccountDAOJDBC(DB.getConnection());
    }

    public static SavingsAccountDAO createSavingsAccountDAO() {
        return new SavingsAccountDAOJDBC(DB.getConnection());
    }

    public static TransactionDAO createTransactionDAO() {
        return new TransactionDAOJDBC(DB.getConnection());
    }
}
