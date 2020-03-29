package com.wjt.model;

import com.wjt.common.Constants;
import com.wjt.common.Direction;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Time 2020/3/29/22:46
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Tank {
    public volatile int x, y;
    public volatile int xv, yv;
    /**
     * 坦克大小;
     */
    public volatile int length, width;

    public volatile Color color;
    public volatile Direction direction;

    /**
     * 边界;
     */
    public volatile Rectangle rect;

    /**
     * 炮弹容器;
     */
    //public static final ConcurrentSkipListSet<Missile> MISSILES = new ConcurrentSkipListSet<>();
    public static final ConcurrentHashMap<Missile, Object> MISSILES = new ConcurrentHashMap<>(50);

    public Tank(Rectangle rect) {
        x = Constants.INIT_X;
        y = Constants.INIT_Y;
        xv = 0;
        yv = 0;
        length = Constants.TANK_LENGTH;
        width = Constants.TANK_WIDTH;
        color = Color.RED;
        direction = Direction.BOTTOM;
        this.rect = rect;
    }

    public Tank(int x, int y, int xv, int yv, Color color, Rectangle rect) {
        this.x = x;
        this.y = y;
        this.xv = xv;
        this.yv = yv;
        length = Constants.TANK_LENGTH;
        width = Constants.TANK_WIDTH;
        this.color = color;
        direction = Direction.BOTTOM;
        this.rect = rect;
    }

    public void draw(Graphics2D g2d) {
        //绘制坦克;
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval(x, y, length, width);
        g2d.setColor(oldColor);
        //绘制炮弹;
        for (Missile missile : MISSILES.keySet()) {
            missile.move();
            missile.draw(g2d);
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            if (yv >= 0) {
                yv -= Constants.YV;
            }
            this.direction = Direction.UPPER;
        } else if (keyCode == KeyEvent.VK_A) {
            if (xv >= 0) {
                xv -= Constants.XV;
            }
            this.direction = Direction.LEFT;
        } else if (keyCode == KeyEvent.VK_S) {
            if (yv <= 0) {
                yv += Constants.YV;
            }
            this.direction = Direction.BOTTOM;
        } else if (keyCode == KeyEvent.VK_D) {
            if (xv <= 0) {
                xv += Constants.XV;
            }
            this.direction = Direction.RIGHT;
        } else if (keyCode == KeyEvent.VK_J) {
            //发射炮弹;
            int missileX = (x + (this.length >> 1)), missileY = (y + (this.width >> 1));
            new Missile(missileX, missileY, this.direction, MISSILES, this.rect);
        }
        log.info("keyPressed;this={};e={};xv={};yv={};this.direction={};", this, e, xv, yv, this.direction);
    }

    public void keyReleased(KeyEvent e) {

    }


    public void move() {
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
