package entities;

import java.time.LocalDate;

public class CurrentAccount extends Account {

    private static final Double maintenaceFee = 29.90;

    private LocalDate maintenanceDate;

    public CurrentAccount(User user) {
        super(user);
        this.maintenanceDate = getCreationDate();
    }

    public LocalDate getMaintenaceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenaceDate) {
        this.maintenanceDate = maintenaceDate;
    }

    public void maintenanceDiscount(LocalDate dateNow) {
        int days = dateNow.compareTo(maintenanceDate);

        if (days >= 30) {
            maintenanceDisc(maintenaceFee);
            setMaintenanceDate(LocalDate.now());
        }
    }
}
