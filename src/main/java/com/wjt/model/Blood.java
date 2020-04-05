package com.wjt.model;

import com.wjt.common.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Time 2020/4/3/1:01
 * @Author jintao.wang
 * @Description 可以为玩家增加生命值的血块;
 */
public class Blood {

    /**
     * 血块的中心位置坐标;
     */
    public volatile int x, y;
    /**
     * 运动速度(xv,yv);
     */
    public volatile int xv, yv;

    public final Rectangle GAME_RECT;

    public final int length = Constants.BLOOD_LENGTH, width = Constants.BLOOD_WIDTH;

    public final Color color = Constants.BLOOD_COLOR;

    /**
     * 能够为玩家增加的生命值;
     */
    public final int score = Constants.BLOOD_SCORE;

    /**
     * 当前可用的血块;
     */
    public final ConcurrentHashMap<Blood, Object> BLOOD_CONTAINER;

    /**
     * 血块出现时的unix时间戳(ms);
     */
    public final long startTime;

    /**
     * 血块的生命时长(ms);
     */
    public final long lifeTime = Constants.BLOOD_LIFETIME;

    private volatile int frameId = -1;


    public Blood(int x, int y, ConcurrentHashMap<Blood, Object> bloods, Rectangle gameRect) {
        this.x = x;
        this.y = y;
        this.BLOOD_CONTAINER = bloods;
        this.startTime = System.currentTimeMillis();
        this.BLOOD_CONTAINER.put(this, Boolean.TRUE);
        this.GAME_RECT = gameRect;
    }

    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(this.color);
        g2d.fillRect(x - (length >> 1), y - (width >> 1), length, width);
        g2d.setColor(oldColor);
    }

    public void proceed() {
        this.frameId++;
        if (this.frameId >= Constants.BLOOD_V_RATIO.length) {
            this.frameId = 0;
        }
        final int idx = this.frameId;
        this.xv = Constants.BLOOD_V_RATIO[idx][0] * Constants.BLOOD_XV;
        this.yv = Constants.BLOOD_V_RATIO[idx][1] * Constants.BLOOD_YV;
    }

    public void move() {
        x += xv;
        y += yv;
        if (this.x < GAME_RECT.x) {
            this.x = GAME_RECT.x;
        } else if (this.x > (GAME_RECT.x + GAME_RECT.width)) {
            this.x = GAME_RECT.width;
        }
        if (this.y < GAME_RECT.y) {
            this.y = GAME_RECT.y;
        } else if (this.y > (GAME_RECT.y + GAME_RECT.height)) {
            this.y = GAME_RECT.y + GAME_RECT.height;
        }
    }

    /**
     * 检查生命期是否应该结束了;
     */
    public void checkLifeTime() {
        if ((System.currentTimeMillis() - this.startTime) > this.lifeTime) {
            destroy();
        }
    }

    public void destroy() {
        BLOOD_CONTAINER.remove(this);
    }


    public Rectangle getRect() {
        return new Rectangle(x - (length >> 1), y - (width >> 1), length, width);
    }


}
