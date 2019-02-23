package com.rescuesimulation.model.units;

import com.rescuesimulation.simulation.Address;

public class Evacuator extends PoliceUnit {

    public Evacuator(int maxCapacity, String id, Address location, int stepsPerCycle) {
        super(maxCapacity, id, location, stepsPerCycle);
    }
}
