package model.units;

import simulation.Address;
import simulation.Rescuable;

public abstract class FireUnit extends Unit {
    @Override
    public void cycleStep() {

    }

    @Override
    public void receiveSOSCall(Rescuable r) {

    }

    @Override
    public void respond(Rescuable r) {

    }

    public FireUnit(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }
}
