package model.infrastructure;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

import java.util.ArrayList;
import java.util.Random;

public class ResidentialBuilding implements Simulatable, Rescuable {

    private Address location;

    private int structuralIntegrity = 100;
    private int fireDamage, foundationDamage, gasLevel;

    private ArrayList<Citizen> occupants = new ArrayList<>();
    private Disaster disaster;

    private SOSListener emergencyService;

    public ResidentialBuilding(Address location) {
        this.location = location;
    }

    public Address getLocation() {
        return location;
    }

    @Override
    public void struckBy(Disaster d) {
        emergencyService.receiveSOSCall(this);
    }

    public int getStructuralIntegrity() {
        return structuralIntegrity;
    }

    public void setStructuralIntegrity(int structuralIntegrity) {
        if (structuralIntegrity >= 0) {
            this.structuralIntegrity = structuralIntegrity;
            if (structuralIntegrity == 0) {
                for (Citizen citizen : occupants) {
                    citizen.setHp(0);
                }
            }
        }
    }

    public int getFireDamage() {
        return fireDamage;
    }

    public void setFireDamage(int fireDamage) {
        if (0 <= fireDamage && fireDamage <= 100) {
            this.fireDamage = fireDamage;
        }
    }

    public int getFoundationDamage() {
        return foundationDamage;
    }

    public void setFoundationDamage(int foundationDamage) {
        this.foundationDamage = foundationDamage;
        if (foundationDamage >= 100) {
            setStructuralIntegrity(0);
        }
    }

    public int getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(int gasLevel) {
        if (0 <= gasLevel && gasLevel <= 100) {
            this.gasLevel = gasLevel;
            if (gasLevel == 100) {
                for (Citizen citizen : occupants) {
                    citizen.setHp(0);
                }
            }
        }
    }

    public ArrayList<Citizen> getOccupants() {
        return occupants;
    }

    public Disaster getDisaster() {
        return disaster;
    }

    @Override
    public void cycleStep() {
        if (foundationDamage > 0) {
            structuralIntegrity -= new Random().nextInt(6) + 5;
        }

        if (0 < fireDamage && fireDamage < 30) {
            structuralIntegrity -= 3;
        } else if (30 <= fireDamage && fireDamage < 70) {
            structuralIntegrity -= 5;
        } else if (70 <= fireDamage) {
            structuralIntegrity -= 7;
        }
    }

    public void setSOSListener(SOSListener sosListener) {
        this.emergencyService = sosListener;
    }
}
