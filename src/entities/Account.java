package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exceptions.TransactionException;

public abstract class Account {

    private String num;
    private String agencyNum;
    private Double balance;
    private LocalDate creationDate;
    private Integer transferKey;

    private User user;
    private List<Transaction> transactions = new ArrayList<>();
    
    public Account(Integer transferKey) {
        this.num = generateNum();
        this.agencyNum = generateAgencyNum();
        this.balance = 0.0;
        this.creationDate = LocalDate.now();
        this.transferKey = transferKey;
    }

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

    public void deposit(double value, String password) {
        if (!password.equals(getUser().getPassword())) {
            throw new TransactionException("Senha incorreta!");
        }

        balance += value;
    }

    public void withdraw(double value, String password) {
        if (!password.equals(getUser().getPassword())) {
            throw new TransactionException("Senha incorreta!");
        }

        balance -= value;
    }

    public void transfer(double value, String password, int transferKey) {
        
    }

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

    protected void maintenanceDisc(double value) {
        balance -= value;
    }

    protected void interestCredit(double value) {
        balance += value;
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
