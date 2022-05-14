package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public abstract class Button {
    protected Image imageDefault;
    protected Image imagePointed;
    protected boolean bPointed;
    protected int leftBoundaryX;
    protected int rightBoundaryX;
    protected int upperBoundaryY;
    protected int lowerBoundaryY;

    protected Canvas observer;


    public Button(String imageDefaultFileName, String imagePointedFileName,
                  int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer){
        imageDefault = Toolkit.getDefaultToolkit().getImage(String.format("res/images/%s", imageDefaultFileName));
        imagePointed = Toolkit.getDefaultToolkit().getImage(String.format("res/images/%s", imagePointedFileName));
        this.leftBoundaryX = leftBoundaryX;
        this.rightBoundaryX = rightBoundaryX;
        this.upperBoundaryY = upperBoundaryY;
        this.lowerBoundaryY = lowerBoundaryY;
        this.observer = observer;
    }

    public Image getImage() {
        return imagePointed;
    }

    public boolean clickedByMouse(int mouseX, int mouseY) {
        if (leftBoundaryX <= mouseX && mouseX <= rightBoundaryX
                && upperBoundaryY <= mouseY && mouseY <= lowerBoundaryY)
            return true;
        else
            return false;
    }

    public void pointedByMouse(int mouseX, int mouseY) {
        if (leftBoundaryX <= mouseX && mouseX <= rightBoundaryX
                && upperBoundaryY <= mouseY && mouseY <= lowerBoundaryY)
            bPointed = true;
        else
            bPointed = false;
    }

    public void draw(Graphics g, Canvas observer) {
        int x = leftBoundaryX;
        int y = upperBoundaryY;

        int w = rightBoundaryX - leftBoundaryX;
        int h = lowerBoundaryY - upperBoundaryY;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);

        if (bPointed)
            g.drawImage(imagePointed, x, y, w, h, observer);
        else
            g.drawImage(imageDefault, x, y, w, h, observer);
    }

}
