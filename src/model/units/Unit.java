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

    public Unit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
        this.unitID = unitID;
        this.location = location;
        this.stepsPerCycle = stepsPerCycle;
        state = UnitState.IDLE;
        setWorldListener(worldListener);
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

    public void setDistanceToTarget(int distanceToTarget) {
        this.distanceToTarget = distanceToTarget;
    }

    public WorldListener getWorldListener() {
        return worldListener;
    }

    public void setWorldListener(WorldListener worldListener) {
        this.worldListener = worldListener;
    }

    public void cycleStep() {
        if (state == UnitState.RESPONDING) {
            if (target instanceof ResidentialBuilding && this instanceof Evacuator) {
                Evacuator evacuator = (Evacuator) this;
                if (evacuator.getPassengers().size() == evacuator.getMaxCapacity()) {
                    evacuator.setDistanceToBase(Math.max(evacuator.getDistanceToBase() - evacuator.getStepsPerCycle(), 0));
                    evacuator.setDistanceToTarget(Math.max(target.getLocation().getX() + target.getLocation().getY() + evacuator.getStepsPerCycle(), 0));
                } else {
                    evacuator.setDistanceToBase(Math.max(evacuator.getDistanceToBase() - evacuator.getStepsPerCycle(), 0));
                    evacuator.setDistanceToTarget(Math.max(target.getLocation().getX() + target.getLocation().getY() + evacuator.getStepsPerCycle(), 0));
                }
            } else {
                distanceToTarget = Math.max(distanceToTarget - stepsPerCycle, 0);
            }
        }
        if (getLocation().getX() == target.getLocation().getX() &&
                getLocation().getY() == target.getLocation().getY()) {
            if (target instanceof ResidentialBuilding && this instanceof Evacuator) {
                ResidentialBuilding building = (ResidentialBuilding) target;
                Evacuator evacuator = (Evacuator) this;
                int capacityToFill = evacuator.getMaxCapacity() - evacuator.getPassengers().size();
                for (int i = 0; i < Math.min(building.getOccupants().size(), capacityToFill); i++) {
                    evacuator.getPassengers().add(building.getOccupants().remove(building.getOccupants().size() - 1));
                }
            } else {
                treat();
            }
        }
    }

    private void treat() {
        target.getDisaster().setActive(false);
        setState(UnitState.TREATING);
        if (target instanceof ResidentialBuilding) {
            ResidentialBuilding building = (ResidentialBuilding) target;
            int treatmentAmount = 10;
            building.setFireDamage(Math.max(building.getFireDamage() - treatmentAmount, 0));
            building.setFoundationDamage(Math.max(building.getFoundationDamage() - treatmentAmount, 0));
            building.setGasLevel(Math.max(building.getGasLevel() - treatmentAmount, 0));
        } else if (target instanceof Citizen) {
            Citizen citizen = (Citizen) target;
            int treatmentAmount = ((MedicalUnit) this).getTreatmentAmount();
            citizen.setBloodLoss(Math.max(citizen.getBloodLoss() - treatmentAmount, 0));
            citizen.setToxicity(Math.max(citizen.getToxicity() - treatmentAmount, 0));
            if (citizen.getBloodLoss() == 0 && citizen.getToxicity() == 0) {
                ((MedicalUnit) this).heal();
            }
            citizen.setState(CitizenState.RESCUED);
        }
    }

    private void jobsDone() {
        if (target instanceof Citizen) {
            Citizen citizen = (Citizen) target;
            if (citizen.getState() == CitizenState.RESCUED || citizen.getState() == CitizenState.DECEASED) {
                state = UnitState.IDLE;
            }
        } else if (target instanceof ResidentialBuilding) {
            ResidentialBuilding building = (ResidentialBuilding) target;
            if ((building.getFireDamage() == 0 && building.getFoundationDamage() == 0 && building.getGasLevel() == 0) ||
                    building.getStructuralIntegrity() == 0) {
                state = UnitState.IDLE;
            }
        }
    }

    @Override
    public void respond(Rescuable r) {
        if (getState() == UnitState.RESPONDING) {
            if (r instanceof Citizen) {
                Citizen citizen = (Citizen) r;
                if (!(citizen.getHp() < 100 && citizen.getBloodLoss() == 0 && citizen.getToxicity() == 0)) {
                    r.getDisaster().setActive(true);
                }
            } else {
                r.getDisaster().setActive(true);
            }
        } else {
            setState(UnitState.RESPONDING);
        }
        target = r;
        if (r instanceof Citizen) {
            Citizen citizen = (Citizen) r;
            setDistanceToTarget((citizen.getLocation().getX() - getLocation().getX()) + (citizen.getLocation().getY() - getLocation().getY()));
        } else if (r instanceof ResidentialBuilding) {
            ResidentialBuilding building = (ResidentialBuilding) r;
            setDistanceToTarget((building.getLocation().getX() - getLocation().getX()) + (building.getLocation().getY() - getLocation().getY()));
        }
        treat();
    }
}
