package exceptions;

import model.units.Ambulance;
import model.units.Evacuator;
import model.units.Unit;
import simulation.Rescuable;

public class IncompatibleTargetException extends UnitException {

    public IncompatibleTargetException(Unit unit, Rescuable target) {
        super(unit, target, (unit instanceof Ambulance || unit instanceof Evacuator ? "An " : "A ") + unit.getClass().getSimpleName() + " cannot treat a " + target.getClass().getSimpleName() + ".");
    }

    public IncompatibleTargetException(Unit unit, Rescuable target, String message) {
        super(unit, target, message);
    }
}
