package controller;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Simulator;

import java.util.ArrayList;

public class CommandCenter {

    private Simulator engine;
    private ArrayList<ResidentialBuilding> visibleBuildings = new ArrayList<>();
    private ArrayList<Citizen> visibleCitizens = new ArrayList<>();
    private ArrayList<Unit> emergencyUnits = new ArrayList<>();

    public CommandCenter() throws Exception {
        engine = new Simulator();
    }
}
