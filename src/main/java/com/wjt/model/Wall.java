package com.wjt.model;

import com.wjt.common.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Time 2020/4/2/1:19
 * @Author jintao.wang
 * @Description 墙是矩形, 固定不动, 坦克穿不透, 炮弹打不坏;
 */
public class Wall {
    /**
     * 中心坐标;
     */
    public final int x, y;

    /**
     * length--水平长度;width--竖直长度;
     */
    public final int length, width;

    public Color color = Constants.WALL_COLOR;
    /**
     * 墙的分数,也就是生命值;
     */
    public int score = Constants.WALL_INIT_SCORE;

    public final ConcurrentHashMap<Wall, Object> WALL_CONTAINER;

    /**
     * 墙体的位置,可用于坦克碰撞检测;
     */
    public final Rectangle rect;

    /**
     * 墙体的受炮弹影响的位置,可用于炮弹碰撞检测;
     */
    public final Rectangle bombRect;

    public Wall(int x, int y, int length, int width, ConcurrentHashMap<Wall, Object> walls) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.WALL_CONTAINER = walls;
        this.WALL_CONTAINER.put(this, Boolean.TRUE);

        this.rect = new Rectangle(x - (this.length >> 1), y - (this.width >> 1), this.length, this.width);

        int hitLength = this.length + Constants.MISSILE_XV;
        int hitWidth = this.width + Constants.MISSILE_YV;
        this.bombRect = new Rectangle(x - (hitLength >> 1), y - (hitWidth >> 1), hitLength, hitWidth);
    }


    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillRect(x - (length >> 1), y - (width >> 1), length, width);
        g2d.setColor(oldColor);
    }

    public void destroy() {
        this.score -= Constants.WALL_BOMB_HIT_SCORE;
        if (this.score <= 0) {
            this.WALL_CONTAINER.remove(this);
        }
    }

}
