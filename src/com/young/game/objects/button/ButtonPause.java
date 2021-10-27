package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonPause extends Button{
    private Image imageButtonRestart;
    private Image imageButtonRestartPointed;

    public ButtonPause() {
        super("button_pause.png", "button_pause_pointed.png");
        imageButtonRestart = Toolkit.getDefaultToolkit().getImage("res/images/button_restart.png");
        imageButtonRestartPointed = Toolkit.getDefaultToolkit().getImage("res/images/button_restart_pointed.png");
    }

    @Override
    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();

        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        int x = 0 + 16 * dw;
        int y = 0 + 2 * dh;

        int w = 4 * dw;
        int h = 1 * dh;

        if (bPointed) {
            g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
            g.fillRect(x, y, w, h);
            g.setColor(Color.BLACK);

            if (observer.isPause())
                g.drawImage(imageButtonRestartPointed, x, y, w, h, observer);
            else
                g.drawImage(imagePointed, x, y, w, h, observer);
        }
        else {
            g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
            g.fillRect(x, y, w, h);
            g.setColor(Color.BLACK);

            if (observer.isPause())
                g.drawImage(imageButtonRestart, x, y, w, h, observer);
            else
                g.drawImage(imageDefault, x, y, w, h, observer);
        }
    }
}
