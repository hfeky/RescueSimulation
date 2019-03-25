package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class DiseaseControlUnit extends MedicalUnit {

    public DiseaseControlUnit(String id, Address location, int stepsPerCycle, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
    }

    @Override
    public void treat() {
        super.treat();
        Citizen citizen = (Citizen) getTarget();
        if (citizen.getToxicity() != 0) {
            citizen.setToxicity(citizen.getToxicity() - getTreatmentAmount());
            if (citizen.getToxicity() == 0) {
                citizen.setState(CitizenState.RESCUED);
                jobsDone();
            }
        } else {
            heal();
        }
    }
}
