package com.rescuesimulation.model.units;

import com.rescuesimulation.simulation.Address;

public abstract class MedicalUnit extends Unit {

    private int healingAmount = 10;
    private int treatmentAmount = 10;

    public MedicalUnit(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }
}
