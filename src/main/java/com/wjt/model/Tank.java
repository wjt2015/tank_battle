package com.wjt.model;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @Time 2020/3/29/16:47
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Tank extends JFrame {

    public void lauchFrame() {
        setLocation(400, 300);
        setSize(800, 600);
        setVisible(true);
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

    }
}
