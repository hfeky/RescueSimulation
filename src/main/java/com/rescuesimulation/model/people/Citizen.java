package com.rescuesimulation.model.people;

import com.rescuesimulation.model.disasters.Disaster;
import com.rescuesimulation.simulation.Address;
import com.rescuesimulation.simulation.Rescuable;
import com.rescuesimulation.simulation.Simulatable;

public class Citizen implements Simulatable, Rescuable {

    private Address location;
    private String nationalID, name;
    private int age;
    private CitizenState state = CitizenState.SAFE;

    private int hp = 100;
    private int bloodLoss, toxicity;

    private Disaster disaster;

    public Citizen(Address location, String nationalID, String name, int age) {
        this.location = location;
        this.nationalID = nationalID;
        this.name = name;
        this.age = age;
    }

    public CitizenState getState() {
        return state;
    }

    public void setState(CitizenState state) {
        this.state = state;
    }

    public Disaster getDisaster() {
        return disaster;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public String getNationalID() {
        return nationalID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getHealth() {
        return hp;
    }

    public void setHealth(int hp) {
        this.hp = hp;
    }

    public int getBloodLoss() {
        return bloodLoss;
    }

    public void setBloodLoss(int bloodLoss) {
        this.bloodLoss = bloodLoss;
    }

    public int getToxicity() {
        return toxicity;
    }

    public void setToxicity(int toxicity) {
        this.toxicity = toxicity;
    }
}
