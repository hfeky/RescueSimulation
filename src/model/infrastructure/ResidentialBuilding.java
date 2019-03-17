package model.infrastructure;

import model.disasters.Disaster;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

import java.util.ArrayList;

public class ResidentialBuilding implements Simulatable, Rescuable {

    private Address location;

    private int structuralIntegrity = 100;
    private int fireDamage, foundationDamage, gasLevel;

    private ArrayList<Citizen> occupants = new ArrayList<>();
    private Disaster disaster;

    public ResidentialBuilding(Address location) {
        this.location = location;
    }

    public Address getLocation() {
        return location;
    }

    @Override
    public void struckBy(Disaster d) {


    }

    public int getStructuralIntegrity() {
        return structuralIntegrity;
    }

    public void setStructuralIntegrity(int structuralIntegrity) {
        this.structuralIntegrity = structuralIntegrity;
    }

    public int getFireDamage() {
        return fireDamage;
    }

    public void setFireDamage(int fireDamage) {
        this.fireDamage = fireDamage;
    }

    public int getFoundationDamage() {
        return foundationDamage;
    }

    public void setFoundationDamage(int foundationDamage) {
        this.foundationDamage = foundationDamage;
    }

    public int getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(int gasLevel) {
        this.gasLevel = gasLevel;
    }

    public ArrayList<Citizen> getOccupants() {
        return occupants;
    }

    public Disaster getDisaster() {
        return disaster;
    }

    @Override
    public void cycleStep() {

    }
}
