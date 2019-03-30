package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

    public Evacuator(String id, Address location, int stepsPerCycle, WorldListener worldListener, int maxCapacity) {
        super(id, location, stepsPerCycle, worldListener, maxCapacity);
    }

    @Override
    public void treat() {
        setState(UnitState.TREATING);
        ResidentialBuilding building = (ResidentialBuilding) getTarget();
        while (!building.getOccupants().isEmpty() && getPassengers().size() < getMaxCapacity()) {
            getPassengers().add(building.getOccupants().remove(0));
        }
    }

    @Override
    public void respond(Rescuable r) {
        super.respond(r);
        setDistanceToBase(getLocation().getX() + getLocation().getY());
    }
}
