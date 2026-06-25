package entities;

import java.time.LocalDate;

import services.InterestRate;
import services.InterestRateBrazil;

public class SavingsAccount extends Account {

    private LocalDate interestRateDate;

    private InterestRate interestRate;

    public SavingsAccount(User user) {
        super(user);
        this.interestRateDate = getCreationDate();
        this.interestRate = new InterestRateBrazil();
    }

    public LocalDate getInterestRateDate() {
        return interestRateDate;
    }

    public void setInterestRateDate(LocalDate newDate) {
        this.interestRateDate = newDate;
    }

    public void processInterestRate(Account account) {
        Double interest = interestRate.interestCalculate(account, LocalDate.now());

        if (interest != null) {
            interestCredit(interest);
        }
    }

}
