package simulation;

import exceptions.DisasterException;
import model.disasters.*;
import model.events.SOSListener;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.*;
import view.GameView;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulator implements WorldListener {

    private ArrayList<ResidentialBuilding> buildings = new ArrayList<>();
    private ArrayList<Citizen> citizens = new ArrayList<>();
    private ArrayList<Unit> emergencyUnits = new ArrayList<>();
    private ArrayList<Disaster> plannedDisasters = new ArrayList<>();
    private ArrayList<Disaster> executedDisasters = new ArrayList<>();

    private Address[][] world = new Address[10][10];
    private int currentCycle;
    private SOSListener emergencyService;

    private GameView gameView;

    public Simulator(SOSListener sosListener) throws Exception {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = new Address(i, j);
            }
        }
        setEmergencyService(sosListener);
        loadBuildings("buildings.csv");
        loadCitizens("citizens.csv");
        loadUnits("units.csv");
        loadDisasters("disasters.csv");
    }

    private void loadBuildings(String filePath) throws Exception {
        Scanner input = new Scanner(new File(filePath));
        while (input.hasNext()) {
            String[] cells = input.nextLine().split(",");
            ResidentialBuilding building = new ResidentialBuilding(world[Integer.parseInt(cells[0])][Integer.parseInt(cells[1])]);
            building.setEmergencyService(emergencyService);
            buildings.add(building);
        }
    }

    private void loadCitizens(String filePath) throws Exception {
        Scanner input = new Scanner(new File(filePath));
        while (input.hasNext()) {
            String[] cells = input.nextLine().split(",");
            Citizen citizen = new Citizen(world[Integer.parseInt(cells[0])][Integer.parseInt(cells[1])],
                    cells[2], cells[3], Integer.parseInt(cells[4]), this);
            citizen.setEmergencyService(emergencyService);
            citizens.add(citizen);
            for (ResidentialBuilding building : buildings) {
                if (building.getLocation().getX() == citizen.getLocation().getX() &&
                        building.getLocation().getY() == citizen.getLocation().getY()) {
                    building.getOccupants().add(citizen);
                }
            }
        }
    }

    private void loadUnits(String filePath) throws Exception {
        Scanner input = new Scanner(new File(filePath));
        while (input.hasNext()) {
            String[] cells = input.nextLine().split(",");
            switch (cells[0]) {
                case "AMB":
                    emergencyUnits.add(new Ambulance(cells[1], world[0][0], Integer.parseInt(cells[2]), this));
                    break;
                case "DCU":
                    emergencyUnits.add(new DiseaseControlUnit(cells[1], world[0][0], Integer.parseInt(cells[2]), this));
                    break;
                case "EVC":
                    emergencyUnits.add(new Evacuator(cells[1], world[0][0], Integer.parseInt(cells[2]), this, Integer.parseInt(cells[3])));
                    break;
                case "FTK":
                    emergencyUnits.add(new FireTruck(cells[1], world[0][0], Integer.parseInt(cells[2]), this));
                    break;
                case "GCU":
                    emergencyUnits.add(new GasControlUnit(cells[1], world[0][0], Integer.parseInt(cells[2]), this));
                    break;
            }
        }
    }

    private void loadDisasters(String filePath) throws Exception {
        Scanner input = new Scanner(new File(filePath));
        while (input.hasNext()) {
            String[] cells = input.nextLine().split(",");
            switch (cells[1]) {
                case "FIR":
                    plannedDisasters.add(new Fire(Integer.parseInt(cells[0]),
                            findBuilding(world[Integer.parseInt(cells[2])][Integer.parseInt(cells[3])])));
                    break;
                case "GLK":
                    plannedDisasters.add(new GasLeak(Integer.parseInt(cells[0]),
                            findBuilding(world[Integer.parseInt(cells[2])][Integer.parseInt(cells[3])])));
                    break;
                case "INF":
                    plannedDisasters.add(new Infection(Integer.parseInt(cells[0]), findCitizen(cells[2])));
                    break;
                case "INJ":
                    plannedDisasters.add(new Injury(Integer.parseInt(cells[0]), findCitizen(cells[2])));
                    break;
            }
        }
    }

    private ResidentialBuilding findBuilding(Address address) {
        for (ResidentialBuilding building : buildings) {
            Address location = building.getLocation();
            if (location.getX() == address.getX() && location.getY() == address.getY()) return building;
        }
        throw new IllegalArgumentException("No building found with the given Address.");
    }

    private Citizen findCitizen(String nationalID) {
        for (Citizen citizen : citizens) {
            String id = citizen.getNationalID();
            if (id.equals(nationalID)) return citizen;
        }
        throw new IllegalArgumentException("No citizen found with the given National ID.");
    }

    @Override
    public void assignAddress(Simulatable sim, int x, int y) {
        if (0 <= x && x <= 10 && 0 <= y && y <= 10) {
            if (sim instanceof Citizen) {
                Citizen citizen = (Citizen) sim;
                gameView.removeSimulatableOnWorldMap(citizen);
                citizen.setLocation(world[x][y]);
                gameView.addSimulatableOnWorldMap(citizen);
            } else if (sim instanceof Unit) {
                Unit unit = (Unit) sim;
                gameView.removeSimulatableOnWorldMap(unit);
                unit.setLocation(world[x][y]);
                gameView.addSimulatableOnWorldMap(unit);
            }
        }
    }

    public ArrayList<Unit> getEmergencyUnits() {
        return emergencyUnits;
    }

    public void setEmergencyService(SOSListener sosListener) {
        this.emergencyService = sosListener;
    }

    public boolean checkGameOver() {
        if (plannedDisasters.size() > 0) return false;
        for (Disaster disaster : executedDisasters) {
            if (disaster.isActive()) {
                Rescuable target = disaster.getTarget();
                if (target instanceof ResidentialBuilding) {
                    ResidentialBuilding building = (ResidentialBuilding) target;
                    if (building.getStructuralIntegrity() != 0) return false;
                } else {
                    Citizen citizen = (Citizen) target;
                    if (citizen.getState() != CitizenState.DECEASED) return false;
                }
            }
        }
        for (Unit unit : emergencyUnits) {
            if (unit.getState() != UnitState.IDLE) return false;
        }
        return true;
    }

    public int calculateCasualties() {
        int casualties = 0;
        for (Citizen citizen : citizens) {
            if (citizen.getState() == CitizenState.DECEASED) casualties++;
        }
        return casualties;
    }

    public void nextCycle() throws DisasterException {
        currentCycle++;

        StringBuilder cycleLog = new StringBuilder();
        boolean[] wasCollapsed = new boolean[buildings.size()];
        boolean[] wasDead = new boolean[citizens.size()];
        for (int i = 0; i < buildings.size(); i++) {
            wasCollapsed[i] = buildings.get(i).getStructuralIntegrity() == 0;
        }
        for (int i = 0; i < citizens.size(); i++) {
            wasDead[i] = citizens.get(i).getState() == CitizenState.DECEASED;
        }

        for (int i = 0; i < plannedDisasters.size(); ) {
            Disaster disaster = plannedDisasters.get(i);
            if (disaster.getStartCycle() == currentCycle) {
                plannedDisasters.remove(disaster);
                Disaster newDisaster = null;
                if (disaster.getTarget() instanceof ResidentialBuilding) {
                    ResidentialBuilding building = (ResidentialBuilding) disaster.getTarget();
                    Disaster currentDisaster = building.getDisaster();
                    if (currentDisaster instanceof GasLeak && disaster instanceof Fire) {
                        int gasLevel = building.getGasLevel();
                        if (gasLevel == 0) {
                            newDisaster = disaster;
                        } else if (0 < gasLevel && gasLevel < 70) {
                            newDisaster = new Collapse(currentCycle, building);
                        } else if (70 <= gasLevel) {
                            building.setStructuralIntegrity(0);
                        }
                    } else if (currentDisaster instanceof Fire && disaster instanceof GasLeak) {
                        newDisaster = new Collapse(currentCycle, building);
                    } else {
                        newDisaster = disaster;
                    }
                    if (newDisaster != null && building.getFireDamage() != 100) {
                        if (newDisaster instanceof Collapse) {
                            for (Disaster exDisaster : executedDisasters) {
                                if (exDisaster.isActive() && exDisaster.getTarget() == building) {
                                    exDisaster.setActive(false);
                                }
                            }
                            building.setFireDamage(0);
                        }
                        newDisaster.strike();
                        executedDisasters.add(newDisaster);
                        cycleLog.append(newDisaster.getClass().getSimpleName()).append(" occurred on ResidentialBuilding at (")
                                .append(building.getLocation().getX()).append(",").append(building.getLocation().getY()).append(").\n");
                    }
                } else {
                    Citizen citizen = (Citizen) disaster.getTarget();
                    disaster.strike();
                    executedDisasters.add(disaster);
                    cycleLog.append(disaster.getClass().getSimpleName()).append(" occurred on Citizen at (")
                            .append(citizen.getLocation().getX()).append(",").append(citizen.getLocation().getY()).append(").\n");
                }
            } else {
                i++;
            }
        }
        for (ResidentialBuilding building : buildings) {
            if (building.getFireDamage() == 100) {
                Collapse collapse = new Collapse(currentCycle, building);
                for (Disaster exDisaster : executedDisasters) {
                    if (exDisaster.isActive() && exDisaster.getTarget() == building) {
                        exDisaster.setActive(false);
                    }
                }
                building.setFireDamage(0);
                collapse.strike();
                executedDisasters.add(collapse);
            }
        }
        for (Unit unit : emergencyUnits) {
            unit.cycleStep();
        }
        for (Disaster disaster : executedDisasters) {
            if (disaster.isActive() && disaster.getStartCycle() < currentCycle) disaster.cycleStep();
        }
        for (int i = 0; i < buildings.size(); i++) {
            ResidentialBuilding building = buildings.get(i);
            building.cycleStep();
            if (!wasCollapsed[i] && building.getStructuralIntegrity() == 0) {
                cycleLog.append("ResidentialBuilding has just collapsed at location (")
                        .append(building.getLocation().getX()).append(",")
                        .append(building.getLocation().getY()).append(").\n");
            }
        }
        for (int i = 0; i < citizens.size(); i++) {
            Citizen citizen = citizens.get(i);
            citizen.cycleStep();
            if (!wasDead[i] && citizen.getState() == CitizenState.DECEASED) {
                cycleLog.append("Citizen ").append(citizen.getName()).append(" has just died at location (")
                        .append(citizen.getLocation().getX()).append(",")
                        .append(citizen.getLocation().getY()).append(").\n");
            }
        }

        if (cycleLog.length() > 0) {
            cycleLog.insert(0, "Cycle " + currentCycle + ":\n");
            gameView.addToLog(cycleLog.toString());
        }
        gameView.setCycleInfo(currentCycle, calculateCasualties());
        gameView.invalidateUnitsPanel();
        StringBuilder activeDisasters = new StringBuilder();
        for (Disaster disaster : executedDisasters) {
            if (disaster.isActive()) {
                Rescuable rescuable = disaster.getTarget();
                activeDisasters.append(disaster.getClass().getSimpleName()).append(" on ")
                        .append(rescuable.getClass().getSimpleName()).append(" at (")
                        .append(rescuable.getLocation().getX()).append(",")
                        .append(rescuable.getLocation().getY()).append(")\n");
            }
        }
        gameView.setActiveDisasters(activeDisasters.toString().trim());
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }
}
