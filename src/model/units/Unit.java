package model.units;

import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {

    private String unitID;
    private UnitState state;
    private Address location;
    private Rescuable target;
    private int distanceToTarget;
    private int stepsPerCycle;
    private WorldListener worldListener;

    public Unit(String unitID, Address location, int stepsPerCycle) {
        this.unitID = unitID;
        this.location = location;
        this.stepsPerCycle = stepsPerCycle;
        state = UnitState.IDLE;
    }

    public void setDistanceToTarget(int distanceToTarget) {
        this.distanceToTarget = distanceToTarget;
    }

    public WorldListener getWorldListener() {
        return worldListener;
    }

    public void setWorldListener(WorldListener worldListener) {
        this.worldListener = worldListener;
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
        if (state == UnitState.RESPONDING) {
            if (distanceToTarget <= stepsPerCycle) {
                distanceToTarget = 0;
                state = UnitState.TREATING;
                location = target.getLocation();
            } else //if (distanceToTarget > 0)
                distanceToTarget -= stepsPerCycle;
        } else if (state == UnitState.TREATING) {
            treat();
        }
    }

    public void treat() {
        target.getDisaster().setActive(false);
        if (target instanceof ResidentialBuilding) {
            int fireDamage = ((ResidentialBuilding) target).getFireDamage();
            int foundationDamage = ((ResidentialBuilding) target).getFoundationDamage();
            int gasLevel = ((ResidentialBuilding) target).getGasLevel();
            int treatmentAmount = 10;

            if (fireDamage < treatmentAmount) {
                treatmentAmount -= fireDamage;
                fireDamage = 0;
            } else {
                fireDamage -= treatmentAmount;
                treatmentAmount = 0;
            }
            if (foundationDamage < treatmentAmount) {
                treatmentAmount -= foundationDamage;
                foundationDamage = 0;
            } else {
                foundationDamage -= treatmentAmount;
                treatmentAmount = 0;
            }
            if (gasLevel < treatmentAmount) {
                treatmentAmount -= gasLevel;
                gasLevel = 0;
            } else {
                gasLevel -= treatmentAmount;
                treatmentAmount = 0;
            }

            ((ResidentialBuilding) target).setFireDamage(fireDamage);
            ((ResidentialBuilding) target).setFoundationDamage(foundationDamage);
            ((ResidentialBuilding) target).setGasLevel(gasLevel);
        } else if (target instanceof Citizen) {
            int bloodLoss = ((Citizen) target).getBloodLoss();
            int toxicity = ((Citizen) target).getToxicity();
            int treatmentAmount = ((MedicalUnit) this).getTreatmentAmount();

            if (bloodLoss < treatmentAmount) {
                treatmentAmount -= bloodLoss;
                bloodLoss = 0;
            } else {
                bloodLoss -= treatmentAmount;
                treatmentAmount = 0;
            }
            if (toxicity < treatmentAmount) {
                treatmentAmount -= toxicity;
                toxicity = 0;
            } else {
                toxicity -= treatmentAmount;
                treatmentAmount = 0;
            }

            ((Citizen) target).setState(CitizenState.RESCUED);
            ((Citizen) target).setBloodLoss(bloodLoss);
            ((Citizen) target).setToxicity(toxicity);

            if (bloodLoss == 0 && toxicity == 0)
                ((MedicalUnit) this).heal();
        }
    }
    public void jobsDone() {
        if (target instanceof Citizen && ( ((Citizen) target).getState() == CitizenState.RESCUED || ((Citizen) target).getState() == CitizenState.DECEASED) )
            state = UnitState.IDLE;
        else if (target instanceof ResidentialBuilding && ( ( ((ResidentialBuilding) target).getFireDamage() == 0 && ((ResidentialBuilding) target).getFoundationDamage() == 0 && ((ResidentialBuilding) target).getGasLevel() == 0) || ((ResidentialBuilding) target).getStructuralIntegrity() == 0) )
            state = UnitState.IDLE;
    }
}
