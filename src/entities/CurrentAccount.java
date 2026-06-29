package entities;

import java.time.LocalDate;

public class CurrentAccount extends Account {

    private static final Double maintenaceFee = 29.90;

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
    }

    public void maintenanceDiscount(LocalDate dateNow) {
        int days = dateNow.compareTo(maintenanceDate);

        if (days >= 30) {
            maintenanceDisc(maintenaceFee);
            setMaintenanceDate(LocalDate.now());
        }
    }
}
