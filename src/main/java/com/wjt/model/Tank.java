package com.wjt.model;

import com.wjt.common.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @Time 2020/3/29/16:47
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Tank extends JFrame implements Runnable {


    public volatile int x = 20;
    public volatile int y = 20;
    public volatile int xv = 50;
    public volatile int yv = 50;
    public int width = 800;
    public int height = 600;

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.GREEN);
        g2d.clearRect(0, 0, 800, 600);
        Color oldColor = g2d.getColor();
        g2d.setColor(Color.RED);
        g2d.fillOval(x, y, 30, 30);
        g2d.setColor(oldColor);
    }

    public Tank() {
        setLocation(400, 300);
        setSize(800, 600);
        //getContentPane().setVisible(false);
        //getContentPane().setBackground(Color.GREEN);
        setTitle("tankWar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
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
        setVisible(true);

    }

    @Override
    public void run() {
        while (x <= width) {

            repaint();
            Utils.sleep(200);

            if (yv > 0) {
                if (y >= height) {
                    yv = -yv;
                    x += xv;
                }
            } else {
                if (y <= 0) {
                    yv = -yv;
                    x += xv;
                }
            }
            y += yv;
        }
    }
}
