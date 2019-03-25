package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import simulation.Address;

public abstract class MedicalUnit extends Unit {

    private int healingAmount = 10;
    private int treatmentAmount = 10;

    public MedicalUnit(String id, Address location, int stepsPerCycle, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
    }

    public int getTreatmentAmount() {
        return treatmentAmount;
    }

    public void heal() {
        Citizen citizen = (Citizen) getTarget();
        citizen.setHp(citizen.getHp() + healingAmount);
        if (citizen.getHp() == 100) setState(UnitState.IDLE);
    }
}
