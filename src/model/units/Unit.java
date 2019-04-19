package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.UnitException;
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
        if (state != UnitState.IDLE) {
            if (this instanceof Evacuator) {
                Evacuator evacuator = (Evacuator) this;
                ResidentialBuilding building = (ResidentialBuilding) target;
                if (evacuator.getPassengers().isEmpty()) {
                    if (distanceToTarget == 0) {
                        // Fill citizens
                        evacuator.setDistanceToBase(getLocation().getX() + getLocation().getY());
                        if (building.getStructuralIntegrity() == 0 || building.getOccupants().size() == 0) {
                            jobsDone();
                        } else {
                            evacuator.treat();
                        }
                    } else {
                        // Go to target
                        setState(UnitState.RESPONDING);
                        int movedDistance = distanceToTarget - getStepsPerCycle() < 0 ? distanceToTarget : getStepsPerCycle();
                        evacuator.setDistanceToTarget(distanceToTarget - movedDistance);
                        evacuator.setDistanceToBase(evacuator.getDistanceToBase() + movedDistance);
                        if (distanceToTarget == 0) {
                            worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                        }
                    }
                } else {
                    if (evacuator.getDistanceToBase() == 0) {
                        // Evacuate citizens
                        while (!evacuator.getPassengers().isEmpty()) {
                            Citizen citizen = evacuator.getPassengers().get(0);
                            citizen.setState(CitizenState.RESCUED);
                            evacuator.getPassengers().remove(citizen);
                        }
                        evacuator.setDistanceToTarget(Math.abs(target.getLocation().getX() - getLocation().getX()) +
                                Math.abs(target.getLocation().getY() - getLocation().getY()));
                        if (building.getStructuralIntegrity() == 0 || building.getOccupants().size() == 0) {
                            jobsDone();
                        }
                    } else {
                        // Return to base
                        int movedDistance = evacuator.getDistanceToBase() - getStepsPerCycle() < 0 ? evacuator.getDistanceToBase() : getStepsPerCycle();
                        evacuator.setDistanceToBase(evacuator.getDistanceToBase() - movedDistance);
                        evacuator.setDistanceToTarget(distanceToTarget + movedDistance);
                        if (evacuator.getDistanceToBase() == 0) {
                            worldListener.assignAddress(this, 0, 0);
                            for (Citizen citizen : evacuator.getPassengers()) {
                                worldListener.assignAddress(citizen, 0, 0);
                            }
                            cycleStep();
                        }
                    }
                }
            } else {
                if (distanceToTarget == 0) {
                    worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                    if ((target instanceof Citizen && ((Citizen) target).getState() == CitizenState.DECEASED) ||
                            (target instanceof ResidentialBuilding && ((ResidentialBuilding) target).getStructuralIntegrity() == 0)) {
                        jobsDone();
                    } else {
                        treat();
                    }
                } else {
                    distanceToTarget = Math.max(distanceToTarget - stepsPerCycle, 0);
                    if (distanceToTarget == 0) {
                        worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                    }
                }
            }
        }
    }

    public void treat() {
        target.getDisaster().setActive(false);
        setState(UnitState.TREATING);
    }

    public void jobsDone() {
        target = null;
        setState(UnitState.IDLE);
    }

    @Override
    public void respond(Rescuable r) throws UnitException {
        if (((this instanceof FireUnit || this instanceof PoliceUnit || this instanceof GasControlUnit) && r instanceof Citizen)
                || ((this instanceof MedicalUnit) && r instanceof ResidentialBuilding)) {
            throw new IncompatibleTargetException(this, r);
        }
        if (!canTreat(r)) throw new CannotTreatException(this, r);
        setState(UnitState.RESPONDING);
        if (target != null) {
            if (r instanceof Citizen) {
                Citizen citizen = (Citizen) r;
                if (!(this instanceof MedicalUnit && citizen.getHp() < 100 && citizen.getBloodLoss() == 0 && citizen.getToxicity() == 0)) {
                    target.getDisaster().setActive(true);
                }
            } else {
                target.getDisaster().setActive(true);
            }
        }
        target = r;
        setDistanceToTarget(Math.abs(target.getLocation().getX() - getLocation().getX()) +
                Math.abs(target.getLocation().getY() - getLocation().getY()));
    }

    private boolean canTreat(Rescuable r) {
        if (r instanceof ResidentialBuilding) {
            ResidentialBuilding building = (ResidentialBuilding) r;
            return (this instanceof Evacuator && building.getFoundationDamage() != 0)
                    || (this instanceof FireTruck && building.getFireDamage() != 0)
                    || (this instanceof GasControlUnit && building.getGasLevel() != 0);
        } else if (r instanceof Citizen) {
            Citizen citizen = (Citizen) r;
            return (this instanceof Ambulance && citizen.getBloodLoss() != 0)
                    || (this instanceof DiseaseControlUnit && citizen.getToxicity() != 0);
        }
        return false;
    }

    @Override
    public String toString() {
        return "<b>" + getClass().getSimpleName() + ":</b>" +
                "\nLocation: (" + location.getX() + "," + location.getY() + ")" +
                "\nUnit ID: " + unitID +
                "\nSteps Per Cycle: " + stepsPerCycle +
                "\nTarget: " + (target != null ? target.getClass().getSimpleName() +
                " (" + target.getLocation().getX() + "," + target.getLocation().getY() + ")" : "None") +
                "\nState: " + state;
    }
}
