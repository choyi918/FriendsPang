package com.young.game.ui;

import com.young.game.objects.Box.BoxRankingNameViewer;
import com.young.game.objects.button.ButtonBackMain;
import com.young.game.objects.pointViewer.LabelPoint;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class CanvasRanking extends Canvas implements Runnable{
    private Image imageRanking;
    private Image imageBackGround;
    private ButtonBackMain buttonBackMain;

    private RankingInfo[] rankingInfos;
    private BoxRankingNameViewer[] boxNameViewerTwos;
    private LabelPoint[] labelPointTwos;

    private static CanvasRanking instance;
    private static final int WIDTH;
    private static final int HEIGHT;
    public static final int DW;
    public static final int DH;

    static {
        WIDTH = 680;
        HEIGHT = 680;
        DW = WIDTH / 22;
        DH = HEIGHT / 22;
    }


    private CanvasRanking() {
        setSize(WIDTH, HEIGHT); // 22는 메뉴바 height

        buttonBackMain = new ButtonBackMain(0 + 9 * DW, 0 + 9 * DW + 4 * DW, 0 + 20 * DH, 0 + 20 * DH + DH, this);
        imageBackGround = Toolkit.getDefaultToolkit().getImage("res/images/autumn_story.png");
        imageRanking = Toolkit.getDefaultToolkit().getImage("res/images/title_ranking.png");

        boxNameViewerTwos = new BoxRankingNameViewer[8];
        labelPointTwos = new LabelPoint[8];
        try {
            loadRankingInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX= e.getX();
                int mouseY = e.getY();

                if (buttonBackMain.clickedByMouse(mouseX, mouseY)) {
                    setVisible(false);
                    CanvasMain.getInstance().setVisible(true);
                    instance = null;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                buttonBackMain.pointedByMouse(mouseX, mouseY);
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        new Thread(this).start();
    }

    public static CanvasRanking getInstance() {
        if (instance == null)
            instance = new CanvasRanking();

        return instance;
    }

    private void loadRankingInfo() throws IOException{

        int count = 0;
        {
            File file = new File("res/rank_info.txt");
            FileInputStream fis = new FileInputStream(file);
            Scanner scan = new Scanner(fis);

            while (scan.hasNextLine()) {
                count++;
                scan.nextLine();
            }

            rankingInfos = new RankingInfo[count];

            scan.close();
            fis.close();
        }

        {
            File file = new File("res/rank_info.txt");
            FileInputStream fis = new FileInputStream(file);
            Scanner scan = new Scanner(fis);

            int i = 0;
            while (scan.hasNextLine()) {
                String[] str = scan.nextLine().split(",");
                rankingInfos[i++] = new RankingInfo(str[0], Integer.parseInt(str[1]));
            }

            scan.close();
            fis.close();
        }

        quickSortRankingInfos(rankingInfos);

        for (int i = 0; i < boxNameViewerTwos.length; i++)
            if (i < rankingInfos.length)
                boxNameViewerTwos[i] = new BoxRankingNameViewer(rankingInfos[i].getName(), 4 * DW, 4 * DH + i * 2 * DH);

        for (int i = 0; i < labelPointTwos.length; i++)
            if (i < rankingInfos.length)
                labelPointTwos[i] = new LabelPoint(rankingInfos[i].getPoint(), 15 * DW, 4 * DH + i * 2 * DH, 200);

    }


    @Override
    public void paint(Graphics g) {
        Image buff = createImage(getWidth(), getHeight());
        Graphics buffG = buff.getGraphics();

        buffG.drawImage(imageBackGround, 0, 0, getWidth(), getHeight(), this);

        buffG.setColor(new Color(0xFF, 0xFF, 0xFF, 150));
        buffG.fillRect(0 + 8 * DW, 0 + 2 * DH, 6 * DW, DH);

        buffG.setColor(Color.BLACK);
        buffG.drawImage(imageRanking, 0 + 8 * DW, 0 + 2 * DH, 6 * DW, DH, this);
        buttonBackMain.draw(buffG);

        for (int i = 0; i < 8; i++) {
            buffG.setColor(new Color(0xFF, 0xFF, 0xFF, 230));
            buffG.fillRect(4 * DW, 4 * DH + i * 2 * DH, 14 * DW, DH);
            buffG.setColor(Color.BLACK);

            BoxRankingNameViewer b = boxNameViewerTwos[i];
            if (b != null)
                b.draw(buffG);

            LabelPoint l = labelPointTwos[i];
            if (l != null)
                l.draw(buffG);
        }

        g.drawImage(buff, 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void run() {
        while (true) {
            for (LabelPoint l : labelPointTwos)
                if (l != null)
                    l.update();

            repaint();
            validate();

            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class RankingInfo{
        private String name;
        private int point;

        public RankingInfo(String name, int point) {
            this.name = name;
            this.point = point;
        }

        public String getName() {
            return name;
        }

        public int getPoint() {
            return point;
        }
    }

    private void quickSortRankingInfos(RankingInfo[] rankingInfos) {
        quickSortRecursive(rankingInfos, 0, rankingInfos.length - 1);
    }

    private void quickSortRecursive(RankingInfo[] rankingInfos, final int left, final int right) {
        if (left > right)
            return;

        int pivotIndex = partition(rankingInfos, left, right);
        quickSortRecursive(rankingInfos, left, pivotIndex - 1);
        quickSortRecursive(rankingInfos, pivotIndex + 1, right);
    }

    private int partition(RankingInfo[] rankingInfos, final int left, final int right) {
        int pivotIndex = right;
        int leftPoint = left;

        for (int i = left; i < right; i++) {
            if (rankingInfos[i].getPoint() > rankingInfos[pivotIndex].getPoint())
                swap(rankingInfos, i, leftPoint++);
        }

        swap(rankingInfos, pivotIndex, leftPoint);
        pivotIndex = leftPoint;

        return pivotIndex;
    }

    private void swap(RankingInfo[] rankingInfos, final int i, final int j) {
        RankingInfo tmp = rankingInfos[i];
        rankingInfos[i] = rankingInfos[j];
        rankingInfos[j] = tmp;
    }
}
