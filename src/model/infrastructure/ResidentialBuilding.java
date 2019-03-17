package model.infrastructure;

import model.disasters.Disaster;
import model.events.SOSListener;
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
    private SOSListener emergencyService;

    public void setEmergencyService(SOSListener emergencyService) {
        this.emergencyService = emergencyService;
    }


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
        if (structuralIntegrity == 0) {
//            loop that sets every occupant's  Hp to 0
            for(int i=0 ; i< occupants.size();i++){
                occupants.get(i).setHp(0);

            }

        }
        this.structuralIntegrity = structuralIntegrity;
    }

    public int getFireDamage() {
        return fireDamage;
    }

    public void setFireDamage(int fireDamage) {
        if (fireDamage == 0) {
            structuralIntegrity = 0;
        } else if (fireDamage > 0 && fireDamage <= 100) this.fireDamage = fireDamage;
        this.fireDamage = fireDamage;
    }

    public int getFoundationDamage() {
        return foundationDamage;
    }

    public void setFoundationDamage(int foundationDamage) {
        if (foundationDamage > 100) {
            structuralIntegrity = 0;
        }
        this.foundationDamage = foundationDamage;
    }

    public int getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(int gasLevel) {
        if (gasLevel == 100) {
            for(int i=0 ; i< occupants.size();i++){
                occupants.get(i).setHp(0);

            }

        } else if (gasLevel >= 0 && gasLevel < 100) {
            this.gasLevel = gasLevel;
        }
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
        if (foundationDamage > 0) {
            structuralIntegrity = (int)(Math.random() * ((10 - 5) + 1)) + 5;
        }
        if (fireDamage > 30 && fireDamage > 0) {
            structuralIntegrity -= 3;
        }
        if (fireDamage >= 30 && fireDamage < 70) {
            structuralIntegrity -= 5;
        }
        if (fireDamage >= 70) {
            structuralIntegrity -= 7;
        }

    }

}
