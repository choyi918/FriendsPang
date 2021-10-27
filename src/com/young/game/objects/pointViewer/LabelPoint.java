package com.young.game.objects.pointViewer;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasRanking;

import java.awt.*;
import java.util.LinkedList;

public class LabelPoint {
    private LinkedList<ImageNum> imageNums;
    private int x;
    private int y;
    private int presentPoint;
    private int finalPoint;
    private final int SPEED;

    public LabelPoint() {
        this(0, 11 * CanvasGameOp.DW, 2 * CanvasGameOp.DH, 10);
    }

    public LabelPoint(int finalPoint, int x, int y, int speed) {
        imageNums = new LinkedList<>();
        presentPoint = -1;

        this.finalPoint = finalPoint;
        this.x = x;
        this.y = y;
        SPEED = speed;
    }

    public void setFinalPoint(int finalPoint) {
        this.finalPoint = finalPoint;
    }

    public void draw(Graphics g) {
        int dw = CanvasRanking.DW;
        int dh = CanvasRanking.DH;

        int defaultX = 0 + x - (imageNums.size() * dw) / 2 ;
        int defaultY = y;

        for (int i = 0; i < imageNums.size(); i++)
            imageNums.get(i).draw(defaultX + i * dw, defaultY, dw, dh, g);

    }

    public void update() {
        for (int j = 0; j < SPEED; j++) {
            if (presentPoint < finalPoint) {
                presentPoint++;
                LinkedList<ImageNum> newNums = convertPointToNumberImages(presentPoint);

                // 앞자리가 늘어나면 늘어난 만큼 기존 리스트에 추가.
                if (newNums.size() > imageNums.size()) {
                    int diff = newNums.size() - imageNums.size();
                    for (int i = 0; i < diff; i++)
                        imageNums.addFirst(newNums.get(diff - (i + 1)));
                }

                // 새로운 포인트의 각 자리 값을 각 자리에 setIndex로 갱신
                for (int i = 0; i < newNums.size(); i++) {
                    int newIndex = newNums.get(i).getIndex();
                    imageNums.get(i).setIndex(newIndex);
                }
            }

            for (ImageNum n : imageNums)
                n.update();
        }
    }

    private LinkedList<ImageNum> convertPointToNumberImages(int presentPoint) {
        LinkedList<ImageNum> returnNums = new LinkedList<>();

        int point = presentPoint;
        do {
            int n = point % 10;
            returnNums.addFirst(new ImageNum(n));
            point /= 10;
        } while (point != 0);

        return returnNums;
    }
}
