package view;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private JPanel infoPanel, gridPanel, unitsPanel;
    private JPanel availableUnits, respondingUnits, treatingUnits;
    private JLabel cycleInfo;
    private JTextArea blockInfo;

    private static final int PADDING = 10;

    public GameView() {
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
        infoPanel.setPreferredSize(new Dimension(200, getHeight()));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, 0));

        JButton nextCycle = new JButton("Next Cycle");
        nextCycle.setPreferredSize(new Dimension(200, 50));

        cycleInfo = new JLabel("<html>Current Cycle: 1<br>Casualties: 0</html>");
        cycleInfo.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, PADDING, 0));

        blockInfo = new JTextArea();
        blockInfo.setPreferredSize(new Dimension(200, getHeight()));
        blockInfo.setEditable(false);
        blockInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        infoPanel.add(nextCycle);
        infoPanel.add(cycleInfo);
        infoPanel.add(blockInfo);
        add(infoPanel, BorderLayout.WEST);
    }

    private void initGridPanel() {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton jButton = new JButton();
                gridPanel.add(jButton);
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
        JButton button1 = new JButton(new GameIcon("ambulance.png").resize(GameIcon.Size.UNIT));
        JButton button2 = new JButton(new GameIcon("disease_control.png").resize(GameIcon.Size.UNIT));
        JButton button3 = new JButton(new GameIcon("fire_truck.png").resize(GameIcon.Size.UNIT));
        JButton button4 = new JButton(new GameIcon("gas_control.png").resize(GameIcon.Size.UNIT));
        JButton button5 = new JButton(new GameIcon("evacuator.png").resize(GameIcon.Size.UNIT));
        availableUnits.add(button1);
        availableUnits.add(button2);
        availableUnits.add(button3);
        availableUnits.add(button4);
        availableUnits.add(button5);
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

    public void setCycleInfo(int cycleNumber, int casualties) {
        cycleInfo.setText("<html>Current Cycle: " + cycleNumber + "<br>Casualties: " + casualties + "</html>");
    }
}
