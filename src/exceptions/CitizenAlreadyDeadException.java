package exceptions;

import model.disasters.Disaster;

public class CitizenAlreadyDeadException extends DisasterException {

    public CitizenAlreadyDeadException(Disaster disaster) {
        super(disaster, "The Citizen is already dead.");
    }

    public CitizenAlreadyDeadException(Disaster disaster, String message) {
        super(disaster, message);
    }
}
