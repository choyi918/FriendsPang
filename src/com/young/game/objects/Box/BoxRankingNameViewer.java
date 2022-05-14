package com.young.game.objects.Box;

import com.young.game.objects.button.*;
import com.young.game.ui.CanvasRanking;

import java.awt.*;
import java.util.LinkedList;

public class BoxRankingNameViewer {
    private String name;
    private LinkedList<ButtonChar> buttonCharsOfName;
    private int x;
    private int y;

    public BoxRankingNameViewer(String name, int x, int y) {
        buttonCharsOfName = new LinkedList<>();
        this.name = name;
        this.x = x;
        this.y = y;

        convertStringToButtonChars();
    }

    private void convertStringToButtonChars() {
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            buttonCharsOfName.addLast(new ButtonChar(ch,
                    String.format("button_%c_default.png", ch),
                    String.format("button_%c_pointed.png", ch),
                    0, 0, 0, 0, null)); // null을 넣어도 괜찮나? 이 클래스(네임뷰어)는 일단 draw에 다시 인스턴스를 불러와서 그리고있음
        }
    }

    public void draw(Graphics g) {
        CanvasRanking observer = CanvasRanking.getInstance();

        int dw = CanvasRanking.DW;
        int dh = CanvasRanking.DH;

        for (int i = 0; i < buttonCharsOfName.size(); i++) {
            Image image = buttonCharsOfName.get(i).getImage();
            g.drawRect(x + i * dw, y, dw, dh);
            g.drawImage(image,x + i * dw, y, dw, dh, observer);
        }

    }
}
