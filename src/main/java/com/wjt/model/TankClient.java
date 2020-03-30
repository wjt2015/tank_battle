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
public class TankClient extends JFrame implements Runnable {

    /*    public volatile int x = Constants.INIT_X;
        public volatile int y = Constants.INIT_Y;
        public volatile int xv = 0;
        public volatile int yv = 0;*/
    public int width = 1200;
    public int height = 1000;
    public int interval = 30;
    int d = 30;

    public Rectangle rect = new Rectangle(0 + d, 0 + d, width - 2 * d, height - 2 * d);

    /**
     * 双缓冲用的虚拟图片;
     */
    public BufferedImage offScreen = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

    /**
     * 坦克;
     */
    public Tank tank;

    private void paintOffScreen() {
        Graphics2D g2d = (Graphics2D) (offScreen.getGraphics());
        g2d.setBackground(Color.GREEN);
        g2d.clearRect(0, 0, width, height);

        tank.draw(g2d);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //在内存绘制;
        paintOffScreen();
        //复制;
        g2d.drawImage(offScreen, 0, 0, null);
    }

    public TankClient() {
        setLocation(0, 0);
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
                tank.keyPressed(e);
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

        tank = new Tank(rect);

        setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            //log.info("tank position:({},{});", x, y);
            tank.move();
            repaint();
            Utils.sleep(interval);
        }
    }
}
