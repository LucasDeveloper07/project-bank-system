package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import dao.CurrentAccountDAO;
import dao.db.DAOFactory;

public class CurrentAccount extends Account {

    private static final Double maintenanceFee = 29.90;

    private LocalDate maintenanceDate;

    // Construtor para criar uma nova CurrentAccount
    public CurrentAccount(Integer transferKey) {
        super(transferKey);
        this.maintenanceDate = getCreationDate();
    }

    // Construtor para instanciar uma CurrentAccount a partir do login do usuário
    public CurrentAccount(String num, String agencyNum, Double balance, LocalDate creationDate, Integer transferKey, LocalDate maintenanceDate) {
        super(num, agencyNum, balance, creationDate, transferKey);
        this.maintenanceDate = maintenanceDate;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    // Método para alterar a data da última taxa de manutenção cobrada
    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;

        // Chamada da classe DAO para fazer o update da data de manutenção no banco de dados
        CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
        currentDao.updateMaintenanceDate(maintenanceDate, getNum());
    }

    // Método para realizar o desconto da taxa de manutenção
    public void maintenanceDiscount(LocalDate dateNow) {
        long days = ChronoUnit.DAYS.between(maintenanceDate, dateNow); // Comparação em dias das datas
        double verifBalance = getBalance(); // Variável para verificar se o desconto foi realizado

        // Se houver 30 dias ou mais desde a última taxa cobrada, será cobrada uma nova taxa
        if (days >= 30) {
            maintenanceDisc(maintenanceFee); // Chamada do método de desconto
            
            // Se a taxa foi descontada do saldo, ele chama o método para atualizar a data de manutenção
            if (verifBalance != getBalance()) {
                setMaintenanceDate(dateNow);
            }
        }
    }
}
