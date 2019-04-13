package view;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.*;
import simulation.Simulatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class WorldBlock extends JButton {

    private ArrayList<Simulatable> simulatables = new ArrayList<>();

    public WorldBlock(JTextArea blockInfo) {
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                WorldBlock.this.setBackground(Color.GREEN);
                for (Simulatable simulatable : simulatables) {
                    blockInfo.setText((blockInfo.getText() + "\n" + simulatable.toString()).trim());
                }
            }

            public void mouseExited(MouseEvent e) {
                WorldBlock.this.setBackground(UIManager.getColor("control"));
                blockInfo.setText(null);
            }
        });
    }

    public void addSimulatable(Simulatable simulatable) {
        if (!simulatables.contains(simulatable)) {
            simulatables.add(simulatable);
            requestLayout();
        }
    }

    public void removeSimulatable(Simulatable simulatable) {
        simulatables.remove(simulatable);
        requestLayout();
    }

    private void requestLayout() {
        Icon icon = null;
        // TODO: Stack icons in one image
        for (Simulatable simulatable : simulatables) {
            if (simulatable instanceof Citizen) {
                icon = new GameIcon("citizen.png").resize(GameIcon.Size.WORLD_BLOCK);
            } else if (simulatable instanceof ResidentialBuilding) {
                icon = new GameIcon("building.png").resize(GameIcon.Size.WORLD_BLOCK);
            } else if (simulatable instanceof Ambulance) {
                icon = new GameIcon("ambulance.png").resize(GameIcon.Size.WORLD_BLOCK);
            } else if (simulatable instanceof DiseaseControlUnit) {
                icon = new GameIcon("disease_control.png").resize(GameIcon.Size.WORLD_BLOCK);
            } else if (simulatable instanceof FireTruck) {
                icon = new GameIcon("fire_truck.png").resize(GameIcon.Size.WORLD_BLOCK);
            } else if (simulatable instanceof GasControlUnit) {
                icon = new GameIcon("gas_control.png").resize(GameIcon.Size.WORLD_BLOCK);
            } else if (simulatable instanceof Evacuator) {
                icon = new GameIcon("evacuator.png").resize(GameIcon.Size.WORLD_BLOCK);
            }
        }
        setIcon(icon);
    }
}
