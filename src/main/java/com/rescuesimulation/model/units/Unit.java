package com.rescuesimulation.model.units;

import com.rescuesimulation.simulation.Address;
import com.rescuesimulation.simulation.Rescuable;

public abstract class Unit {

    private String unitID;
    private UnitState state;
    private Address location;
    private Rescuable target;
    private int distanceToTarget;
    private int stepsPerCycle;

    public Unit(String unitID, Address location, int stepsPerCycle) {
        this.unitID = unitID;
        this.location = location;
        this.stepsPerCycle = stepsPerCycle;
        state = UnitState.IDLE;
    }

    public String getUnitID() {
        return unitID;
    }

    public Rescuable getTarget() {
        return target;
    }

    public int getStepsPerCycle() {
        return stepsPerCycle;
    }
}
