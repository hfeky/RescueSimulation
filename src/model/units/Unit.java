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
        if (this instanceof Evacuator) {
            Evacuator evacuator = (Evacuator) this;
            ResidentialBuilding building = (ResidentialBuilding) target;
            if (state == UnitState.RESPONDING || state == UnitState.TREATING) {
                if (evacuator.getPassengers().isEmpty()) {
                    if (distanceToTarget == 0) {
                        // Fill citizens
                        System.out.println("Filling citizens");
                        evacuator.treat();
                        evacuator.setDistanceToBase(getLocation().getX() + getLocation().getY());
                    } else {
                        // Go to target
                        System.out.println("Going to target");
                        setState(UnitState.RESPONDING);
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
                    }
                } else {
                    if (evacuator.getDistanceToBase() == 0) {
                        // Evacuate citizens
                        System.out.println("Evacuating citizens");
                        while (!evacuator.getPassengers().isEmpty()) {
                            Citizen citizen = evacuator.getPassengers().get(0);
                            citizen.setState(CitizenState.RESCUED);
                            evacuator.getPassengers().remove(citizen);
                        }
                        if (building.getStructuralIntegrity() == 0 || building.getOccupants().size() == 0) {
                            jobsDone();
                        }
                    } else {
                        // Return to base
                        System.out.println("Returning to base");
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
                    }
                }
                System.out.println("distanceToTarget: " + distanceToTarget + "\ndistanceToBase: " + evacuator.getDistanceToBase() + "\n");
            }
        } else {
            if (state == UnitState.RESPONDING) {
                if (target instanceof Citizen) {
                    Citizen citizen = (Citizen) target;
                    if (citizen.getState() == CitizenState.DECEASED) {
                        jobsDone();
                    } else {
                        if (distanceToTarget == 0) {
                            worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
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
                        jobsDone();
                    } else {
                        if (distanceToTarget == 0) {
                            worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                            treat();
                        } else {
                            distanceToTarget = Math.max(distanceToTarget - stepsPerCycle, 0);
                            if (distanceToTarget == 0) {
                                worldListener.assignAddress(this, target.getLocation().getX(), target.getLocation().getY());
                            }
                        }
                    }
                }
            } else if (state == UnitState.TREATING) {
                treat();
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
