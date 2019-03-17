package model.people;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

import static model.people.CitizenState.IN_TROUBLE;

public class Citizen implements Simulatable, Rescuable {

    private Address location;
    private String nationalID, name;
    private int age;
    private CitizenState state = CitizenState.SAFE;
    private int hp = 100;
    private int bloodLoss, toxicity;
    private Disaster disaster;
    private SOSListener emergencyService;
    private WorldListener worldListener;

    public Citizen(Address location, String nationalID, String name, int age) {
        this.location = location;
        this.nationalID = nationalID;
        this.name = name;
        this.age = age;
    }

    public WorldListener getWorldListener() {
        return worldListener;
    }

    public void setWorldListener(WorldListener worldListener) {
        this.worldListener = worldListener;
    }

    public void setEmergencyService(SOSListener emergencyService) {
        this.emergencyService = emergencyService;
    }

    public CitizenState getState() {
        return state;
    }

    public void setState(CitizenState state) {
        this.state = state;
    }

    @Override
    public void struckBy(Disaster d) {
        state = IN_TROUBLE;

    }

    public Disaster getDisaster() {
        return disaster;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public String getNationalID() {
        return nationalID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp == 0) state = CitizenState.DECEASED;
        else if (hp > 0 && hp <= 100) this.hp = hp;
    }

    public int getBloodLoss() {
        return bloodLoss;
    }

    public void setBloodLoss(int bloodLoss) {
        if (bloodLoss == 100) setHp(0);
        else if (bloodLoss >= 0 && bloodLoss < 100) this.bloodLoss = bloodLoss;
    }

    public int getToxicity() {
        return toxicity;
    }

    public void setToxicity(int toxicity) {
        if (toxicity == 100) setHp(0);
        else if (toxicity >= 0 && toxicity < 100) this.toxicity = toxicity;
    }

    @Override
    public void cycleStep() {
        if (bloodLoss > 0 && bloodLoss < 30) hp -= 5;
        else if (bloodLoss >= 30 && bloodLoss < 70) hp -= 10;
        else if (bloodLoss >= 70) hp -= 15;

        if (toxicity > 0 && toxicity < 30) hp -= 5;
        else if (toxicity >= 30 && toxicity < 70) hp -= 10;
        else if (toxicity >= 70) hp -= 15;
    }
}
