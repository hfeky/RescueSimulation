package model.units;

import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public abstract class MedicalUnit extends Unit {

    private int healingAmount = 10;
    private int treatmentAmount = 10;

    public MedicalUnit(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }

    public int getTreatmentAmount() {
        return treatmentAmount;
    }

    public abstract void receiveSOSCall(Rescuable r);

    public abstract void respond(Rescuable r);

    public void heal() {
        Citizen target = (Citizen) getTarget();
        int hp = target.getHp() + healingAmount;
        if (hp > 100) hp = 100;
        target.setHp(hp);
    }
}
