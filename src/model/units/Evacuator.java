package model.units;

import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

    public Evacuator(String id, Address location, int stepsPerCycle, int maxCapacity, WorldListener worldListener) {
        super(id, location, stepsPerCycle, maxCapacity, worldListener);
    }

    @Override
    public void respond(Rescuable r) {

    }
}
