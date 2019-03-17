package model.people;

import model.disasters.Disaster;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

import static model.people.CitizenState.IN_TROUBLE;

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

    @Override
    public void struckBy(Disaster d) {
        state = IN_TROUBLE;

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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
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

    @Override
    public void cycleStep() {

    }
}
