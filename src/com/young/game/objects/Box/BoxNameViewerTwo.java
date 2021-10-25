package com.young.game.objects.Box;

import com.young.game.objects.button.*;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;
import java.util.LinkedList;

public class BoxNameViewerTwo {
    private String name;
    private LinkedList<ButtonChar> buttonCharsOfName;
    private int x;
    private int y;

    public BoxNameViewerTwo(String name, int x, int y) {
        buttonCharsOfName = new LinkedList<>();
        this.name = name;
        this.x = x;
        this.y = y;

        convertStringToButtonChars();
    }

    private void convertStringToButtonChars() {
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            switch (ch) {
                case 'A':
                    buttonCharsOfName.addLast(new ButtonA());
                    break;
                case 'B':
                    buttonCharsOfName.addLast(new ButtonB());
                    break;
                case 'C':
                    buttonCharsOfName.addLast(new ButtonC());
                    break;
                case 'D':
                    buttonCharsOfName.addLast(new ButtonD());
                    break;
                case 'E':
                    buttonCharsOfName.addLast(new ButtonE());
                    break;
                case 'F':
                    buttonCharsOfName.addLast(new ButtonF());
                    break;
                case 'G':
                    buttonCharsOfName.addLast(new ButtonG());
                    break;
                case 'H':
                    buttonCharsOfName.addLast(new ButtonH());
                    break;
                case 'I':
                    buttonCharsOfName.addLast(new ButtonI());
                    break;
                case 'J':
                    buttonCharsOfName.addLast(new ButtonJ());
                    break;
                case 'K':
                    buttonCharsOfName.addLast(new ButtonK());
                    break;
                case 'L':
                    buttonCharsOfName.addLast(new ButtonL());
                    break;
                case 'M':
                    buttonCharsOfName.addLast(new ButtonM());
                    break;
                case 'N':
                    buttonCharsOfName.addLast(new ButtonN());
                    break;
                case 'O':
                    buttonCharsOfName.addLast(new ButtonO());
                    break;
                case 'P':
                    buttonCharsOfName.addLast(new ButtonP());
                    break;
                case 'Q':
                    buttonCharsOfName.addLast(new ButtonQ());
                    break;
                case 'R':
                    buttonCharsOfName.addLast(new ButtonR());
                    break;
                case 'S':
                    buttonCharsOfName.addLast(new ButtonS());
                    break;
                case 'T':
                    buttonCharsOfName.addLast(new ButtonT());
                    break;
                case 'U':
                    buttonCharsOfName.addLast(new ButtonU());
                    break;
                case 'V':
                    buttonCharsOfName.addLast(new ButtonV());
                    break;
                case 'W':
                    buttonCharsOfName.addLast(new ButtonW());
                    break;
                case 'X':
                    buttonCharsOfName.addLast(new ButtonX());
                    break;
                case 'Y':
                    buttonCharsOfName.addLast(new ButtonY());
                    break;
                case 'Z':
                    buttonCharsOfName.addLast(new ButtonZ());
                    break;
                default:
                    assert (false) : "No Alpahbet";
                    break;
            }
        }
    }

    public void draw(Graphics g) {
        CanvasRankingInput observer = CanvasRankingInput.getInstance();

        int dw = observer.getWidth() / 22;
        int dh = observer.getHeight() / 22;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 230));
        g.fillRect(x, y, 8 * dw, dh);
        g.setColor(Color.BLACK);

        for (int i = 0; i < buttonCharsOfName.size(); i++) {
            Image image = buttonCharsOfName.get(i).getImage();
            g.drawRect(x + i * dw, y, dw, dh);
            g.drawImage(image,x + i * dw, y, dw, dh, observer);
        }

    }
}
