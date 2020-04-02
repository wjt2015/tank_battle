package com.wjt.model;

import com.wjt.common.Constants;
import com.wjt.common.Direction;
import com.wjt.common.PlayerType;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * @Time 2020/3/29/23:43
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Missile {

    /**
     * 炮弹右上角坐标;
     */
    public volatile int x, y;
    public volatile int xv, yv;
    public volatile int length, width;
    public volatile Color color;
    /**
     * 边界;
     */
    public volatile Rectangle rect;

    /**
     * 该炮弹所属的坦克;
     */
    public final Tank TANK;

    public Missile(final int x, final int y, final Direction direction, Tank tank, Rectangle rect, Color color) {
        this.x = x;
        this.y = y;
        length = Constants.MISSILE_LENGTH;
        width = Constants.MISSILE_WIDTH;
        this.color = color;
        setDirection(direction);
        this.TANK = tank;
        this.TANK.MISSILES.put(this, Boolean.TRUE);
        this.rect = rect;


        //log.info("missilePos=({},{});this.missiles={};direction={};this.RECT={};", this.x, this.y, this.TANK.MISSILES, direction, this.rect);
        log.info("TANK.playerType={};missilePos=({},{});this.missiles.size={};direction={};this.RECT={};",
                tank.playerType, this.x, this.y, this.TANK.MISSILES.size(), direction, this.rect);
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
        log.info("before;TANK.playerType={};missilePos=({},{});this.missiles.size={};", TANK.playerType, this.x, this.y, this.TANK.MISSILES.size());
        this.TANK.MISSILES.remove(this);
        log.info("after;TANK.playerType={};missilePos=({},{});this.missiles.size={};", TANK.playerType, this.x, this.y, this.TANK.MISSILES.size());
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
        if (this.TANK == objTank) {
            return false;
        } else {
            Rectangle missileRect = couldBombRect();
            Rectangle objRect = new Rectangle(objTank.x, objTank.y, objTank.length, objTank.width);
            Rectangle intersectionRect = missileRect.intersection(objRect);
            if (intersectionRect != null) {
                /**
                 * 击中坦克;
                 * 爆炸;
                 *  发出该炮弹的坦克加分;
                 *  被击中的坦克减分;
                 *  炮弹消失;
                 *  爆炸中心位置在交互区域的中心;
                 */

                new Bomb(intersectionRect.x + (intersectionRect.width >> 1), intersectionRect.y + (intersectionRect.height >> 1), this);

                if (this.TANK.playerType != PlayerType.PLAYER_D) {
                    this.TANK.addScore();
                }
                objTank.subScore();
                destroy();
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 炮弹击中墙的检测和处理;
     *
     * @param wall
     * @return
     */
    public boolean hitWall(Wall wall) {
        Rectangle intersectionRect = couldBombRect().intersection(wall.rect);
        if (intersectionRect != null) {
            /**
             * 撞墙爆炸;墙受损;炮弹消失;爆炸中心位置在交互区域的中心;
             */
            new Bomb(intersectionRect.x + (intersectionRect.width >> 1), intersectionRect.y + (intersectionRect.height >> 1), this);
            wall.destroy();
            destroy();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 炮弹可爆炸的区域;
     *
     * @return
     */
    public Rectangle couldBombRect() {
        int newX = (this.xv < 0 ? (this.x + this.xv) : this.x);
        int newY = (this.yv < 0 ? (this.y + this.yv) : this.y);
        return new Rectangle(newX, newY, Constants.MISSILE_LENGTH + Math.abs(this.xv), Constants.MISSILE_WIDTH + Math.abs(this.yv));
    }


}
