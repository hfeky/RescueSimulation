package view;

import model.disasters.Disaster;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulatable;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private JPanel infoPanel, gridPanel, unitsPanel;
    private JPanel availableUnits, respondingUnits, treatingUnits;
    private JLabel cycleInfo;
    private JButton nextCycle;
    private JTextArea disastersInfo;
    private JTextArea logInfo;

    private static final int PADDING = 10;

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

        disastersInfo = new JTextArea();
        disastersInfo.setEditable(false);
        disastersInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane blockScrollPane = new JScrollPane(disastersInfo);
        blockScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() / 2));
        blockScrollPane.setBorder(BorderFactory.createTitledBorder("Active Disasters"));

        logInfo = new JTextArea();
        logInfo.setEditable(false);
        logInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane logScrollPane = new JScrollPane(logInfo);
        logScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() / 2));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Game Log"));

        infoPanel.add(blockScrollPane);
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
                WorldBlock worldBlock = new WorldBlock();
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
        } else if (simulatable instanceof Unit){
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
        } else if (simulatable instanceof Unit){
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
