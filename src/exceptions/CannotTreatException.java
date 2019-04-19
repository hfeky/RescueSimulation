package exceptions;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Rescuable;

public class CannotTreatException extends UnitException {

    public CannotTreatException(Unit unit, Rescuable target) {
        this(unit, target, ((target instanceof ResidentialBuilding) ? "The ResidentialBuilding" : target.getClass().getSimpleName() + " " + ((Citizen) target).getName()) + " is already safe.");
    }

    public CannotTreatException(Unit unit, Rescuable target, String message) {
        super(unit, target, message);
    }
}
