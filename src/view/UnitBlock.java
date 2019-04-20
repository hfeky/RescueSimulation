package view;

import model.units.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnitBlock extends JToggleButton {

    private Unit unit;

    public UnitBlock(Unit unit) {
        this.unit = unit;
        requestLayout();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                UnitBlock.this.setBackground(Color.GREEN);
                String blockInfo = "<b>" + unit.getClass().getSimpleName() + ":</b>\n" + unit.toString();
                if (!blockInfo.isEmpty()) {
                    setToolTipText("<html>" + blockInfo.replaceAll("\n", "<br>") + "</html>");
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                UnitBlock.this.setBackground(UIManager.getColor("control"));
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
