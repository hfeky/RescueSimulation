package model.events;

import simulation.Rescuable;

public interface SOSResponder {
    void cycleStep();
    void receiveSOSCall(Rescuable r);
    void respond(Rescuable r);
}
