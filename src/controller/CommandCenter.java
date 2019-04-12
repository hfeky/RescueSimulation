package controller;

import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CommandCenter implements SOSListener, ActionListener {

    private Simulator engine;
    private ArrayList<ResidentialBuilding> visibleBuildings = new ArrayList<>();
    private ArrayList<Citizen> visibleCitizens = new ArrayList<>();
    private ArrayList<Unit> emergencyUnits = new ArrayList<>();
    private GameView gameView;

    public CommandCenter() throws Exception {
        engine = new Simulator(this);
        gameView = new GameView();
    }

    @Override
    public void receiveSOSCall(Rescuable r) {
        if (r instanceof Citizen) {
            visibleCitizens.add((Citizen) r);
        } else if (r instanceof ResidentialBuilding) {
            visibleBuildings.add((ResidentialBuilding) r);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) throws Exception {
        CommandCenter commandCenter = new CommandCenter();
    }
}
