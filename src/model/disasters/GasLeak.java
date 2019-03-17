package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster {

    private ResidentialBuilding residentialBuilding;

    public GasLeak(int cycle, ResidentialBuilding target) {
        super(cycle, target);
        this.residentialBuilding = target;
    }

    @Override
    public void cycleStep() {
        residentialBuilding.setGasLevel(residentialBuilding.getGasLevel() + 15);
    }

    @Override
    public void strike() {
        residentialBuilding.setGasLevel(residentialBuilding.getGasLevel() + 10);
    }
}
