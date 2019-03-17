package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {

    private ResidentialBuilding residentialBuilding;

    public Collapse(int cycle, ResidentialBuilding target) {
        super(cycle, target);
        this.residentialBuilding = target;
    }

    @Override
    public void cycleStep() {
        residentialBuilding.setFoundationDamage(residentialBuilding.getFoundationDamage() + 10);
    }

    @Override
    public void strike() {
        residentialBuilding.setFoundationDamage(residentialBuilding.getFoundationDamage() + 10);
    }
}
