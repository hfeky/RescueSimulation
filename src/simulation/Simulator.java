package simulation;

import model.disasters.*;
import model.events.SOSListener;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.*;

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
                citizen.setLocation(world[x][y]);
            } else if (sim instanceof Unit) {
                Unit unit = (Unit) sim;
                unit.setLocation(world[x][y]);
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
            if (disaster.isActive()) return false;
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

    public void nextCycle() {
        if (!checkGameOver()) {
            currentCycle++;
            for (int i = 0; i < plannedDisasters.size();) {
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
                            newDisaster.strike();
                            executedDisasters.add(newDisaster);
                        }
                    } else {
                        disaster.strike();
                        executedDisasters.add(disaster);
                    }
                } else {
                    i++;
                }
            }
            for (ResidentialBuilding building : buildings) {
                if (building.getFireDamage() == 100) {
                    Collapse collapse = new Collapse(currentCycle, building);
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
            for (ResidentialBuilding building : buildings) {
                building.cycleStep();
            }
            for (Citizen citizen : citizens) {
                citizen.cycleStep();
            }
        }
    }
}
