package view;

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

    private Rescuable rescuable;
    private ArrayList<Unit> units = new ArrayList<>();

    public WorldBlock(JTextArea blockInfo) {
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                WorldBlock.this.setBackground(Color.GREEN);
                if (rescuable != null) {
                    if (units.size() == 0) {
                        blockInfo.setText(rescuable.toString());
                    } else {
                        blockInfo.setText((rescuable instanceof ResidentialBuilding ? "Building:\n" : "Citizen:\n") + rescuable.toString());
                    }
                }
                if (units.size() == 1) {
                    blockInfo.setText((blockInfo.getText() + "\n\nUnit:\n" + units.get(0).toString()).trim());
                } else {
                    for (int i = 1; i <= units.size(); i++) {
                        blockInfo.setText((blockInfo.getText() + "\n\nUnit " + i + ":\n" + units.get(i - 1).toString()).trim());
                    }
                }
            }

            public void mouseExited(MouseEvent e) {
                WorldBlock.this.setBackground(UIManager.getColor("control"));
                blockInfo.setText(null);
            }
        });
    }

    public void addSimulatable(Simulatable simulatable) {
        if (simulatable instanceof ResidentialBuilding) {
            rescuable = (ResidentialBuilding) simulatable;
        } else if (simulatable instanceof Citizen) {
            if (rescuable == null) {
                rescuable = (Citizen) simulatable;
            }
        } else if (simulatable instanceof Unit) {
            units.add((Unit) simulatable);
        }
        requestLayout();
    }

    public void removeSimulatable(Simulatable simulatable) {
        if (simulatable instanceof ResidentialBuilding) {
            if (simulatable == rescuable) rescuable = null;
        } else if (simulatable instanceof Citizen) {
            if (simulatable == rescuable) rescuable = null;
        } else if (simulatable instanceof Unit) {
            units.remove(simulatable);
        }
        requestLayout();
    }

    public Rescuable getRescuable() {
        return rescuable;
    }

    private void requestLayout() {
        BufferedImage bufferedImage = new BufferedImage(70, 70, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        if (rescuable instanceof ResidentialBuilding) {
            graphics.drawImage(new ImageIcon("assets/ico/building.png").getImage(), 0, 0, 70, 70, this);
        } else if (rescuable instanceof Citizen) {
            graphics.drawImage(new ImageIcon("assets/ico/citizen.png").getImage(), 0, 0, 70, 70, this);
        }
        if (rescuable == null) {
            if (units.size() == 5) {
                ImageIcon subIcon1 = getUnitIcon(units.get(0));
                if (subIcon1 != null) {
                    graphics.drawImage(subIcon1.getImage(), 0, 0, 35, 35, this);
                }
                ImageIcon subIcon2 = getUnitIcon(units.get(1));
                if (subIcon2 != null) {
                    graphics.drawImage(subIcon2.getImage(), 35, 0, 35, 35, this);
                }
                ImageIcon subIcon3 = getUnitIcon(units.get(2));
                if (subIcon3 != null) {
                    graphics.drawImage(subIcon3.getImage(), 0, 35, 35, 35, this);
                }
                ImageIcon subIcon4 = getUnitIcon(units.get(3));
                if (subIcon4 != null) {
                    graphics.drawImage(subIcon4.getImage(), 35, 35, 35, 35, this);
                }
                ImageIcon subIcon5 = getUnitIcon(units.get(4));
                if (subIcon5 != null) {
                    graphics.drawImage(subIcon5.getImage(), 17, 17, 35, 35, this);
                }
            } else if (units.size() == 4) {
                ImageIcon subIcon1 = getUnitIcon(units.get(0));
                if (subIcon1 != null) {
                    graphics.drawImage(subIcon1.getImage(), 0, 0, 35, 35, this);
                }
                ImageIcon subIcon2 = getUnitIcon(units.get(1));
                if (subIcon2 != null) {
                    graphics.drawImage(subIcon2.getImage(), 35, 0, 35, 35, this);
                }
                ImageIcon subIcon3 = getUnitIcon(units.get(2));
                if (subIcon3 != null) {
                    graphics.drawImage(subIcon3.getImage(), 0, 35, 35, 35, this);
                }
                ImageIcon subIcon4 = getUnitIcon(units.get(3));
                if (subIcon4 != null) {
                    graphics.drawImage(subIcon4.getImage(), 35, 35, 35, 35, this);
                }
            } else if (units.size() == 3) {
                ImageIcon subIcon1 = getUnitIcon(units.get(0));
                if (subIcon1 != null) {
                    graphics.drawImage(subIcon1.getImage(), 0, 0, 35, 35, this);
                }
                ImageIcon subIcon2 = getUnitIcon(units.get(1));
                if (subIcon2 != null) {
                    graphics.drawImage(subIcon2.getImage(), 35, 0, 35, 35, this);
                }
                ImageIcon subIcon3 = getUnitIcon(units.get(2));
                if (subIcon3 != null) {
                    graphics.drawImage(subIcon3.getImage(), 17, 35, 35, 35, this);
                }
            } else if (units.size() == 2) {
                ImageIcon subIcon1 = getUnitIcon(units.get(0));
                if (subIcon1 != null) {
                    graphics.drawImage(subIcon1.getImage(), 17, 0, 35, 35, this);
                }
                ImageIcon subIcon2 = getUnitIcon(units.get(1));
                if (subIcon2 != null) {
                    graphics.drawImage(subIcon2.getImage(), 17, 35, 35, 35, this);
                }
            } else if (units.size() == 1) {
                ImageIcon subIcon1 = getUnitIcon(units.get(0));
                if (subIcon1 != null) {
                    graphics.drawImage(subIcon1.getImage(), 0, 0, 70, 70, this);
                }
            }
        } else {
            for (Unit unit : units) {
                ImageIcon subIcon = getUnitIcon(unit);
                if (subIcon != null) {
                    graphics.drawImage(subIcon.getImage(), 30, 30, 40, 40, this);
                }
            }
        }
        setIcon(new ImageIcon(bufferedImage));
    }

    private ImageIcon getUnitIcon(Unit unit) {
        if (unit instanceof Ambulance) {
            return new ImageIcon("assets/ico/ambulance.png");
        } else if (unit instanceof DiseaseControlUnit) {
            return new ImageIcon("assets/ico/disease_control.png");
        } else if (unit instanceof FireTruck) {
            return new ImageIcon("assets/ico/fire_truck.png");
        } else if (unit instanceof GasControlUnit) {
            return new ImageIcon("assets/ico/gas_control.png");
        } else if (unit instanceof Evacuator) {
            return new ImageIcon("assets/ico/evacuator.png");
        }
        return null;
    }
}
