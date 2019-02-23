package com.rescuesimulation.controller;

import com.rescuesimulation.model.infrastructure.ResidentialBuilding;
import com.rescuesimulation.model.people.Citizen;
import com.rescuesimulation.model.units.Unit;
import com.rescuesimulation.simulation.Simulator;

import java.util.ArrayList;

public class CommandCenter {

    private Simulator engine;
    private ArrayList<ResidentialBuilding> visibleBuildings;
    private ArrayList<Citizen> visibleCitizens;
    private ArrayList<Unit> emergencyUnits;

    public CommandCenter() {

    }
}
