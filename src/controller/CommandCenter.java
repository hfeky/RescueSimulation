package controller;

import exceptions.UnitException;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;
import view.UnitBlock;
import view.WorldBlock;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
        engine.setGameView(gameView);
    }

    @Override
    public void receiveSOSCall(Rescuable r) {
        if (r instanceof Citizen) {
            visibleCitizens.add((Citizen) r);
        } else if (r instanceof ResidentialBuilding) {
            visibleBuildings.add((ResidentialBuilding) r);
        }
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

    public GameView getGameView() {
        return gameView;
    }

    private static UnitBlock selectedUnit;

    public static void main(String[] args) throws Exception {
        CommandCenter commandCenter = new CommandCenter();
        GameView gameView = commandCenter.getGameView();

        for (int i = 0; i < gameView.getAvailableUnits().getComponentCount(); i++) {
            UnitBlock unitBlock = (UnitBlock) gameView.getAvailableUnits().getComponent(i);
            unitBlock.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    int state = e.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        unitBlock.removeItemListener(this);
                        gameView.deselectAllUnits();
                        unitBlock.setSelected(true);
                        unitBlock.addItemListener(this);
                        selectedUnit = unitBlock;
                    } else {
                        selectedUnit = null;
                    }
                }
            });
        }

        for (int i = 0; i < gameView.getGridPanel().getComponentCount(); i++) {
            WorldBlock worldBlock = (WorldBlock) gameView.getGridPanel().getComponent(i);
            worldBlock.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedUnit != null) {
                        Rescuable rescuable = worldBlock.getRescuable();
                        if (rescuable != null) {
                            try {
                                selectedUnit.getUnit().respond(rescuable);
                                gameView.invalidateUnitsPanel();
                                selectedUnit.setSelected(false);
                                selectedUnit = null;
                            } catch (UnitException ue) {
                                JOptionPane.showMessageDialog(gameView, ue.getMessage());
                            }
                        }
                    }
                }
            });
        }
    }
}
