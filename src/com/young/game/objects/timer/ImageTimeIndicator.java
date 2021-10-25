package com.young.game.objects.timer;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ImageTimeIndicator {
    private static Image image;
    private static ImageTimeIndicator instance;
    private static final int DURATION;
    private int durationIndex;
    private int x;
    private int y;
    private boolean bEnd;
    private boolean bStop;

    static {
        image = Toolkit.getDefaultToolkit().getImage("res/images/mad_kon.png");
        DURATION = 30;
    }

    private ImageTimeIndicator() {
        x = 0 + 2 * CanvasGameOp.getDw();
        y = 0 + 19 * CanvasGameOp.getDh();
    }

    public static ImageTimeIndicator getInstance() {
        if (instance == null)
            instance = new ImageTimeIndicator();
        return instance;
    }

    public void reset() {
        instance = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnd() {
        return bEnd;
    }

    public void stop() {
        bStop = true;
    }

    public void start() {
        bStop = false;
    }

    public void update() {
        if (bStop)
            return;

        if (x != 0 + 19 * CanvasGameOp.getDw()) {
            if (durationIndex == DURATION) {
                x += 1;
                durationIndex %= DURATION;
            }
            durationIndex++;
        } else {
            bEnd = true;
        }
    }

    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();
        g.drawImage(image, x, y, CanvasGameOp.getDw(), CanvasGameOp.getDh(), observer);
    }

}
