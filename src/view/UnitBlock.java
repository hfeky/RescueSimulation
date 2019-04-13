package view;

import model.units.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnitBlock extends JButton {

    private Unit unit;

    public UnitBlock(Unit unit, JTextArea blockInfo) {
        this.unit = unit;
        requestLayout();
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                UnitBlock.this.setBackground(Color.GREEN);
                blockInfo.setText(unit.toString());
            }

            public void mouseExited(MouseEvent e) {
                UnitBlock.this.setBackground(UIManager.getColor("control"));
                blockInfo.setText(null);
            }
        });
    }

    public Unit getUnit() {
        return unit;
    }

    private void requestLayout() {
        Icon icon = null;
        if (unit instanceof Ambulance) {
            icon = new GameIcon("ambulance.png").resize(GameIcon.Size.UNIT_BLOCK);
        } else if (unit instanceof DiseaseControlUnit) {
            icon = new GameIcon("disease_control.png").resize(GameIcon.Size.UNIT_BLOCK);
        } else if (unit instanceof FireTruck) {
            icon = new GameIcon("fire_truck.png").resize(GameIcon.Size.UNIT_BLOCK);
        } else if (unit instanceof GasControlUnit) {
            icon = new GameIcon("gas_control.png").resize(GameIcon.Size.UNIT_BLOCK);
        } else if (unit instanceof Evacuator) {
            icon = new GameIcon("evacuator.png").resize(GameIcon.Size.UNIT_BLOCK);
        }
        setIcon(icon);
    }
}
