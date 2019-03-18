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

    public Citizen(Address location, String nationalID, String name, int age, WorldListener worldListener) {
        this.location = location;
        this.nationalID = nationalID;
        this.name = name;
        this.age = age;
        setWorldListener(worldListener);
    }

    public CitizenState getState() {
        return state;
    }

    public void setState(CitizenState state) {
        this.state = state;
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
        if (0 <= hp && hp <= 100) {
            this.hp = hp;
            if (hp == 0) state = CitizenState.DECEASED;
        }
    }

    public int getBloodLoss() {
        return bloodLoss;
    }

    public void setBloodLoss(int bloodLoss) {
        if (0 <= bloodLoss && bloodLoss <= 100) {
            this.bloodLoss = bloodLoss;
            if (bloodLoss == 100) setHp(0);
        }
    }

    public int getToxicity() {
        return toxicity;
    }

    public void setToxicity(int toxicity) {
        if (0 <= toxicity && toxicity <= 100) {
            this.toxicity = toxicity;
            if (toxicity == 100) setHp(0);
        }
    }

    @Override
    public void cycleStep() {
        if (0 < bloodLoss && bloodLoss < 30) {
            hp -= 5;
        } else if (30 <= bloodLoss && bloodLoss < 70) {
            hp -= 10;
        } else if (70 <= bloodLoss) {
            hp -= 15;
        }

        if (0 < toxicity && toxicity < 30) {
            hp -= 5;
        } else if (30 <= toxicity && toxicity < 70) {
            hp -= 10;
        } else if (70 <= toxicity) {
            hp -= 15;
        }
    }

    @Override
    public void struckBy(Disaster d) {
        state = IN_TROUBLE;
        emergencyService.receiveSOSCall(this);
    }

    public void setSOSListener(SOSListener sosListener) {
        this.emergencyService = sosListener;
    }

    public WorldListener getWorldListener() {
        return worldListener;
    }

    public void setWorldListener(WorldListener worldListener) {
        this.worldListener = worldListener;
    }
}
