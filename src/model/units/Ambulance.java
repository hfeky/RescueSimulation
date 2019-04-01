package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class Ambulance extends MedicalUnit {

    public Ambulance(String id, Address location, int stepsPerCycle, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
    }

    @Override
    public void treat() {
        super.treat();
        Citizen citizen = (Citizen) getTarget();
        if (citizen.getBloodLoss() > 0) {
            citizen.setBloodLoss(citizen.getBloodLoss() - getTreatmentAmount());
            if (citizen.getBloodLoss() == 0) {
                citizen.setState(CitizenState.RESCUED);
                jobsDone();
            }
        } else {
            heal();
        }
    }
}
