package model.units;

import model.disasters.*;
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
            if (target instanceof Citizen) {
                Citizen citizen = (Citizen) target;
                if (citizen.getState() == CitizenState.DECEASED) {
                    target = null;
                    setState(UnitState.IDLE);
                } else {
                    if (distanceToTarget == 0) {
                        treat();
                    } else {
                        distanceToTarget = Math.max(distanceToTarget - stepsPerCycle, 0);
                        if (distanceToTarget == 0) {
                            worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                        }
                    }
                }
            } else {
                ResidentialBuilding building = (ResidentialBuilding) target;
                if (building.getStructuralIntegrity() == 0) {
                    target = null;
                    setState(UnitState.IDLE);
                } else {
                    if (this instanceof Evacuator) {
                        Evacuator evacuator = (Evacuator) this;
                        if (evacuator.getDistanceToBase() == 0 && !evacuator.getPassengers().isEmpty()) {
                            for (Citizen citizen : evacuator.getPassengers()) {
                                citizen.setState(CitizenState.RESCUED);
                                evacuator.getPassengers().remove(citizen);
                            }
                            if (building.getStructuralIntegrity() == 0 || building.getOccupants().size() == 0) {
                                target = null;
                                setState(UnitState.IDLE);
                            }
                        } else if (evacuator.getPassengers().size() <= evacuator.getMaxCapacity()) {
                            // Return to base
                            int movedDistance;
                            if (evacuator.getDistanceToBase() - getStepsPerCycle() < 0) {
                                movedDistance = evacuator.getDistanceToBase();
                            } else {
                                movedDistance = getStepsPerCycle();
                            }
                            evacuator.setDistanceToBase(evacuator.getDistanceToBase() - movedDistance);
                            evacuator.setDistanceToTarget(distanceToTarget + movedDistance);
                            if (evacuator.getDistanceToBase() == 0) {
                                worldListener.assignAddress(this, 0, 0);
                                for (Citizen citizen : evacuator.getPassengers()) {
                                    worldListener.assignAddress(citizen, 0, 0);
                                }
                            }
                        } else if (distanceToTarget == 0 && evacuator.getPassengers().isEmpty()) {
                            int capacityToFill = evacuator.getMaxCapacity() - evacuator.getPassengers().size();
                            for (int i = 0; i < Math.min(building.getOccupants().size(), capacityToFill); i++) {
                                evacuator.getPassengers().add(building.getOccupants().remove(building.getOccupants().size() - 1));
                            }
                        } else if (building.getOccupants().size() > 0) {
                            // Go to target
                            int movedDistance;
                            if (distanceToTarget - getStepsPerCycle() < 0) {
                                movedDistance = distanceToTarget;
                            } else {
                                movedDistance = getStepsPerCycle();
                            }
                            evacuator.setDistanceToTarget(distanceToTarget - movedDistance);
                            evacuator.setDistanceToBase(evacuator.getDistanceToBase() + movedDistance);
                            if (distanceToTarget == 0) {
                                worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                            }
                        } else {
                            target = null;
                            setState(UnitState.IDLE);
                        }
                    } else {
                        if (distanceToTarget == 0) {
                            treat();
                        } else {
                            distanceToTarget = Math.max(distanceToTarget - stepsPerCycle, 0);
                            if (distanceToTarget == 0) {
                                worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                            }
                        }
                    }
                }
            }
        }
    }

    private void treat() {
        Disaster disaster = target.getDisaster();
        disaster.setActive(false);
        setState(UnitState.TREATING);
        if (target instanceof ResidentialBuilding) {
            ResidentialBuilding building = (ResidentialBuilding) target;
            int treatmentAmount = 10;
            if (disaster instanceof Collapse) {
                building.setFoundationDamage(building.getFoundationDamage() - treatmentAmount);
            } else if (disaster instanceof Fire) {
                building.setFireDamage(building.getFireDamage() - treatmentAmount);
            } else if (disaster instanceof GasLeak) {
                building.setGasLevel(building.getGasLevel() - treatmentAmount);
            }
        } else if (target instanceof Citizen) {
            Citizen citizen = (Citizen) target;
            int treatmentAmount = ((MedicalUnit) this).getTreatmentAmount();
            if (citizen.getBloodLoss() != 0 || citizen.getToxicity() != 0) {
                if (disaster instanceof Injury) {
                    citizen.setBloodLoss(citizen.getBloodLoss() - treatmentAmount);
                } else if (disaster instanceof Infection) {
                    citizen.setToxicity(citizen.getToxicity() - treatmentAmount);
                }
                if (citizen.getBloodLoss() == 0 && citizen.getToxicity() == 0) {
                    citizen.setState(CitizenState.RESCUED);
                }
            } else {
                ((MedicalUnit) this).heal();
            }
        }
        jobsDone();
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
}
