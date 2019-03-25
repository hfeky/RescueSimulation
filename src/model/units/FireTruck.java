package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class FireTruck extends FireUnit {

    public FireTruck(String id, Address location, int stepsPerCycle, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
    }

    @Override
    public void treat() {
        super.treat();
        ResidentialBuilding building = (ResidentialBuilding) getTarget();
        building.setFireDamage(building.getFireDamage() - 10);
        if (building.getFireDamage() == 0) jobsDone();
    }
}
