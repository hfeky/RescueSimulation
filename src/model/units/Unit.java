package model.units;

import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable {

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

    public UnitState getState() {
        return state;
    }

    public void setState(UnitState state) {
        this.state = state;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public Rescuable getTarget() {
        return target;
    }

    public int getStepsPerCycle() {
        return stepsPerCycle;
    }

    @Override
    public void cycleStep() {

    }
}
