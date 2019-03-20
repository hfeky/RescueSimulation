package model.units;

import model.events.WorldListener;
import simulation.Address;

public class DiseaseControlUnit extends MedicalUnit {

    public DiseaseControlUnit(String id, Address location, int stepsPerCycle, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
    }
}
