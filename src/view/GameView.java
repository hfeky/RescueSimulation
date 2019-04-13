package view;

import controller.CommandCenter;
import exceptions.DisasterException;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulatable;
import simulation.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameView extends JFrame {

    private JPanel infoPanel, gridPanel, unitsPanel;
    private JPanel availableUnits, respondingUnits, treatingUnits;
    private JLabel cycleInfo;
    private JTextArea blockInfo;
    private JTextArea logInfo;
    private ArrayList<WorldBlock> gridBlocks = new ArrayList<>();

    private static final int PADDING = 10;

    private CommandCenter commandCenter;
    private Simulator engine;

    public GameView(CommandCenter commandCenter, Simulator engine) {
        this.commandCenter = commandCenter;
        this.engine = engine;
        setTitle("Rescue Simulation");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(new Rectangle(new Dimension(800, 600)));
        setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);

        initInfoPanel();
        initGridPanel();
        initUnitsPanel();

        setVisible(true);
    }

    private void initInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(250, getHeight()));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, 0));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        JButton nextCycle = new JButton("Next Cycle");
        nextCycle.setPreferredSize(new Dimension(250 - PADDING, 50));
        nextCycle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!engine.checkGameOver()) {
                    try {
                        engine.nextCycle();
                        setCycleInfo(engine.getCurrentCycle(), engine.calculateCasualties());
                    } catch (DisasterException de) {
                        de.printStackTrace();
                    }
                }
            }
        });
        controlPanel.add(nextCycle);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        cycleInfo = new JLabel("<html>Current Cycle: 0<br>Casualties: 0</html>");
        cycleInfo.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, PADDING, 0));
        statusPanel.add(cycleInfo);

        blockInfo = new JTextArea();
        blockInfo.setEditable(false);
        blockInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        blockInfo.setBorder(BorderFactory.createTitledBorder("Block Info"));

        logInfo = new JTextArea();
        logInfo.setEditable(false);
        logInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logInfo.setBorder(BorderFactory.createTitledBorder("Game Log"));

        infoPanel.add(controlPanel);
        infoPanel.add(statusPanel);
        infoPanel.add(blockInfo);
        infoPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));
        infoPanel.add(logInfo);
        add(infoPanel, BorderLayout.WEST);
    }

    private void initGridPanel() {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                WorldBlock worldBlock = new WorldBlock(blockInfo);
                gridPanel.add(worldBlock);
                gridBlocks.add(worldBlock);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    private void initUnitsPanel() {
        unitsPanel = new JPanel();
        unitsPanel.setPreferredSize(new Dimension(200, getHeight()));
        unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.PAGE_AXIS));
        unitsPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, PADDING, PADDING));

        availableUnits = new JPanel();
        availableUnits.setLayout(new GridLayout(3, 2));
        availableUnits.setBorder(BorderFactory.createTitledBorder("Available Units"));
        for (Unit unit : engine.getEmergencyUnits()) {
            UnitBlock unitBlock = new UnitBlock(unit, blockInfo);
            availableUnits.add(unitBlock);
        }
        unitsPanel.add(availableUnits);
        unitsPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));

        respondingUnits = new JPanel();
        respondingUnits.setLayout(new GridLayout(3, 2));
        respondingUnits.setBorder(BorderFactory.createTitledBorder("Responding Units"));
        unitsPanel.add(respondingUnits);
        unitsPanel.add(Box.createRigidArea(new Dimension(0, PADDING)));

        treatingUnits = new JPanel();
        treatingUnits.setLayout(new GridLayout(3, 2));
        treatingUnits.setBorder(BorderFactory.createTitledBorder("Treating Units"));
        unitsPanel.add(treatingUnits);

        add(unitsPanel, BorderLayout.EAST);
    }

    public void addRescuableOnWorldMap(Rescuable rescuable) {
        WorldBlock worldBlock = gridBlocks.get(rescuable.getLocation().getY() * 10 + rescuable.getLocation().getX());
        worldBlock.addSimulatable((Simulatable) rescuable);
    }

    public void setCycleInfo(int cycleNumber, int casualties) {
        cycleInfo.setText("<html>Current Cycle: " + cycleNumber + "<br>Casualties: " + casualties + "</html>");
    }
}
