package com.young.game.objects.timer;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class LabelTimer {
    private ImageTimeIndicator imageTimeIndicator;

    public LabelTimer() {
        imageTimeIndicator = new ImageTimeIndicator();
    }

    public boolean isTimeout() {
        return imageTimeIndicator.isEnd();
    }

    public void stop() {
        imageTimeIndicator.stop();
    }

    public void start() {
        imageTimeIndicator.start();
    }

    public void update() {
        imageTimeIndicator.update();
    }

    public void draw(Graphics g) {
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
        g.fillRect(2 * dw, 19 * dh, 18 * dw, dh);

        int indicatorLocation = imageTimeIndicator.getX();
        if (indicatorLocation < 2 * dw + 8 * dw)
            g.setColor(new Color(0xFF, 0xFF, 0xFF, 200));
        else if (2 * dw + 8 * dw <= indicatorLocation && indicatorLocation <= 2 * dw + 13 * dw)
            g.setColor(new Color(0xFF, 0xFF, 0x00, 200));
        else
            g.setColor(new Color(0xFF, 0x00, 0x00, 200));

        g.fillRect(2 * dw, 19 * dh, imageTimeIndicator.getX() - 2 * dw, dh);

        g.setColor(Color.BLACK);
        imageTimeIndicator.draw(g);
    }
}
