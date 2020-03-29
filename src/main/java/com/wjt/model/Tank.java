package com.wjt.model;

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
public class Tank extends JFrame {

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        g2d.setColor(Color.RED);
        g2d.fillOval(50, 50, 30, 30);
        g2d.setColor(oldColor);
    }

    public void lauchFrame() {
        setLocation(400, 300);
        setSize(800, 600);
        setTitle("tankWar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(Color.GREEN);
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
}
