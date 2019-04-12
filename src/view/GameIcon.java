package view;

import javax.swing.*;
import java.awt.*;

public class GameIcon extends ImageIcon {

    public enum Size {
        CITIZEN, BUILDING, UNIT
    }

    public GameIcon(String filename) {
        super("assets/ico/" + filename);
    }

    public Icon resize(Size imageSize) {
        int resizedWidth = 50;
        int resizedHeight = 50;
        switch (imageSize) {
            case CITIZEN:
                resizedWidth = 50;
                resizedHeight = 50;
                break;
            case BUILDING:
                resizedWidth = 50;
                resizedHeight = 50;
                break;
            case UNIT:
                resizedWidth = 50;
                resizedHeight = 50;
                break;
        }
        return new ImageIcon(this.getImage().getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH));
    }
}
