package view;

import javax.swing.*;
import java.awt.*;

public class GameIcon extends ImageIcon {

    public enum Size {
        WORLD_BLOCK, UNIT_BLOCK
    }

    public GameIcon(String filename) {
        super("assets/ico/" + filename);
    }

    public Icon resize(Size imageSize) {
        int resizedWidth;
        int resizedHeight;
        switch (imageSize) {
            case WORLD_BLOCK:
                resizedWidth = 70;
                resizedHeight = 70;
                break;
            case UNIT_BLOCK:
                resizedWidth = 50;
                resizedHeight = 50;
                break;
            default:
                resizedWidth = 50;
                resizedHeight = 50;
                break;
        }
        return new ImageIcon(this.getImage().getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH));
    }
}
