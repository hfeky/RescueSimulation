package simulation;

import model.disasters.*;
import model.events.SOSListener;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulator implements WorldListener {

    private ArrayList<ResidentialBuilding> buildings = new ArrayList<>();
    private ArrayList<Citizen> citizens = new ArrayList<>();



    private SOSListener emergencyService;



    private ArrayList<Unit> emergencyUnits = new ArrayList<>();
    private ArrayList<Disaster> plannedDisasters = new ArrayList<>();
    private ArrayList<Disaster> executedDisasters = new ArrayList<>();

    private Address[][] world = new Address[10][10];
    private int currentCycle;

    public Simulator() throws Exception {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = new Address(i, j);
            }
        }
        loadBuildings("buildings.csv");
        loadCitizens("citizens.csv");
        loadUnits("units.csv");
        loadDisasters("disasters.csv");
    }

    private void loadBuildings(String filePath) throws Exception {
        Scanner input = new Scanner(new File(filePath));
        while (input.hasNext()) {
            String[] cells = input.nextLine().split(",");
            buildings.add(new ResidentialBuilding(world[Integer.parseInt(cells[0])][Integer.parseInt(cells[1])]));
        }
    }

    private void loadCitizens(String filePath) throws Exception {
        Scanner input = new Scanner(new File(filePath));
        while (input.hasNext()) {
            String[] cells = input.nextLine().split(",");
            Citizen citizen = new Citizen(world[Integer.parseInt(cells[0])][Integer.parseInt(cells[1])],
                    cells[2], cells[3], Integer.parseInt(cells[4]));
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
                    emergencyUnits.add(new Ambulance(cells[1], world[0][0], Integer.parseInt(cells[2])));
                    break;
                case "DCU":
                    emergencyUnits.add(new DiseaseControlUnit(cells[1], world[0][0], Integer.parseInt(cells[2])));
                    break;
                case "EVC":
                    emergencyUnits.add(new Evacuator(cells[1], world[0][0], Integer.parseInt(cells[2]), Integer.parseInt(cells[3])));
                    break;
                case "FTK":
                    emergencyUnits.add(new FireTruck(cells[1], world[0][0], Integer.parseInt(cells[2])));
                    break;
                case "GCU":
                    emergencyUnits.add(new GasControlUnit(cells[1], world[0][0], Integer.parseInt(cells[2])));
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
        //sim.setLocation(new Address(x,y));
    }
    public ArrayList<Unit> getEmergencyUnits() {
        return emergencyUnits;
    }

    public void setEmergencyService(SOSListener emergencyService) {
        this.emergencyService = emergencyService;
    }
}
