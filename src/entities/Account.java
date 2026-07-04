package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dao.CurrentAccountDAO;
import dao.SavingsAccountDAO;
import dao.TransactionDAO;
import dao.db.DAOFactory;
import enums.TransactionType;
import exceptions.TransactionException;

public abstract class Account {

    private String num;
    private String agencyNum;
    private Double balance;
    private LocalDate creationDate;
    private Integer transferKey;

    private User user;
    private List<Transaction> transactions = new ArrayList<>();
    
    // Construtor para criar uma nova account
    public Account(Integer transferKey) {
        this.num = generateNum();
        this.agencyNum = generateAgencyNum();
        this.balance = 0.0;
        this.creationDate = LocalDate.now();
        this.transferKey = transferKey;
    }

    // Construtor para instanciar uma account a partir do login
    public Account(String num, String agencyNum, Double balance, LocalDate creationDate, Integer transferKey) {
        this.num = num;
        this.agencyNum = agencyNum;
        this.balance = balance;
        this.creationDate = creationDate;
        this.transferKey = transferKey;
    }

    public String getNum() {
        return num;
    }

    public String getAgencyNum() {
        return agencyNum;
    }

    public void setAgencyNum(String agencyNum) {
        this.agencyNum = agencyNum;
    }

    public Double getBalance() {
        return balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Integer getTransferKey() {
        return transferKey;
    }

    public void setTransferKey(Integer transferKey) {
        this.transferKey = transferKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void loadTransactions() {
        TransactionDAO transactionDao = DAOFactory.createTransactionDAO();

        this.transactions = transactionDao.findAll(getUser().getId());
    }

    // Método para realizar o depósito em conta
    public void deposit(double value, String password) {
        // Verificação de senha do usuário
        if (!password.equals(getUser().getPassword())) {
            throw new TransactionException("Senha incorreta!");
        }

        balance += value;

        // Chamada da classe DAO para realizar a operação no banco de dados
        if (this instanceof CurrentAccount currentAccount) {
            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            currentDao.updateBalance(currentAccount);

            Transaction transaction = new Transaction(TransactionType.DEPOSIT, value, LocalDateTime.now(), currentAccount, null);
            addTransaction(transaction);

            TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
            transactionDao.insert(transaction);
        } else if (this instanceof SavingsAccount savingsAccount) {
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();
            savingsDao.updateBalance(savingsAccount);

            Transaction transaction = new Transaction(TransactionType.DEPOSIT, value, LocalDateTime.now(), savingsAccount, null);
            addTransaction(transaction);

            TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
            transactionDao.insert(transaction);
        }
    }

    // Método para realizar o saque em conta
    public void withdraw(double value, String password) {
        // Verificação de senha do usuário
        if (!password.equals(getUser().getPassword())) {
            throw new TransactionException("Senha incorreta!");
        }

        if (balance < value) {
            throw new TransactionException("Saldo insuficiente");
        }

        balance -= value;

        // Chamada da classe DAO para realizar a operação no banco de dados
        if (this instanceof CurrentAccount currentAccount) {
            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            currentDao.updateBalance(currentAccount);

            Transaction transaction = new Transaction(TransactionType.WITHDRAW, value, LocalDateTime.now(), currentAccount, null);
            addTransaction(transaction);    

            TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
            transactionDao.insert(transaction);
        } else if (this instanceof SavingsAccount savingsAccount) {
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();
            savingsDao.updateBalance(savingsAccount);

            Transaction transaction = new Transaction(TransactionType.WITHDRAW, value, LocalDateTime.now(), savingsAccount, null);
            addTransaction(transaction);    

            TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
            transactionDao.insert(transaction);
        }
    }

    // Método para realizar transferências entre contas
    public void transfer(double value, String password, int transferKey) {
        // Verificação de senha do usuário
        if (!password.equals(getUser().getPassword())) {
            throw new TransactionException("Senha incorreta!");
        }

        if (balance < value) {
            throw new TransactionException("Saldo insuficiente");
        }

        // Chamada da classe DAO para realizar a operação no banco de dados
        CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
        SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

        if (this instanceof CurrentAccount currentAccount) {
            CurrentAccount currentAccountTransf = currentDao.findByTransferKey(transferKey);
            SavingsAccount savingsAccountTransf = savingsDao.findByTransferKey(transferKey);

            // Verificação para realizar a transferência na conta certa
            if (currentAccountTransf != null) {
                currentDao.transfer(currentAccount, currentAccountTransf, value);
                balance -= value;

                Transaction transaction = new Transaction(TransactionType.TRANSFER, value, LocalDateTime.now(), currentAccount, currentAccountTransf);
                addTransaction(transaction);    

                TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
                transactionDao.insert(transaction);

                System.out.println("Transferência realizada com sucesso!");
            } else if (savingsAccountTransf != null) {
                currentDao.transfer(currentAccount, savingsAccountTransf, value);
                balance -= value;

                Transaction transaction = new Transaction(TransactionType.TRANSFER, value, LocalDateTime.now(), currentAccount, savingsAccountTransf);
                addTransaction(transaction);    

                TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
                transactionDao.insert(transaction);

                System.out.println("Transferência realizada com sucesso!");
            } else {
                throw new TransactionException("Conta não encontrada!");
            }
        } else if (this instanceof SavingsAccount savingsAccount) {
            CurrentAccount currentAccountTransf = currentDao.findByTransferKey(transferKey);
            SavingsAccount savingsAccountTransf = savingsDao.findByTransferKey(transferKey);

            if (currentAccountTransf != null) {
                savingsDao.transfer(savingsAccount, currentAccountTransf, value);
                balance -= value;

                Transaction transaction = new Transaction(TransactionType.TRANSFER, value, LocalDateTime.now(), savingsAccount, currentAccountTransf);
                addTransaction(transaction);    

                TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
                transactionDao.insert(transaction);

                System.out.println("Transferência realizada com sucesso!");
            } else if (savingsAccountTransf != null) {
                savingsDao.transfer(savingsAccount, savingsAccountTransf, value);
                balance -= value;

                Transaction transaction = new Transaction(TransactionType.TRANSFER, value, LocalDateTime.now(), savingsAccount, savingsAccountTransf);
                addTransaction(transaction);    

                TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
                transactionDao.insert(transaction);

                System.out.println("Transferência realizada com sucesso!");
            } else {
                throw new TransactionException("Conta não encontrada!");
            }
        }
    }

    // Método para o usuário visualizar os dados da conta
    public String viewDataAccount() {
        StringBuilder sb = new StringBuilder();

        sb.append("=====DADOS DA CONTA=====\n");
        sb.append("Numero: " + num.replaceAll("(\\d{7})(\\d{1})", "$1-$2"));
        sb.append("\nAgência: " + agencyNum.replaceAll("(\\d{4})(\\d{1})", "$1-$2"));
        sb.append("\nData de abertura: " + creationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        if (transferKey != null) {
            sb.append("\nChave de transferência: " + transferKey);
        }

        return sb.toString();
    }

    // Método para realizar o desconto de manutenção na conta
    protected void maintenanceDisc(double value) {
        if (balance >= value) {
            balance -= value;

            // Chamada da classe DAO para realizar a operação no banco de dados
            if (this instanceof CurrentAccount currentAccount) {
                CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
                currentDao.updateBalance(currentAccount);

                Transaction transaction = new Transaction(TransactionType.MAINTENANCE_FEE, value, LocalDateTime.now(), currentAccount, null);
                addTransaction(transaction);    

                TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
                transactionDao.insert(transaction);
            } else if (this instanceof SavingsAccount savingsAccount) {
                SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();
                savingsDao.updateBalance(savingsAccount);

                Transaction transaction = new Transaction(TransactionType.MAINTENANCE_FEE, value, LocalDateTime.now(), savingsAccount, null);
                addTransaction(transaction);    

                TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
                transactionDao.insert(transaction);
            }
        }
    }

    // Método para creditar a taxa de juros no saldo da conta
    protected void interestCredit(double value) {
        balance += value;

        // Chamada da classe DAO para realizar a operação no banco de dados
        if (this instanceof CurrentAccount currentAccount) {
            CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
            currentDao.updateBalance(currentAccount);

            Transaction transaction = new Transaction(TransactionType.INTEREST_CREDIT, value, LocalDateTime.now(), currentAccount, null);
            addTransaction(transaction);    

            TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
            transactionDao.insert(transaction);
        } else if (this instanceof SavingsAccount savingsAccount) {
            SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();
            savingsDao.updateBalance(savingsAccount);

            Transaction transaction = new Transaction(TransactionType.INTEREST_CREDIT, value, LocalDateTime.now(), savingsAccount, null);
            addTransaction(transaction);    

            TransactionDAO transactionDao = DAOFactory.createTransactionDAO();
            transactionDao.insert(transaction);
        }
    }

    private String generateNum() {
        Random rm = new Random();
        int num = rm.nextInt(10000000, 99999999);

        String numStr = String.format("%08d", num);
    
        return numStr;
    }

    private String generateAgencyNum() {
        Random rm = new Random();
        int num = rm.nextInt(10000, 99999);

        String numStr = String.format("%05d", num);
    
        return numStr;
    }
}
