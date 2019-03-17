package model.units;

import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

    public DiseaseControlUnit(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }

    @Override
    public void cycleStep() {

    }

    @Override
    public void receiveSOSCall(Rescuable r) {

    }

    @Override
    public void respond(Rescuable r) {

    }
}