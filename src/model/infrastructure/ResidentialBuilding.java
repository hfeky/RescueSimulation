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
        this.structuralIntegrity = Math.max(structuralIntegrity, 0);
        if (this.structuralIntegrity == 0) {
            for (Citizen citizen : occupants) {
                citizen.setHp(0);
            }
        }
    }

    public int getFireDamage() {
        return fireDamage;
    }

    public void setFireDamage(int fireDamage) {
        this.fireDamage = Math.min(Math.max(fireDamage, 0), 100);
    }

    public int getFoundationDamage() {
        return foundationDamage;
    }

    public void setFoundationDamage(int foundationDamage) {
        this.foundationDamage = Math.max(foundationDamage, 0);
        if (this.foundationDamage >= 100) {
            setStructuralIntegrity(0);
        }
    }

    public int getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(int gasLevel) {
        this.gasLevel = Math.min(Math.max(gasLevel, 0), 100);
        if (this.gasLevel == 100) {
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
        if (foundationDamage > 0) {
            setStructuralIntegrity(structuralIntegrity - (new Random().nextInt(6) + 5));
        }
        if (0 < fireDamage && fireDamage < 30) {
            setStructuralIntegrity(structuralIntegrity - 3);
        } else if (30 <= fireDamage && fireDamage < 70) {
            setStructuralIntegrity(structuralIntegrity - 5);
        } else if (70 <= fireDamage) {
            setStructuralIntegrity(structuralIntegrity - 7);
        }
    }

    public void setEmergencyService(SOSListener sosListener) {
        this.emergencyService = sosListener;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Location: (" + location.getX() + "," + location.getY() + ")" +
                "\nOccupants Amount: " + occupants.size() +
                "\nStructural Integrity: " + structuralIntegrity +
                "\nFire Damage: " + fireDamage +
                "\nGas Level: " + gasLevel +
                "\nFoundation Damage: " + foundationDamage +
                "\nCurrent Disaster: " + (disaster != null ? disaster.getClass().getSimpleName() : "None"));
        for (int i = 1; i <= occupants.size(); i++) {
            sb.append("\n\n<b>Citizen ").append(i).append(":</b>\n").append(occupants.get(i - 1).toString());
        }
        return sb.toString();
    }
}
