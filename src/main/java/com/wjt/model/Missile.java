package com.wjt.model;

import com.wjt.common.Constants;
import com.wjt.common.Direction;
import com.wjt.common.PlayerType;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Time 2020/3/29/23:43
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Missile {

    public volatile int x, y;
    public volatile int xv, yv;
    public volatile int length, width;
    public volatile Color color;
    /**
     * 边界;
     */
    public volatile Rectangle rect;

    public Tank tank;

    public Missile(final int x, final int y, final Direction direction, Tank tank, Rectangle rect) {
        this.x = x;
        this.y = y;
        length = Constants.MISSILE_LENGTH;
        width = Constants.MISSILE_WIDTH;
        color = Color.BLACK;
        setDirection(direction);
        this.tank = tank;
        this.tank.MISSILES.put(this, Boolean.TRUE);
        this.rect = rect;
        //log.info("missilePos=({},{});this.missiles={};direction={};this.RECT={};", this.x, this.y, this.tank.MISSILES, direction, this.rect);
        log.info("tank.playerType={};missilePos=({},{});this.missiles.size={};direction={};this.RECT={};",
                tank.playerType, this.x, this.y, this.tank.MISSILES.size(), direction, this.rect);
        //addMissileDestroyListener(missileDestroyListener);
    }

    public Missile(final int x, final int y, final Direction direction, Tank tank, Rectangle rect, Color color) {
        this.x = x;
        this.y = y;
        length = Constants.MISSILE_LENGTH;
        width = Constants.MISSILE_WIDTH;
        this.color = color;
        setDirection(direction);
        this.tank = tank;
        this.tank.MISSILES.put(this, Boolean.TRUE);
        this.rect = rect;
        //log.info("missilePos=({},{});this.missiles={};direction={};this.RECT={};", this.x, this.y, this.tank.MISSILES, direction, this.rect);
        log.info("tank.playerType={};missilePos=({},{});this.missiles.size={};direction={};this.RECT={};",
                tank.playerType, this.x, this.y, this.tank.MISSILES.size(), direction, this.rect);
        //addMissileDestroyListener(missileDestroyListener);
    }

    public void setDirection(final Direction direction) {
        switch (direction) {
            case LEFT:
                xv = -Constants.MISSILE_XV;
                yv = 0;
                break;
            case LEFT_UPPER:
                xv = -Constants.MISSILE_XV;
                yv = -Constants.MISSILE_YV;
                break;
            case UPPER:
                xv = 0;
                yv = -Constants.MISSILE_YV;
                break;
            case RIGHT_UPPER:
                xv = Constants.MISSILE_XV;
                yv = -Constants.MISSILE_YV;
                break;
            case RIGHT:
                xv = Constants.MISSILE_XV;
                yv = 0;
                break;
            case RIGHT_BOTTOM:
                xv = Constants.MISSILE_XV;
                yv = Constants.MISSILE_YV;
                break;
            case BOTTOM:
                xv = 0;
                yv = Constants.MISSILE_YV;
                break;
            case LEFT_BOTTOM:
                xv = -Constants.MISSILE_XV;
                yv = Constants.MISSILE_YV;
                break;
        }
    }

    public void move() {
        x += xv;
        y += yv;
        int left = rect.x, right = left + rect.width, upper = rect.y, bottom = upper + rect.height;
        //越界后炮弹的生命周期就结束;
        if (x < left || x > right || y < upper || y > bottom) {
            log.info("missile_move;pos=({},{});rect=({},{}),({},{});", x, y, left, upper, right, bottom);
            destroy();
        }
    }

    public void destroy() {
        log.info("before;tank.playerType={};missilePos=({},{});this.missiles.size={};", tank.playerType, this.x, this.y, this.tank.MISSILES.size());
        this.tank.MISSILES.remove(this);
        log.info("after;tank.playerType={};missilePos=({},{});this.missiles.size={};", tank.playerType, this.x, this.y, this.tank.MISSILES.size());
    }

    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval(x, y, length, width);
        g2d.setColor(oldColor);
    }


    public boolean hitTank(Tank objTank) {
        if (objTank == null) {
            return false;
        }
        if (this.tank == objTank) {
            return false;
        } else {
            Rectangle missileRect = new Rectangle(x, y, Constants.MISSILE_LENGTH, Constants.MISSILE_WIDTH);
            Rectangle objRect = new Rectangle(objTank.x, objTank.y, objTank.length, objTank.width);
            boolean hit = (!missileRect.intersection(objRect).isEmpty());
            log.info("missileRect={};objRect={};hit={};", missileRect, objRect, hit);
            if (hit) {
                /**
                 * 击中坦克;
                 *  发出该炮弹的坦克加分;
                 *  被击中的坦克减分;
                 *  炮弹消失;
                 */
                if (this.tank.playerType != PlayerType.PLAYER_D) {
                    this.tank.addScore();
                }
                objTank.subScore();
                destroy();
                return true;
            } else {
                return false;
            }
        }
    }


}
