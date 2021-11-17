package com.young.game.objects.timer;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ImageTimeIndicator {
    private int x;
    private int y;
    private int durationIndex;
    private boolean bEnd;
    private boolean bStop;

    private static Image image;
    private static final int DURATION;

    static {
        image = Toolkit.getDefaultToolkit().getImage("res/images/mad_kon.png");
        DURATION = 30;
    }

    public ImageTimeIndicator() {
        x = 0 + 2 * CanvasGameOp.DW;
        y = 0 + 19 * CanvasGameOp.DH;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /* Time Over 때 사용 */
    public boolean isEnd() {
        return bEnd;
    }

    /* start(), stop()은 pause 때 사용*/
    public void stop() {
        bStop = true;
    }

    public void start() {
        bStop = false;
    }

    public void update() {
        if (bStop)
            return;

        if (x != 0 + 19 * CanvasGameOp.DW) {
            if (durationIndex == DURATION) {
                x += 1;
                durationIndex %= DURATION;
            }
            durationIndex++;
        }
        else
            bEnd = true;
    }

    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();
        g.drawImage(image, x, y, CanvasGameOp.DW, CanvasGameOp.DH, observer);
    }

}
