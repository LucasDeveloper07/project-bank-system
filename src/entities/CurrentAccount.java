package entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import dao.CurrentAccountDAO;
import dao.db.DAOFactory;

public class CurrentAccount extends Account {

    private static final Double maintenanceFee = 29.90;

    private LocalDate maintenanceDate;

    public CurrentAccount(Integer transferKey) {
        super(transferKey);
        this.maintenanceDate = getCreationDate();
    }

    public CurrentAccount(String num, String agencyNum, Double balance, LocalDate creationDate, Integer transferKey, LocalDate maintenanceDate) {
        super(num, agencyNum, balance, creationDate, transferKey);
        this.maintenanceDate = maintenanceDate;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;

        CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
        currentDao.updateMaintenanceDate(maintenanceDate, getNum());
    }

    public void maintenanceDiscount(LocalDate dateNow) {
        long days = ChronoUnit.DAYS.between(maintenanceDate, dateNow);

        if (days >= 30) {
            maintenanceDisc(maintenanceFee);
            setMaintenanceDate(dateNow);
        }
    }
}
