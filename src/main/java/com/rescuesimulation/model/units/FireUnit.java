package com.rescuesimulation.model.units;

import com.rescuesimulation.simulation.Address;

public abstract class FireUnit extends Unit {

    public FireUnit(String id, Address location, int stepsPerCycle) {
        super(id, location, stepsPerCycle);
    }
}
