package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import simulation.Address;

import java.util.ArrayList;

public abstract class PoliceUnit extends Unit {

    private int maxCapacity;

    private ArrayList<Citizen> passengers = new ArrayList<>();
    private int distanceToBase;

    public PoliceUnit(String id, Address location, int stepsPerCycle, int maxCapacity, WorldListener worldListener) {
        super(id, location, stepsPerCycle, worldListener);
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public ArrayList<Citizen> getPassengers() {
        return passengers;
    }

    public int getDistanceToBase() {
        return distanceToBase;
    }

    public void setDistanceToBase(int distanceToBase) {
        this.distanceToBase = distanceToBase;
    }
}
