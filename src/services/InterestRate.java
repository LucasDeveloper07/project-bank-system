package services;

import java.time.LocalDate;

import entities.Account;

public interface InterestRate {

    Double interestCalculate(Account account, LocalDate dateNow);
}
