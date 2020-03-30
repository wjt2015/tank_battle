package com.wjt.model;

import com.wjt.common.Constants;
import com.wjt.common.Direction;
import lombok.extern.slf4j.Slf4j;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * @Time 2020/3/30/15:52
 * @Author jintao.wang
 * @Description 炮管
 */
@Slf4j
public class GunBarrel {

    public volatile int xStart, yStart, xEnd, yEnd;
    public volatile int length;
    public volatile Color color;
    public Stroke stroke;

    public GunBarrel(int xStart, int yStart, Direction direction, int length, Color color) {
        this.length = length;
        this.color = color;
        this.stroke = new BasicStroke(Constants.GUN_BARREL_WIDTH >> 1);
        move(xStart, yStart, direction);
    }

    public void move(int xStart, int yStart, Direction direction) {
        this.xStart = xStart;
        this.yStart = yStart;
        int barrelLength = (int) (this.length / 1.41421356);
        switch (direction) {
            case LEFT:
                xEnd = xStart - length;
                yEnd = yStart;
                break;
            case LEFT_UPPER:
                xEnd = xStart - barrelLength;
                yEnd = yStart - barrelLength;
                break;
            case UPPER:
                xEnd = xStart;
                yEnd = yStart - length;
                break;
            case RIGHT_UPPER:
                xEnd = xStart + barrelLength;
                yEnd = yStart - barrelLength;
                break;
            case RIGHT:
                xEnd = xStart + length;
                yEnd = yStart;
                break;
            case RIGHT_BOTTOM:
                xEnd = xStart + barrelLength;
                yEnd = yStart + barrelLength;
                break;
            case BOTTOM:
                xEnd = xStart;
                yEnd = yStart + length;
                break;
            case LEFT_BOTTOM:
                xEnd = xStart - barrelLength;
                yEnd = yStart + barrelLength;
                break;

        }
    }


    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();

        g2d.setColor(color);
        g2d.setStroke(this.stroke);
        log.info("g2d={};this.stroke={};this.color={};", g2d, this.stroke, this.color);
        g2d.drawLine(xStart, yStart, xEnd, yEnd);
        g2d.setColor(oldColor);
        g2d.setStroke(oldStroke);
    }

}
