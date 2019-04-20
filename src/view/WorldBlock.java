package view;

import javafx.util.Pair;
import model.disasters.Disaster;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.*;
import simulation.Rescuable;
import simulation.Simulatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WorldBlock extends JButton {

    private Disaster disaster;
    private ArrayList<ResidentialBuilding> buildings = new ArrayList<>();
    private ArrayList<Citizen> citizens = new ArrayList<>();
    private ArrayList<Ambulance> ambulances = new ArrayList<>();
    private ArrayList<DiseaseControlUnit> diseaseUnits = new ArrayList<>();
    private ArrayList<Evacuator> evacuators = new ArrayList<>();
    private ArrayList<FireTruck> fireTrucks = new ArrayList<>();
    private ArrayList<GasControlUnit> gasUnits = new ArrayList<>();

    private final int DISASTER_COLOR = 0xFF4FC3F7;
    private final int DEAD_COLOR = 0xFFB71C1C;

    public WorldBlock() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                WorldBlock.this.setBackground(Color.GREEN);
                StringBuilder blockInfo = new StringBuilder();
                if (buildings.size() > 0) {
                    blockInfo = addToBlockInfo(blockInfo, buildings);
                } else if (citizens.size() > 0) {
                    blockInfo = addToBlockInfo(blockInfo, citizens);
                }
                blockInfo = addToBlockInfo(blockInfo, ambulances);
                blockInfo = addToBlockInfo(blockInfo, diseaseUnits);
                blockInfo = addToBlockInfo(blockInfo, evacuators);
                blockInfo = addToBlockInfo(blockInfo, fireTrucks);
                blockInfo = addToBlockInfo(blockInfo, gasUnits);
                if (blockInfo.length() > 0) {
                    setToolTipText("<html>" + blockInfo.toString().trim().replaceAll("\n", "<br>") + "</html>");
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                updateBackgroundColor();
            }
        });
    }

    @Override
    public void invalidate() {
        super.invalidate();
        requestLayout();
    }

    private StringBuilder addToBlockInfo(StringBuilder blockInfo, ArrayList<? extends Simulatable> list) {
        if (list.size() == 1) {
            Simulatable simulatable = list.get(0);
            blockInfo.append("\n\n<b>").append(simulatable.getClass().getSimpleName()).append(":</b>\n").append(simulatable.toString());
        } else {
            for (int i = 1; i <= list.size(); i++) {
                Simulatable simulatable = list.get(i - 1);
                blockInfo.append("\n\n<b>").append(simulatable.getClass().getSimpleName()).append(" ").append(i).append(":</b>\n").append(simulatable.toString());
            }
        }
        return blockInfo;
    }

    public void setDisaster(Disaster disaster) {
        this.disaster = disaster;
    }

    public void addSimulatable(Simulatable simulatable) {
        if (simulatable instanceof ResidentialBuilding) {
            if (!buildings.contains(simulatable)) {
                buildings.add((ResidentialBuilding) simulatable);
            }
        } else if (simulatable instanceof Citizen) {
            if (!citizens.contains(simulatable)) {
                citizens.add((Citizen) simulatable);
            }
        } else if (simulatable instanceof Ambulance) {
            if (!ambulances.contains(simulatable)) {
                ambulances.add((Ambulance) simulatable);
            }
        } else if (simulatable instanceof DiseaseControlUnit) {
            if (!diseaseUnits.contains(simulatable)) {
                diseaseUnits.add((DiseaseControlUnit) simulatable);
            }
        } else if (simulatable instanceof Evacuator) {
            if (!evacuators.contains(simulatable)) {
                evacuators.add((Evacuator) simulatable);
            }
        } else if (simulatable instanceof FireTruck) {
            if (!fireTrucks.contains(simulatable)) {
                fireTrucks.add((FireTruck) simulatable);
            }
        } else if (simulatable instanceof GasControlUnit) {
            if (!gasUnits.contains(simulatable)) {
                gasUnits.add((GasControlUnit) simulatable);
            }
        } else if (simulatable instanceof Disaster) {
            this.disaster = (Disaster) simulatable;
        }
        requestLayout();
    }

    public void removeSimulatable(Simulatable simulatable) {
        if (simulatable instanceof ResidentialBuilding) {
            buildings.remove(simulatable);
        } else if (simulatable instanceof Citizen) {
            citizens.remove(simulatable);
        } else if (simulatable instanceof Ambulance) {
            ambulances.remove(simulatable);
        } else if (simulatable instanceof DiseaseControlUnit) {
            diseaseUnits.remove(simulatable);
        } else if (simulatable instanceof Evacuator) {
            evacuators.remove(simulatable);
        } else if (simulatable instanceof FireTruck) {
            fireTrucks.remove(simulatable);
        } else if (simulatable instanceof GasControlUnit) {
            gasUnits.remove(simulatable);
        } else if (simulatable instanceof Disaster) {
            this.disaster = null;
        }
        requestLayout();
    }

    public Rescuable getMainRescuable() {
        return buildings.size() > 0 ? buildings.get(0) : citizens.size() > 0 ? citizens.get(0) : null;
    }

    private ArrayList<Pair<String, Integer>> getAllPairs() {
        ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();
        if (buildings.size() > 0) pairs.add(new Pair<>("building", buildings.size()));
        if (ambulances.size() > 0) pairs.add(new Pair<>("ambulance", ambulances.size()));
        if (diseaseUnits.size() > 0) pairs.add(new Pair<>("disease_control", diseaseUnits.size()));
        if (evacuators.size() > 0) pairs.add(new Pair<>("evacuator", evacuators.size()));
        if (fireTrucks.size() > 0) pairs.add(new Pair<>("fire_truck", fireTrucks.size()));
        if (gasUnits.size() > 0) pairs.add(new Pair<>("gas_control", gasUnits.size()));
        if (citizens.size() > 0) pairs.add(new Pair<>("citizen", citizens.size()));
        return pairs;
    }

    public void requestLayout() {
        if (getWidth() != 0 && getHeight() != 0) {
            updateBackgroundColor();
            BufferedImage bufferedImage = new BufferedImage(90, 80, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = bufferedImage.getGraphics();
            ArrayList<Pair<String, Integer>> pairs = getAllPairs();
            if (pairs.size() == 6) {
                graphics.drawImage(getIcon(pairs.get(0).getKey()), 0, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(1).getKey()), 30, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(2).getKey()), 60, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(3).getKey()), 0, 40, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(4).getKey()), 30, 40, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(5).getKey()), 60, 40, 30, 30, this);
            } else if (pairs.size() == 5) {
                graphics.drawImage(getIcon(pairs.get(0).getKey()), 0, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(1).getKey()), 30, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(2).getKey()), 60, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(3).getKey()), 15, 40, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(4).getKey()), 45, 40, 30, 30, this);
            } else if (pairs.size() == 4) {
                graphics.drawImage(getIcon(pairs.get(0).getKey()), 15, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(1).getKey()), 45, 10, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(2).getKey()), 15, 40, 30, 30, this);
                graphics.drawImage(getIcon(pairs.get(3).getKey()), 45, 40, 30, 30, this);
            } else if (pairs.size() == 3) {
                if (pairs.get(0).getKey().equals("building") && pairs.get(2).getKey().equals("citizen")) {
                    graphics.drawImage(getIcon(pairs.get(0).getKey()), 5, 0, 80, 80, this);
                    graphics.drawImage(getIcon(pairs.get(2).getKey()), 0, 35, 45, 45, this);
                    graphics.drawImage(getIcon(pairs.get(1).getKey()), 45, 35, 45, 45, this);
                } else {
                    graphics.drawImage(getIcon(pairs.get(0).getKey()), 15, 10, 30, 30, this);
                    graphics.drawImage(getIcon(pairs.get(1).getKey()), 45, 10, 30, 30, this);
                    graphics.drawImage(getIcon(pairs.get(2).getKey()), 30, 40, 30, 30, this);
                }
            } else if (pairs.size() == 2) {
                if (pairs.get(0).getKey().equals("building")) {
                    if (pairs.get(1).getKey().equals("citizen")) {
                        graphics.drawImage(getIcon(pairs.get(0).getKey()), 5, 0, 80, 80, this);
                        graphics.drawImage(getIcon(pairs.get(1).getKey()), 0, 35, 45, 45, this);
                    } else {
                        graphics.drawImage(getIcon(pairs.get(0).getKey()), 5, 0, 80, 80, this);
                        graphics.drawImage(getIcon(pairs.get(1).getKey()), 45, 35, 45, 45, this);
                    }
                } else if (pairs.get(1).getKey().equals("citizen")) {
                    graphics.drawImage(getIcon(pairs.get(0).getKey()), 5, 0, 80, 80, this);
                    graphics.drawImage(getIcon(pairs.get(1).getKey()), 45, 35, 45, 45, this);
                } else {
                    graphics.drawImage(getIcon(pairs.get(0).getKey()), 15, 25, 30, 30, this);
                    graphics.drawImage(getIcon(pairs.get(1).getKey()), 45, 25, 30, 30, this);
                }
            } else if (pairs.size() == 1) {
                graphics.drawImage(getIcon(pairs.get(0).getKey()), 5, 0, 80, 80, this);
            }
            setIcon(new ImageIcon(bufferedImage));
        }
    }

    private void updateBackgroundColor() {
        Rescuable rescuable = getMainRescuable();
        if (rescuable != null &&
                ((rescuable instanceof ResidentialBuilding && ((ResidentialBuilding) rescuable).isCollapsed())
                        || (rescuable instanceof Citizen && ((Citizen) rescuable).isDead()))) {
            setBackground(new Color(DEAD_COLOR));
        } else if (rescuable != null && rescuable.getDisaster() != null && rescuable.getDisaster().isActive()) {
            setBackground(new Color(DISASTER_COLOR));
        } else {
            setBackground(UIManager.getColor("control"));
        }
    }

    private Image getIcon(String iconName) {
        return new ImageIcon("assets/ico/" + iconName + ".png").getImage();
    }
}
