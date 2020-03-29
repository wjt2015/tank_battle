package com.wjt.model;

import com.wjt.common.Constants;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @Time 2020/3/29/22:46
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Tank {
    public volatile int x, y;
    public volatile int xv, yv;
    public volatile int length = 30, width = 30;
    public volatile Color color;


    public Tank() {
        x = Constants.INIT_X;
        y = Constants.INIT_Y;
        xv = Constants.XV;
        yv = Constants.YV;
        length = 30;
        width = 30;
        color = Color.RED;
    }

    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval(x, y, length, width);
        g2d.setColor(oldColor);
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            yv = -Constants.YV;
        } else if (keyCode == KeyEvent.VK_S) {
            yv = Constants.YV;
        } else if (keyCode == KeyEvent.VK_A) {
            xv = -Constants.XV;
        } else if (keyCode == KeyEvent.VK_D) {
            xv = Constants.XV;
        }
        log.info("this={};keyPressed;e={};keyCode={};xv={};yv={};", this, e, keyCode, xv, yv);
    }

    /**
     * @param rect 边界;
     */
    public void move(Rectangle rect) {
        x += xv;
        y += yv;
        //防止越界;
        int left = rect.x, right = left + rect.width, upper = rect.y, bottom = upper + rect.height;
        x = (x < left ? left : x);
        x = (x > right ? right : x);
        y = (y < upper ? upper : y);
        y = (y > bottom ? bottom : y);
    }


}
