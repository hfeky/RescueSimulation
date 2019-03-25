package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class GasControlUnit extends FireUnit {

    public GasControlUnit(String id, Address location, int stepsPerCycle, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
    }

    @Override
    public void treat() {
        super.treat();
        ResidentialBuilding building = (ResidentialBuilding) getTarget();
        building.setGasLevel(building.getGasLevel() - 10);
        if (building.getGasLevel() == 0) jobsDone();
    }
}
