package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

    public static Icon resizeTo(BufferedImage image, double factor) {
        return new ImageIcon(image.getScaledInstance((int) (image.getWidth() * factor),
                (int) (image.getHeight() * factor), Image.SCALE_SMOOTH));
    }
}
