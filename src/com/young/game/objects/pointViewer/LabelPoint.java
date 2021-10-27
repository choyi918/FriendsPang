package com.young.game.objects.pointViewer;

import com.young.game.objects.GameBoard;
import com.young.game.ui.CanvasGameOp;

import java.awt.*;
import java.util.LinkedList;

public class LabelPoint {
    private LinkedList<ImageNum> imageNums;
    private int presentPoint;

    public LabelPoint() {
        imageNums = new LinkedList<>();
        presentPoint = -1; // update() 메소드가 getPoint == 0부터 동작하기 위해서
    }

    public void draw(Graphics g) {
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        int defaultX = 0 + 11 * dw - (imageNums.size() * dw) / 2 ;
        int defaultY = 2 * dh;

        for (int i = 0; i < imageNums.size(); i++)
            imageNums.get(i).draw(defaultX + i * dw, defaultY, dw, dh, g);

    }

    public void update() {

        if (presentPoint != GameBoard.getInstance().getPoint()) {
            LinkedList<ImageNum> newNums = convertPointToNumberImages();

            // 앞자리가 늘어나면 늘어난 만큼 기존 리스트에 추가.
            if (newNums.size() > imageNums.size()) {
                int diff = newNums.size() - imageNums.size();
                for (int i = 0; i < diff; i++)
                    imageNums.addFirst(newNums.get(diff - (i + 1)));
//                    imageNums.addFirst(newNums.get(i)); // 첫 0점 -> 세자리이상 점수 발생시 버그
            }

            // 새로운 포인트의 각 자리 값을 각 자리에 setIndex로 갱신
            for (int i = 0; i < newNums.size(); i++) {
                int newIndex = newNums.get(i).getIndex();
                imageNums.get(i).setIndex(newIndex);
            }

            // 현재 기억하고 있는 점수를 새로운 점수로 갱신
            presentPoint = GameBoard.getInstance().getPoint();
        }


        for (ImageNum n : imageNums)
            n.update();
    }

    private LinkedList<ImageNum> convertPointToNumberImages() {
        int point = GameBoard.getInstance().getPoint();
        LinkedList<ImageNum> returnNums = new LinkedList<>();

        do {
            int n = point % 10;
            returnNums.addFirst(new ImageNum(n));
            point /= 10;
        } while (point != 0);

        return returnNums;
    }
}
