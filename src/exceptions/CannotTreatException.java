package exceptions;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Ambulance;
import model.units.Evacuator;
import model.units.Unit;
import simulation.Rescuable;

public class CannotTreatException extends UnitException {

    public CannotTreatException(Unit unit, Rescuable target) {
        this(unit, target, ((target instanceof ResidentialBuilding) ? "The ResidentialBuilding" :
                target.getClass().getSimpleName() + " " + ((Citizen) target).getName()) +
                " does not need to be treated by " +
                (unit instanceof Ambulance || unit instanceof Evacuator ? "an " : "a ") +
                unit.getClass().getSimpleName() + ".");
    }

    public CannotTreatException(Unit unit, Rescuable target, String message) {
        super(unit, target, message);
    }
}
