package com.young.game.objects.timer;

import com.young.game.objects.GameBoard;

import java.awt.*;

public class LabelTimer {
    private static LabelTimer instance;
    private ImageTimeIndicator imageTimeIndicator;

    private LabelTimer() {
        imageTimeIndicator = ImageTimeIndicator.getInstance();
    }

    public static LabelTimer getInstance() {
        if (instance == null)
            instance = new LabelTimer();
        return instance;
    }

    public static void reset() {
        ImageTimeIndicator.getInstance().reset();
        instance = null;
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
        GameBoard inBoard = GameBoard.getInstance();

        int dw = inBoard.getDw();
        int dh = inBoard.getDh();

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
        g.fillRect(2 * dw, 19 * dh, 18 * dw, dh);
//        g.setColor(Color.WHITE);
        g.setColor(new Color(0xFF, 0xFF, 0xFF, 200));
        g.fillRect(2 * dw, 19 * dh, imageTimeIndicator.getX() - 2 * dw, dh);
        g.setColor(Color.BLACK);
        imageTimeIndicator.draw(g);
    }
}
