package entities;

import java.time.LocalDate;

import dao.SavingsAccountDAO;
import dao.db.DAOFactory;
import services.InterestRate;
import services.InterestRateBrazil;

public class SavingsAccount extends Account {

    private LocalDate interestRateDate;

    private InterestRate interestRate;

    public SavingsAccount(Integer transferKey) {
        super(transferKey);
        this.interestRateDate = getCreationDate();
        this.interestRate = new InterestRateBrazil();
    }

    public SavingsAccount(String num, String agencyNum, Double balance, LocalDate creationDate, Integer transferKey, LocalDate interestRateDate) {
        super(num, agencyNum, balance, creationDate, transferKey);
        this.interestRateDate = interestRateDate;
        this.interestRate = new InterestRateBrazil();
    }

    public LocalDate getInterestRateDate() {
        return interestRateDate;
    }

    public void setInterestRateDate(LocalDate newDate) {
        this.interestRateDate = newDate;

        SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();
        savingsDao.updateInterestDate(this);
    }

    public void processInterestRate(SavingsAccount savingsAccount, LocalDate dateNow) {
        Double interest = interestRate.interestCalculate(savingsAccount, dateNow);
        double verifBalance = getBalance();

        if (interest != null) {
            interestCredit(interest);

            if (verifBalance != getBalance()) {
                setInterestRateDate(dateNow);
            }
        }
    }

}
