package controller;

import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;

import java.util.ArrayList;

public class CommandCenter implements SOSListener {

    private Simulator engine;
    private ArrayList<ResidentialBuilding> visibleBuildings = new ArrayList<>();
    private ArrayList<Citizen> visibleCitizens = new ArrayList<>();
    private ArrayList<Unit> emergencyUnits = new ArrayList<>();

    public CommandCenter() throws Exception {
        engine = new Simulator();
    }

    @Override
    public void receiveSOSCall(Rescuable r) {

    }
}
