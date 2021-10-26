package com.young.game.ui;

import com.young.game.objects.Box.BoxNameViewer;
import com.young.game.objects.GameBoard;
import com.young.game.objects.button.*;
import com.young.game.objects.pointViewer.LabelPointTwo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

public class CanvasRankingInput extends Canvas implements Runnable {
    private ButtonChar[] buttons;
    private ButtonArrow buttonArrow;
    private ButtonOk buttonOk;
    private LinkedList<ButtonChar> buttonChars;
    private LabelPointTwo labelPointRenew;
    private Image imageBackGround;
    private int point;
    private MouseAdapterForCanvasRankingInput mouseAdapterForCanvasRankingInput;

    private static CanvasRankingInput instance;
    private static final int ALPHABET_SIZE;
    private static final int WIDTH;
    private static final int HEIGHT;
    public static final int DW;
    public static final int DH;

    static {
        ALPHABET_SIZE = 26;
        WIDTH = 680;
        HEIGHT = 680;
        DW = WIDTH / 22;
        DH = HEIGHT / 22;
    }

    private CanvasRankingInput() {
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setBackground(new Color(0xFF, 0xFF,0xFF, 0));

        buttonChars = new LinkedList<>();
        buttons = new ButtonChar[ALPHABET_SIZE];
        labelPointRenew = new LabelPointTwo(GameBoard.getInstance().getPoint(), 11 * DW, 2 * DH);

        int i = 0;
        buttons[i++] = new ButtonA();
        buttons[i++] = new ButtonB();
        buttons[i++] = new ButtonC();
        buttons[i++] = new ButtonD();
        buttons[i++] = new ButtonE();
        buttons[i++] = new ButtonF();
        buttons[i++] = new ButtonG();
        buttons[i++] = new ButtonH();
        buttons[i++] = new ButtonI();
        buttons[i++] = new ButtonJ();
        buttons[i++] = new ButtonK();
        buttons[i++] = new ButtonL();
        buttons[i++] = new ButtonM();
        buttons[i++] = new ButtonN();
        buttons[i++] = new ButtonO();
        buttons[i++] = new ButtonP();
        buttons[i++] = new ButtonQ();
        buttons[i++] = new ButtonR();
        buttons[i++] = new ButtonS();
        buttons[i++] = new ButtonT();
        buttons[i++] = new ButtonU();
        buttons[i++] = new ButtonV();
        buttons[i++] = new ButtonW();
        buttons[i++] = new ButtonX();
        buttons[i++] = new ButtonY();
        buttons[i] = new ButtonZ();
        buttonArrow = new ButtonArrow();
        buttonOk = new ButtonOk();

        imageBackGround = Toolkit.getDefaultToolkit().getImage("res/images/autumn_story.png");
        mouseAdapterForCanvasRankingInput = new MouseAdapterForCanvasRankingInput();
        addMouseListener(mouseAdapterForCanvasRankingInput);
        addMouseMotionListener(mouseAdapterForCanvasRankingInput);

        new Thread(this).start();
    }

    public static CanvasRankingInput getInstance() {
        if (instance == null)
            instance = new CanvasRankingInput();

        return instance;
    }

    public void register() throws IOException {
        File file = new File("res/rank_info.txt");
        FileOutputStream fos = new FileOutputStream(file, true);
        PrintStream ps = new PrintStream(fos);

        point = GameBoard.getInstance().getPoint();

        StringBuilder name = new StringBuilder();
        for (ButtonChar b : buttonChars)
            name.append(b.getCh());

        ps.printf("%s,%d\n", name.toString(), point);

        ps.close();
        fos.close();
    }

    @Override
    public void paint(Graphics g) {
        Image buff = createImage(getWidth(), getHeight());
        Graphics buffG = buff.getGraphics();

        buffG.drawImage(imageBackGround, 0, 0, getWidth(), getHeight(), this);
        labelPointRenew.draw(buffG);

        new BoxNameViewer(buttonChars).draw(buffG);

        for (ButtonChar b : buttons)
            b.draw(buffG);

        buttonArrow.draw(buffG);
        buttonOk.draw(buffG);

        g.drawImage(buff, 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void run() {
        while (true) {
            labelPointRenew.update();
            repaint();
            validate();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MouseAdapterForCanvasRankingInput extends MouseAdapter {
        int defaultX = 0 + 3 / 2 * DW;
        int defaultY = 0 + 8 * DH;
        int dw2 = DW / 4;
        int dh2 = DH / 4;

        @Override
        public void mouseClicked(MouseEvent e) {
            int eX = e.getX();
            int eY = e.getY();

            for (char ch = 'A'; ch < 'Z'; ch++)
                if (getCharX(ch) <= eX && eX <= getCharX(ch) + 2 * DW - dw2
                        && getCharY(ch) <= eY && eY <= getCharY(ch) + 2 * DH - dh2) {
                    if (buttonChars.size() != 8)
                        buttonChars.add(buttons[ch - 'A']);
                }

            if (getCharX('Z') + 3 * DW <= eX && eX <= getCharX('Z') + 3 * DW + 2 * DW - dw2
                    && getCharY('Z') <= eY && eY <= getCharY('Z') + 2 * DH - dh2)
                buttonChars.removeLast();

            if (getCharX('Z') + 3 * DW * 2 <= eX && eX <= getCharX('Z') + 3 * DW * 2+ 2 * DW - dw2
                    && getCharY('Z') <= eY && eY <= getCharY('Z') + 2 * DH - dh2){
                try {
                    CanvasRankingInput.this.register();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                CanvasGameOp.reset();
                setVisible(false);
                instance = null;
                CanvasMain.getInstance().setVisible(true);
            }

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int eX = e.getX();
            int eY = e.getY();

            for (char ch = 'A'; ch < 'Z' + 1; ch++)
                buttons[ch - 'A'].outpointButton();

            buttonArrow.outpointButton();
            buttonOk.outpointButton();

            for (char ch = 'A'; ch < 'Z' + 1; ch++)
                if (getCharX(ch) <= eX && eX <= getCharX(ch) + 2 * DW - dw2
                        && getCharY(ch) <= eY && eY <= getCharY(ch) + 2 * DH - dh2)
                    buttons[ch - 'A'].pointButton();


            if (getCharX('Z') + 3 * DW <= eX && eX <= getCharX('Z') + 3 * DW + 2 * DW - dw2
                    && getCharY('Z') <= eY && eY <= getCharY('Z') + 2 * DH - dh2){
                buttonArrow.pointButton();
            }

            if (getCharX('Z') + 3 * DW * 2 <= eX && eX <= getCharX('Z') + 3 * DW * 2+ 2 * DW - dw2
                    && getCharY('Z') <= eY && eY <= getCharY('Z') + 2 * DH - dh2){
                buttonOk.pointButton();
            }
        }

        private int getCharX(char ch) {
            return defaultX + ((ch - 'A') % 7) * 3 * DW;
        }

        private int getCharY(char ch) {
            return defaultY + ((ch - 'A') / 7) * 3 * DH;
        }
    };
}
