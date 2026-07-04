package dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.CurrentAccountDAO;
import dao.SavingsAccountDAO;
import dao.TransactionDAO;
import dao.db.DAOFactory;
import dao.db.DB;
import dao.db.DbException;
import entities.Account;
import entities.Transaction;
import enums.TransactionType;

public class TransactionDAOJDBC implements TransactionDAO {

    private Connection conn = null;

    public TransactionDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Transaction transaction) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("INSERT INTO transaction "
                + "(type, value, date, origin_account, destination_account) "
                + "VALUES "
                + "(?, ?, ?, ?, ?)");

            if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
                st.setString(1, "DEPOSIT");
                st.setDouble(2, transaction.getValue());
                st.setTimestamp(3, java.sql.Timestamp.valueOf(transaction.getDate()));
                st.setInt(4, transaction.getOriginAccount().getUser().getId());
                st.setNull(5, java.sql.Types.INTEGER);

            } else if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                st.setString(1, "WITHDRAW");
                st.setDouble(2, transaction.getValue());
                st.setTimestamp(3, java.sql.Timestamp.valueOf(transaction.getDate()));
                st.setInt(4, transaction.getOriginAccount().getUser().getId());
                st.setNull(5, java.sql.Types.INTEGER);

            } else if (transaction.getTransactionType() == TransactionType.TRANSFER) {
                st.setString(1, "TRANSFER");
                st.setDouble(2, transaction.getValue());
                st.setTimestamp(3, java.sql.Timestamp.valueOf(transaction.getDate()));
                st.setInt(4, transaction.getOriginAccount().getUser().getId());
                st.setInt(5, transaction.getDestinationAccount().getUser().getId());

            } else if (transaction.getTransactionType() == TransactionType.MAINTENANCE_FEE) {
                st.setString(1, "MAINTENANCE_FEE");
                st.setDouble(2, transaction.getValue());
                st.setTimestamp(3, java.sql.Timestamp.valueOf(transaction.getDate()));
                st.setInt(4, transaction.getOriginAccount().getUser().getId());
                st.setNull(5, java.sql.Types.INTEGER);

            } else if (transaction.getTransactionType() == TransactionType.INTEREST_CREDIT) {
                st.setString(1, "INTEREST_CREDIT");
                st.setDouble(2, transaction.getValue());
                st.setTimestamp(3, java.sql.Timestamp.valueOf(transaction.getDate()));
                st.setInt(4, transaction.getOriginAccount().getUser().getId());
                st.setNull(5, java.sql.Types.INTEGER);
            }

            int rowsAffect = st.executeUpdate();

            if (rowsAffect == 0) {
                throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Transaction> findAll(int id) {
        List<Transaction> transactions = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM transaction "
                + "WHERE origin_account = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            while (rs.next()) {
                Transaction transaction = createTransaction(rs);
                transactions.add(transaction);
            }

            return transactions;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Transaction createTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = null;

        String type = rs.getString(2);

        if (type.equals("DEPOSIT")) {
            double value = rs.getDouble(3);
            LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();

            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

            Account currentAccount = null;
            Account savingsAccount = null; 
            currentAccount = currentDao.findByNum(rs.getString(5));
            savingsAccount = savingsDao.findByNum(rs.getString(5));

            if (currentAccount != null) {
                transaction = new Transaction(TransactionType.DEPOSIT, value, date, currentAccount, null);
                transaction.setId(rs.getInt(1));
            } else {
                transaction = new Transaction(TransactionType.DEPOSIT, value, date, savingsAccount, null);
                transaction.setId(rs.getInt(1));
            }
        } else if (type.equals("WITHDRAW")) {
            double value = rs.getDouble(3);
            LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();

            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

            Account currentAccount = null;
            Account savingsAccount = null; 
            currentAccount = currentDao.findByNum(rs.getString(5));
            savingsAccount = savingsDao.findByNum(rs.getString(5));

            if (currentAccount != null) {
                transaction = new Transaction(TransactionType.WITHDRAW, value, date, currentAccount, null);
                transaction.setId(rs.getInt(1));
            } else {
                transaction = new Transaction(TransactionType.WITHDRAW, value, date, savingsAccount, null);
                transaction.setId(rs.getInt(1));
            }
        } else if (type.equals("TRANSFER")) {
            double value = rs.getDouble(3);
            LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();

            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

            Account currentAccountOrigin = null;
            Account savingsAccountOrigin = null;
            Account currentAccountDestination = null;
            Account savingsAccountDestination = null; 

            currentAccountOrigin = currentDao.findByUserId(rs.getInt(5));
            savingsAccountOrigin = savingsDao.findByUserId(rs.getInt(5));
            currentAccountDestination = currentDao.findByUserId(rs.getInt(6));
            savingsAccountDestination = savingsDao.findByUserId(rs.getInt(6));

            if (currentAccountOrigin != null) {
                if (currentAccountDestination != null) {
                    transaction = new Transaction(TransactionType.TRANSFER, value, date, currentAccountOrigin, currentAccountDestination);
                    transaction.setId(rs.getInt(1));
                } else if (savingsAccountDestination != null) {
                    transaction = new Transaction(TransactionType.TRANSFER, value, date, currentAccountOrigin, savingsAccountDestination);
                    transaction.setId(rs.getInt(1));
                }
            } else if (savingsAccountOrigin != null) {
                if (currentAccountDestination != null) {
                    transaction = new Transaction(TransactionType.TRANSFER, value, date, savingsAccountOrigin, currentAccountDestination);
                    transaction.setId(rs.getInt(1));
                } else if (savingsAccountDestination != null) {
                    transaction = new Transaction(TransactionType.TRANSFER, value, date, savingsAccountOrigin, savingsAccountDestination);
                    transaction.setId(rs.getInt(1));
                }
            }
        } else if (type.equals("INTEREST_CREDIT")) {
            double value = rs.getDouble(3);
            LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();

            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

            Account currentAccount = null;
            Account savingsAccount = null; 
            currentAccount = currentDao.findByNum(rs.getString(5));
            savingsAccount = savingsDao.findByNum(rs.getString(5));

            if (currentAccount != null) {
                transaction = new Transaction(TransactionType.INTEREST_CREDIT, value, date, currentAccount, null);
                transaction.setId(rs.getInt(1));
            } else {
                transaction = new Transaction(TransactionType.INTEREST_CREDIT, value, date, savingsAccount, null);
                transaction.setId(rs.getInt(1));
            }
        } else if (type.equals("MAINTENANCE_FEE")) {
            double value = rs.getDouble(3);
            LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();

            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

            Account currentAccount = null;
            Account savingsAccount = null; 
            currentAccount = currentDao.findByNum(rs.getString(5));
            savingsAccount = savingsDao.findByNum(rs.getString(5));

            if (currentAccount != null) {
                transaction = new Transaction(TransactionType.MAINTENANCE_FEE, value, date, currentAccount, null);
                transaction.setId(rs.getInt(1));
            } else {
                transaction = new Transaction(TransactionType.MAINTENANCE_FEE, value, date, savingsAccount, null);
                transaction.setId(rs.getInt(1));
            }
        }

        return transaction;
    }
}
