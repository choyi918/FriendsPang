package com.young.game.objects.button;

import java.awt.*;

public abstract class Button {
    protected Image imageDefault;
    protected Image imagePointed;
    protected boolean bPointed;

    public Button(String imageDefaultFileName, String imagePointedFileName) {
        imageDefault = Toolkit.getDefaultToolkit().getImage(String.format("res/images/%s", imageDefaultFileName));
        imagePointed = Toolkit.getDefaultToolkit().getImage(String.format("res/images/%s", imagePointedFileName));
    }

    public Image getImage() {
        return imagePointed;
    }

    public void pointButton() {
        bPointed = true;
    }

    public void outpointButton() {
        bPointed = false;
    }

    abstract void draw(Graphics g);
}
