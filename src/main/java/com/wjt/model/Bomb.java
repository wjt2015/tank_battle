package com.wjt.model;

import com.wjt.common.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * @Time 2020/4/1/22:34
 * @Author jintao.wang
 * @Description 每一个炮弹都有一个爆炸的机会;
 */
public class Bomb {
    /**
     * 炮弹中心坐标;爆炸时中心位置不变;
     */
    public final int x, y;
    /**
     * 爆炸图形的帧编号,每次爆炸都是将爆炸图形依次逐个播放一次;
     */
    public volatile int frameNo = -1;

    public final Missile MISSILE;

    public Bomb(int x, int y, Missile missile) {
        this.x = x;
        this.y = y;
        this.MISSILE = missile;
        this.MISSILE.TANK.BOMB_CONTAINER.put(this, Boolean.TRUE);
    }

    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(Constants.BOMB_COLOR);
        int bombSize = Constants.BOMB_SIZE_ARR[this.frameNo];
        int bombRadius = bombSize >> 1;
        g2d.fillOval(x - bombRadius, y - bombRadius, bombSize, bombSize);
        g2d.setColor(oldColor);
    }

    /**
     * 推进到下一帧;
     */
    public void proceed() {
        this.frameNo++;
        if (this.frameNo >= Constants.BOMB_SIZE_ARR.length) {
            destroy();
        }
    }

    public void destroy() {
        this.MISSILE.TANK.BOMB_CONTAINER.remove(this);
    }



}
