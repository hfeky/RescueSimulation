package view;

import javafx.util.Pair;
import model.disasters.*;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.*;
import simulation.Rescuable;
import simulation.Simulatable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GameView extends JFrame {

    private JPanel infoPanel, gridPanel, unitsPanel;
    private JPanel availableUnits, respondingUnits, treatingUnits;
    private JLabel cycleInfo;
    private JButton nextCycle;
    private JTextArea recommenderInfo;
    private JTextArea disastersInfo;
    private JTextArea logInfo;

    private static final int PADDING = 10;
    private static final int TEXT_AREA_MARGIN = 4;

    public GameView() {
        setTitle("Rescue Simulation");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(new Rectangle(new Dimension(1500, 900)));
        setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);

        initInfoPanel();
        initGridPanel();
        initUnitsPanel();

        setVisible(true);
    }

    private void initInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(450, getHeight()));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, 0));

        recommenderInfo = new JTextArea();
        recommenderInfo.setEditable(false);
        recommenderInfo.setBackground(null);
        recommenderInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        recommenderInfo.setMargin(new Insets(TEXT_AREA_MARGIN, TEXT_AREA_MARGIN, TEXT_AREA_MARGIN, TEXT_AREA_MARGIN));
        JScrollPane recommenderScrollPane = new JScrollPane(recommenderInfo);
        recommenderScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));
        recommenderScrollPane.setBorder(BorderFactory.createTitledBorder("Recommendations"));

        disastersInfo = new JTextArea();
        disastersInfo.setEditable(false);
        disastersInfo.setBackground(null);
        disastersInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        disastersInfo.setMargin(new Insets(TEXT_AREA_MARGIN, TEXT_AREA_MARGIN, TEXT_AREA_MARGIN, TEXT_AREA_MARGIN));
        JScrollPane disastersScrollPane = new JScrollPane(disastersInfo);
        disastersScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));
        disastersScrollPane.setBorder(BorderFactory.createTitledBorder("Active Disasters"));

        logInfo = new JTextArea();
        logInfo.setEditable(false);
        logInfo.setBackground(null);
        logInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logInfo.setMargin(new Insets(TEXT_AREA_MARGIN, TEXT_AREA_MARGIN, TEXT_AREA_MARGIN, TEXT_AREA_MARGIN));
        JScrollPane logScrollPane = new JScrollPane(logInfo);
        logScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Game Log"));

        infoPanel.add(recommenderScrollPane);
        infoPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));
        infoPanel.add(disastersScrollPane);
        infoPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));
        infoPanel.add(logScrollPane);
        add(infoPanel, BorderLayout.WEST);
    }

    private void initGridPanel() {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                WorldBlock worldBlock = new WorldBlock(this);
                gridPanel.add(worldBlock);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    private void initUnitsPanel() {
        unitsPanel = new JPanel();
        unitsPanel.setPreferredSize(new Dimension(200, getHeight()));
        unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.PAGE_AXIS));
        unitsPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, PADDING, PADDING));

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        cycleInfo = new JLabel("<html>Current Cycle: 0<br>Casualties: 0</html>");
        statusPanel.add(cycleInfo);
        unitsPanel.add(statusPanel);
        unitsPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        nextCycle = new JButton("Next Cycle");
        nextCycle.setPreferredSize(new Dimension(180, 50));
        controlPanel.add(nextCycle);
        unitsPanel.add(controlPanel);
        unitsPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));

        availableUnits = new JPanel();
        availableUnits.setLayout(new GridLayout(3, 2));
        availableUnits.setPreferredSize(new Dimension(getWidth(), 300));
        availableUnits.setBorder(BorderFactory.createTitledBorder("Available Units"));
        unitsPanel.add(availableUnits);
        unitsPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));

        respondingUnits = new JPanel();
        respondingUnits.setLayout(new GridLayout(3, 2));
        respondingUnits.setPreferredSize(new Dimension(getWidth(), 300));
        respondingUnits.setBorder(BorderFactory.createTitledBorder("Responding Units"));
        unitsPanel.add(respondingUnits);
        unitsPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));

        treatingUnits = new JPanel();
        treatingUnits.setLayout(new GridLayout(3, 2));
        treatingUnits.setPreferredSize(new Dimension(getWidth(), 300));
        treatingUnits.setBorder(BorderFactory.createTitledBorder("Treating Units"));
        unitsPanel.add(treatingUnits);

        add(unitsPanel, BorderLayout.EAST);
    }

    public void addSimulatableOnWorldMap(Simulatable simulatable) {
        if (simulatable instanceof Rescuable) {
            ((WorldBlock) gridPanel.getComponent(((Rescuable) simulatable).getLocation().getY() * 10 +
                    ((Rescuable) simulatable).getLocation().getX())).addSimulatable(simulatable);
        } else if (simulatable instanceof Unit) {
            ((WorldBlock) gridPanel.getComponent(((Unit) simulatable).getLocation().getY() * 10 +
                    ((Unit) simulatable).getLocation().getX())).addSimulatable(simulatable);
        } else {
            ((WorldBlock) gridPanel.getComponent(((Disaster) simulatable).getTarget().getLocation().getY() * 10 +
                    ((Disaster) simulatable).getTarget().getLocation().getX())).addSimulatable(simulatable);
        }
    }

    public void removeSimulatableOnWorldMap(Simulatable simulatable) {
        if (simulatable instanceof Rescuable) {
            ((WorldBlock) gridPanel.getComponent(((Rescuable) simulatable).getLocation().getY() * 10 +
                    ((Rescuable) simulatable).getLocation().getX())).removeSimulatable(simulatable);
        } else if (simulatable instanceof Unit) {
            ((WorldBlock) gridPanel.getComponent(((Unit) simulatable).getLocation().getY() * 10 +
                    ((Unit) simulatable).getLocation().getX())).removeSimulatable(simulatable);
        } else {
            ((WorldBlock) gridPanel.getComponent(((Disaster) simulatable).getTarget().getLocation().getY() * 10 +
                    ((Disaster) simulatable).getTarget().getLocation().getX())).removeSimulatable(simulatable);
        }
    }

    public void setCycleInfo(int cycleNumber, int casualties) {
        cycleInfo.setText("<html>Current Cycle: " + cycleNumber + "<br>Casualties: " + casualties + "</html>");
    }

    public void setActiveDisasters(String activeDisasters) {
        disastersInfo.setText(activeDisasters);
    }

    public void addToLog(String text) {
        logInfo.setText((logInfo.getText() + "\n\n" + text).trim());
    }

    public void deselectAllUnits() {
        for (int i = 0; i < getAvailableUnits().getComponentCount(); i++) {
            UnitBlock unitBlock = (UnitBlock) getAvailableUnits().getComponent(i);
            unitBlock.setSelected(false);
        }
        for (int i = 0; i < getRespondingUnits().getComponentCount(); i++) {
            UnitBlock unitBlock = (UnitBlock) getRespondingUnits().getComponent(i);
            unitBlock.setSelected(false);
        }
        for (int i = 0; i < getTreatingUnits().getComponentCount(); i++) {
            UnitBlock unitBlock = (UnitBlock) getTreatingUnits().getComponent(i);
            unitBlock.setSelected(false);
        }
    }

    public void invalidateUnitsPanel() {
        for (int i = 0; i < getAvailableUnits().getComponentCount(); ) {
            UnitBlock unitBlock = (UnitBlock) getAvailableUnits().getComponent(i);
            if (unitBlock.getUnit().getState() == UnitState.RESPONDING) {
                getAvailableUnits().remove(unitBlock);
                getRespondingUnits().add(unitBlock);
            } else if (unitBlock.getUnit().getState() == UnitState.TREATING) {
                getAvailableUnits().remove(unitBlock);
                getTreatingUnits().add(unitBlock);
            } else {
                i++;
            }
        }
        for (int i = 0; i < getRespondingUnits().getComponentCount(); ) {
            UnitBlock unitBlock = (UnitBlock) getRespondingUnits().getComponent(i);
            if (unitBlock.getUnit().getState() == UnitState.IDLE) {
                getRespondingUnits().remove(unitBlock);
                getAvailableUnits().add(unitBlock);
            } else if (unitBlock.getUnit().getState() == UnitState.TREATING) {
                getRespondingUnits().remove(unitBlock);
                getTreatingUnits().add(unitBlock);
            } else {
                i++;
            }
        }
        for (int i = 0; i < getTreatingUnits().getComponentCount(); ) {
            UnitBlock unitBlock = (UnitBlock) getTreatingUnits().getComponent(i);
            if (unitBlock.getUnit().getState() == UnitState.IDLE) {
                getTreatingUnits().remove(unitBlock);
                getAvailableUnits().add(unitBlock);
            } else if (unitBlock.getUnit().getState() == UnitState.RESPONDING) {
                getTreatingUnits().remove(unitBlock);
                getRespondingUnits().add(unitBlock);
            } else {
                i++;
            }
        }
        getAvailableUnits().validate();
        getRespondingUnits().validate();
        getTreatingUnits().validate();
    }

    public void updateGrid() {
        for (int i = 0; i < getGridPanel().getComponentCount(); i++) {
            WorldBlock worldBlock = (WorldBlock) getGridPanel().getComponent(i);
            worldBlock.requestLayout();
        }
    }

    public void recommendMoves(ArrayList<ResidentialBuilding> buildings, ArrayList<Citizen> citizens, ArrayList<Unit> units) {
        StringBuilder recommendation = new StringBuilder();
        for (Unit unit : units) {
            if (unit.getTarget() == null) {
                ArrayList<Pair<Rescuable, Double>> pairs = new ArrayList<>();
                if (unit instanceof Ambulance) {
                    for (Citizen citizen : citizens) {
                        if (citizen.getDisaster() instanceof Injury && !citizen.isDead()) {
                            pairs.add(new Pair<>(citizen, getDistanceDifference(unit, citizen)));
                        }
                    }
                } else if (unit instanceof DiseaseControlUnit) {
                    for (Citizen citizen : citizens) {
                        if (citizen.getDisaster() instanceof Infection && !citizen.isDead()) {
                            pairs.add(new Pair<>(citizen, getDistanceDifference(unit, citizen)));
                        }
                    }
                } else if (unit instanceof Evacuator) {
                    for (ResidentialBuilding building : buildings) {
                        if (building.getDisaster() instanceof Collapse && !building.isCollapsed()) {
                            pairs.add(new Pair<>(building, getDistanceDifference(unit, building) / (building.countAliveOccupants() + 1)));
                        }
                    }
                } else if (unit instanceof FireTruck) {
                    for (ResidentialBuilding building : buildings) {
                        if (building.getDisaster() instanceof Fire && !building.isCollapsed()) {
                            pairs.add(new Pair<>(building, getDistanceDifference(unit, building) / (building.countAliveOccupants() + 1)));
                        }
                    }
                } else if (unit instanceof GasControlUnit) {
                    for (ResidentialBuilding building : buildings) {
                        if (building.getDisaster() instanceof GasLeak && !building.isCollapsed()) {
                            pairs.add(new Pair<>(building, getDistanceDifference(unit, building) / (building.countAliveOccupants() + 1)));
                        }
                    }
                }
                if (!pairs.isEmpty()) {
                    pairs.sort(new Comparator<Pair<Rescuable, Double>>() {
                        @Override
                        public int compare(Pair<Rescuable, Double> o1, Pair<Rescuable, Double> o2) {
                            return (int) (o1.getValue() - o2.getValue());
                        }
                    });
                    recommendation.append(unit.getClass().getSimpleName()).append(" ").append(unit.getUnitID()).append(":\n");
                    for (int i = 1; i <= pairs.size(); i++) {
                        Pair<Rescuable, Double> pair = pairs.get(i - 1);
                        Rescuable rescuable = pair.getKey();
                        if (rescuable instanceof Citizen) {
                            Citizen citizen = (Citizen) pair.getKey();
                            recommendation.append(i).append(". Citizen ").append(citizen.getName()).append(" at (")
                                    .append(citizen.getLocation().getX()).append(",")
                                    .append(citizen.getLocation().getY()).append("), [")
                                    .append(citizen.getHp()).append(" hp]\n");
                        } else if (rescuable instanceof ResidentialBuilding) {
                            ResidentialBuilding building = (ResidentialBuilding) pair.getKey();
                            int aliveOccupants = building.countAliveOccupants();
                            recommendation.append(i).append(". ResidentialBuilding at (")
                                    .append(building.getLocation().getX()).append(",")
                                    .append(building.getLocation().getY()).append("), [")
                                    .append((aliveOccupants == 0) ? "no" : aliveOccupants)
                                    .append(" alive occupants]\n");
                        }
                    }
                    recommendation.append("\n");
                }
            }
        }
        recommenderInfo.setText(recommendation.toString().trim());
    }

    private double getDistanceDifference(Unit unit, Rescuable rescuable) {
        return Math.abs(unit.getLocation().getX() - rescuable.getLocation().getX()) +
                Math.abs(unit.getLocation().getY() - rescuable.getLocation().getY());
    }

    public JButton getNextCycle() {
        return nextCycle;
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }

    public JPanel getAvailableUnits() {
        return availableUnits;
    }

    public JPanel getRespondingUnits() {
        return respondingUnits;
    }

    public JPanel getTreatingUnits() {
        return treatingUnits;
    }
}
