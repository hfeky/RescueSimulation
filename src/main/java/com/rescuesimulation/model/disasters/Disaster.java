package com.rescuesimulation.model.disasters;

import com.rescuesimulation.simulation.Rescuable;
import com.rescuesimulation.simulation.Simulatable;

public abstract class Disaster implements Simulatable {

    private int startCycle;
    private Rescuable target;
    private boolean active;

    public Disaster(int startCycle, Rescuable target) {
        this.startCycle = startCycle;
        this.target = target;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStartCycle() {
        return startCycle;
    }

    public Rescuable getTarget() {
        return target;
    }
}
