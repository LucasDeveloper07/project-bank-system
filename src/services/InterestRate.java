package services;

import java.time.LocalDate;

import entities.SavingsAccount;

public interface InterestRate {

    Double interestCalculate(SavingsAccount savingsAccount, LocalDate dateNow);
}
