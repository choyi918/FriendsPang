package com.young.game.objects.pointViewer;

import com.young.game.ui.CanvasRanking;

import java.awt.*;
import java.util.LinkedList;

public class LabelPointTwo {
    private LinkedList<ImageNum> imageNums;
    private int x;
    private int y;
    private int presentPoint;
    private int finalPoint;

    public LabelPointTwo(int point, int x, int y) {
        this.x = x;
        this.y = y;
        this.finalPoint = point;
        imageNums = new LinkedList<>();
        presentPoint = -1;
    }

    public void draw(Graphics g) {
        int dw = CanvasRanking.DW;
        int dh = CanvasRanking.DH;

        int defaultX = 0 + x - (imageNums.size() * dw) / 2 ;
        int defaultY = y;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 230));
        g.fillRect(defaultX, y, imageNums.size() * dw, dh);
        g.setColor(Color.BLACK);

        for (int i = 0; i < imageNums.size(); i++) {
            imageNums.get(i).draw(defaultX + i * dw, defaultY, dw, dh, g);
        }

    }

    public void update() {
        for (int j = 0; j < 200; j++) {
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
