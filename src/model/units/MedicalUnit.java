package model.units;

import simulation.Address;
import simulation.Rescuable;

public abstract class MedicalUnit extends Unit {

    private int healingAmount = 10;
    private int treatmentAmount = 10;

    public MedicalUnit(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }

    public abstract void receiveSOSCall(Rescuable r);

    public abstract void respond(Rescuable r);
}
