package entities;

import java.time.LocalDate;

import dao.SavingsAccountDAO;
import dao.db.DAOFactory;
import services.InterestRate;
import services.InterestRateBrazil;

public class SavingsAccount extends Account {

    private LocalDate interestRateDate;

    private InterestRate interestRate;

    // Construtor para criar uma nova SavingsAccount
    public SavingsAccount(Integer transferKey) {
        super(transferKey);
        this.interestRateDate = getCreationDate();
        this.interestRate = new InterestRateBrazil(); // Instância da classe InterestRateBrazil
    }

    // Construtor para instanciar uma SavingsAccount a partir do login do usuário
    public SavingsAccount(String num, String agencyNum, Double balance, LocalDate creationDate, Integer transferKey, LocalDate interestRateDate) {
        super(num, agencyNum, balance, creationDate, transferKey);
        this.interestRateDate = interestRateDate;
        this.interestRate = new InterestRateBrazil(); // Instância da classe InterestRateBrazil
    }

    public LocalDate getInterestRateDate() {
        return interestRateDate;
    }

    // Método para alterar a data da última taxa de juros creditada
    public void setInterestRateDate(LocalDate newDate) {
        this.interestRateDate = newDate;

        // Chamada da classe DAO para fazer o update da data de crédito no banco de dados
        SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();
        savingsDao.updateInterestDate(this);
    }

    // Método para realizar o crédito da taxa de juros na conta
    public void processInterestRate(SavingsAccount savingsAccount, LocalDate dateNow) {
        Double interest = interestRate.interestCalculate(savingsAccount, dateNow); // Chamada do método de cálculo da taxa de juros
        double verifBalance = getBalance(); // Variável para verificar o saldo da conta

        // Verificação para saber se tem taxa a ser creditada
        if (interest != null) {
            interestCredit(interest);

            // Se a taxa foi creditada no saldo, ele chama o método para atualizar a data de crédito
            if (verifBalance != getBalance()) {
                setInterestRateDate(dateNow);
            }
        }
    }

}
