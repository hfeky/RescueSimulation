package com.rescuesimulation.model.units;

import com.rescuesimulation.model.people.Citizen;
import com.rescuesimulation.simulation.Address;

import java.util.ArrayList;

public abstract class PoliceUnit extends Unit {

    private ArrayList<Citizen> passengers = new ArrayList<>();
    private int maxCapacity;
    private int distanceToBase;

    public PoliceUnit(String id, Address location, int stepsPerCycle, int maxCapacity) {
        super(id, location, stepsPerCycle);
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getDistanceToBase() {
        return distanceToBase;
    }

    public void setDistanceToBase(int distanceToBase) {
        this.distanceToBase = distanceToBase;
    }
}
