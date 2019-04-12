package model.disasters;

import exceptions.DisasterException;
import model.people.Citizen;

public class Injury extends Disaster {

    private Citizen citizen;

    public Injury(int cycle, Citizen target) {
        super(cycle, target);
        this.citizen = target;
    }

    @Override
    public void cycleStep() {
        citizen.setBloodLoss(citizen.getBloodLoss() + 10);
    }

    @Override
    public void strike() throws DisasterException {
        super.strike();
        citizen.setBloodLoss(citizen.getBloodLoss() + 30);
    }
}
