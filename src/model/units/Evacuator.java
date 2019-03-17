package model.units;

import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

    public Evacuator(String id, Address location, int stepsPerCycle, int maxCapacity) {
        super(id, location, stepsPerCycle, maxCapacity);
    }

    @Override
    public void cycleStep() {

    }

    @Override
    public void receiveSOSCall(Rescuable r) {

    }

    @Override
    public void respond(Rescuable r) {

    }
}
