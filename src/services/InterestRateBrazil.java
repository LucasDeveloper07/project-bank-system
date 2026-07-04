package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import entities.SavingsAccount;

public class InterestRateBrazil implements InterestRate {

    private static final double INTEREST = 0.01;

    @Override
    public Double interestCalculate(SavingsAccount savingsAccount, LocalDate dateNow) {
        long days = ChronoUnit.DAYS.between(savingsAccount.getInterestRateDate(), dateNow);

        if (days >= 30 && savingsAccount.getBalance() > 0) {
            return savingsAccount.getBalance() * INTEREST;
        }

        return null;
    }
}
