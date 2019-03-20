package model.infrastructure;

import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
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

    public Disaster getDisaster() {
        return disaster;
    }

    public Address getLocation() {
        return location;
    }

    @Override
    public void struckBy(Disaster d) {
        disaster = d;
        emergencyService.receiveSOSCall(this);
    }

    public int getStructuralIntegrity() {
        return structuralIntegrity;
    }

    public void setStructuralIntegrity(int structuralIntegrity) {
        structuralIntegrity = Math.max(structuralIntegrity, 0);
        this.structuralIntegrity = structuralIntegrity;
        if (structuralIntegrity == 0) {
            for (Citizen citizen : occupants) {
                citizen.setHp(0);
            }
        }
    }

    public int getFireDamage() {
        return fireDamage;
    }

    public void setFireDamage(int fireDamage) {
        fireDamage = Math.min(Math.max(fireDamage, 0), 100);
        this.fireDamage = fireDamage;
    }

    public int getFoundationDamage() {
        return foundationDamage;
    }

    public void setFoundationDamage(int foundationDamage) {
        foundationDamage = Math.max(foundationDamage, 0);
        this.foundationDamage = foundationDamage;
        if (foundationDamage >= 100) {
            setStructuralIntegrity(0);
        }
    }

    public int getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(int gasLevel) {
        gasLevel = Math.min(Math.max(gasLevel, 0), 100);
        this.gasLevel = gasLevel;
        if (gasLevel == 100) {
            for (Citizen citizen : occupants) {
                citizen.setHp(0);
            }
        }
    }

    public ArrayList<Citizen> getOccupants() {
        return occupants;
    }

    @Override
    public void cycleStep() {
        if (disaster instanceof Collapse) {
            if (foundationDamage > 0) {
                setStructuralIntegrity(structuralIntegrity - new Random().nextInt(6) + 5);
            }
        } else if (disaster instanceof Fire) {
            if (0 < fireDamage && fireDamage < 30) {
                setStructuralIntegrity(structuralIntegrity - 3);
            } else if (30 <= fireDamage && fireDamage < 70) {
                setStructuralIntegrity(structuralIntegrity - 5);
            } else if (70 <= fireDamage) {
                setStructuralIntegrity(structuralIntegrity - 7);
            }
        }
    }

    public void setSOSListener(SOSListener sosListener) {
        this.emergencyService = sosListener;
    }
}
