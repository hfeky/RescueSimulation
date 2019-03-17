package model.units;

import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {
    @Override
    public void cycleStep() {

    }

    @Override
    public void receiveSOSCall(Rescuable r) {

    }

    @Override
    public void respond(Rescuable r) {

    }

    public Ambulance(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }
}
