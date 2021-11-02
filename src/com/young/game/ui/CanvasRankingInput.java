package com.young.game.ui;

import com.young.game.objects.Box.BoxRankingInputNameViewer;
import com.young.game.objects.GameBoard;
import com.young.game.objects.button.*;
import com.young.game.objects.pointViewer.LabelPoint;

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
    private LabelPoint labelPoint;
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
        labelPoint = new LabelPoint(GameBoard.getInstance().getPoint(), 11 * DW, 2 * DH, 200);

        int defaultX = 0 + 3 / 2 * DW;
        int defaultY = 0 + 8 * DH;
        int dw2 = DW / 4;
        int dh2 = DH / 4;

        for (char ch = 'A'; ch < 'Z' + 1; ch++)
            buttons[ch - 'A'] = new ButtonChar(ch,
                    String.format("button_%c_default.png", ch),
                    String.format("button_%c_pointed.png", ch),
                    defaultX + ((ch - 'A') % 7) * 3 * DW,
                    defaultX + ((ch - 'A') % 7) * 3 * DW + 2 * DW - dw2,
                    defaultY + ((ch - 'A') / 7) * 3 * DH,
                    defaultY + ((ch - 'A') / 7) * 3 * DH + 2 * DH - dh2);

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
        labelPoint.draw(buffG);

        buffG.setColor(new Color(0xFF, 0xFF, 0xFF, 150));
        buffG.fillRect(0 + 3 * DW - DW / 4, 0 + 5 * DH, 9 * (2 * DW - DW / 4),2 * DH - DH / 4);

        buffG.setColor(Color.BLACK);
        new BoxRankingInputNameViewer(buttonChars).draw(buffG);

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
            labelPoint.update();
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

        @Override
        public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            for (char ch = 'A'; ch < 'Z' + 1; ch++) {
                if (buttonChars.size() != 8 && buttons[ch - 'A'].clickedByMouse(mouseX, mouseY))
                    buttonChars.add(buttons[ch - 'A']);
            }

            if (buttonArrow.clickedByMouse(mouseX, mouseY) && buttonChars.size() > 0)
                buttonChars.removeLast();

            if (buttonOk.clickedByMouse(mouseX, mouseY)) {
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
            int mouseX = e.getX();
            int mouseY = e.getY();

            for (char ch = 'A'; ch < 'Z' + 1; ch++)
                buttons[ch - 'A'].pointedByMouse(mouseX, mouseY);

            buttonArrow.pointedByMouse(mouseX, mouseY);
            buttonOk.pointedByMouse(mouseX, mouseY);
        }
    };
}
