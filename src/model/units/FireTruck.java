package model.units;

import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

    public FireTruck(String id, Address location, int stepsPerCycle, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
    }
}
