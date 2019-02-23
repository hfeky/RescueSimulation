package com.rescuesimulation.model.units;

import com.rescuesimulation.simulation.Address;

public class DiseaseControlUnit extends MedicalUnit {

    public DiseaseControlUnit(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }
}