package exceptions;

import model.disasters.Disaster;

public class CitizenAlreadyDeadException extends DisasterException {

    public CitizenAlreadyDeadException(Disaster disaster) {
        this(disaster, "The Citizen is already dead.");
    }

    public CitizenAlreadyDeadException(Disaster disaster, String message) {
        super(disaster, message);
    }
}
