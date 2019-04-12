package model.disasters;

import exceptions.DisasterException;
import model.people.Citizen;

public class Infection extends Disaster {

    private Citizen citizen;

    public Infection(int cycle, Citizen target) {
        super(cycle, target);
        this.citizen = target;
    }

    @Override
    public void cycleStep() {
        citizen.setToxicity(citizen.getToxicity() + 15);
    }

    @Override
    public void strike() throws DisasterException {
        super.strike();
        citizen.setToxicity(citizen.getToxicity() + 25);
    }
}
