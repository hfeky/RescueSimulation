package model.units;

import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

    public FireTruck(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
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
