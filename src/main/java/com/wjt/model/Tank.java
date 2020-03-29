package com.wjt.model;

import com.wjt.common.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

/**
 * @Time 2020/3/29/16:47
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Tank extends JFrame implements Runnable {

    public volatile int x = INIT_X;
    public volatile int y = INIT_Y;
    public volatile int xv = 0;
    public volatile int yv = 0;
    public int width = 800;
    public int height = 600;
    public int interval = 30;

    /**
     * 最初的坦克位置(INIT_X,INIT_Y);
     */
    public static final int INIT_X = 0;
    public static final int INIT_Y = 0;

    public static final int XV = 10;
    public static final int YV = 10;
    /**
     * 双缓冲用的虚拟图片;
     */
    public BufferedImage offScreen = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);


    private void paintOffScreen() {
        Graphics2D g2d = (Graphics2D) (offScreen.getGraphics());
        g2d.setBackground(Color.GREEN);
        g2d.clearRect(0, 0, width, height);
        Color oldColor = g2d.getColor();
        g2d.setColor(Color.RED);
        g2d.fillOval(x, y, 30, 30);
        g2d.setColor(oldColor);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //在内存绘制;
        paintOffScreen();
        //复制;
        g2d.drawImage(offScreen, 0, 0, null);
    }

    public Tank() {
        setLocation(INIT_X, INIT_Y);
        setSize(width, height);
        setTitle("tankWar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setResizable(false);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                log.info("window has been opened!e={};", e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                log.info("window has been closing!e={};", e);
                System.exit(0);

            }

            @Override
            public void windowClosed(WindowEvent e) {
                log.info("window has been closed!e={};", e);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_W) {
                    yv = -YV;
                } else if (keyCode == KeyEvent.VK_S) {
                    yv = YV;
                } else if (keyCode == KeyEvent.VK_A) {
                    xv = -XV;
                } else if (keyCode == KeyEvent.VK_D) {
                    xv = XV;
                }
                log.info("keyPressed;e={};keyCode={};xv={};yv={};", e, keyCode, xv, yv);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.info("mouseXY=({},{});mouseXYOnScreen=({},{});clickCount={};",
                        e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        setVisible(true);

    }

    @Override
    public void run() {
        while (true) {
            //log.info("tank position:({},{});", x, y);
            if (x >= 0 && x <= width && y >= 0 && y <= height) {
                x += xv;
                y += yv;

                //防止越界;
                x = (x < 0 ? 0 : x);
                x = (x > width ? width : x);
                y = (y < 0 ? 0 : y);
                y = (y > height ? height : y);

                repaint();
                Utils.sleep(interval);
            }
        }
    }
}
