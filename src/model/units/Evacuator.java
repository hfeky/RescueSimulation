package model.units;

import exceptions.UnitException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
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
        for (int i = 0; i < building.getOccupants().size() && getPassengers().size() < getMaxCapacity(); ) {
            Citizen citizen = building.getOccupants().get(i);
            if (citizen.getState() != CitizenState.DECEASED) {
                getPassengers().add(citizen);
                building.getOccupants().remove(i);
            } else {
                i++;
            }
        }
    }

    @Override
    public void respond(Rescuable r) throws UnitException {
        super.respond(r);
        setDistanceToBase(getLocation().getX() + getLocation().getY());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nPassengers Amount: ").append(getPassengers().size());
        for (int i = 1; i <= getPassengers().size(); i++) {
            sb.append("\n\nPassenger ").append(i).append(":\n").append(getPassengers().get(i - 1).toString());
        }
        return sb.toString();
    }
}
