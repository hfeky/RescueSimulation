package controller;

import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;

import java.util.ArrayList;

public class CommandCenter implements SOSListener {

    private Simulator engine;
    private ArrayList<ResidentialBuilding> visibleBuildings = new ArrayList<>();
    private ArrayList<Citizen> visibleCitizens = new ArrayList<>();
    private ArrayList<Unit> emergencyUnits = new ArrayList<>();
    private GameView gameView;

    public CommandCenter() throws Exception {
        engine = new Simulator(this);
        gameView = new GameView(this, engine);
    }

    @Override
    public void receiveSOSCall(Rescuable r) {
        if (r instanceof Citizen) {
            visibleCitizens.add((Citizen) r);
        } else if (r instanceof ResidentialBuilding) {
            visibleBuildings.add((ResidentialBuilding) r);
        }
        gameView.addRescuableOnWorldMap(r);
    }

    public ArrayList<Citizen> getVisibleCitizens() {
        return visibleCitizens;
    }

    public ArrayList<ResidentialBuilding> getVisibleBuildings() {
        return visibleBuildings;
    }

    public ArrayList<Unit> getEmergencyUnits() {
        return emergencyUnits;
    }

    public static void main(String[] args) throws Exception {
        CommandCenter commandCenter = new CommandCenter();
    }
}
