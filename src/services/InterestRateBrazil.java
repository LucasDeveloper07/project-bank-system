package services;

import java.time.LocalDate;

import entities.Account;

public class InterestRateBrazil implements InterestRate {

    private static final double INTEREST = 0.01;

    @Override
    public Double interestCalculate(Account account, LocalDate dateNow) {
        int days = dateNow.compareTo(account.getCreationDate());

        if (days >= 30) {
            return account.getBalance() * INTEREST;
        }

        return null;
    }
}
