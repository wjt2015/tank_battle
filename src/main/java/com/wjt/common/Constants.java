package com.wjt.common;

import com.wjt.model.Missile;

import java.util.LinkedHashSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Time 2020/3/29/18:00
 * @Author jintao.wang
 * @Description
 */
public interface Constants {
    /**
     * 最初默认的坦克位置(INIT_X,INIT_Y);
     */
    int INIT_X = 0;
    int INIT_Y = 0;
    /**
     * 坦克的默认速度大小;
     */
    int XV = 10;
    int YV = 10;

    /**
     * 坦克的默认大小;
     */
    int TANK_LENGTH = 50;
    int TANK_WIDTH = 50;

    /**
     * 炮弹的默认大小;
     */
    int MISSILE_LENGTH = 15;
    int MISSILE_WIDTH = 15;


    /**
     * 炮弹的默认速度大小;
     */
    int MISSILE_XV = 20;
    int MISSILE_YV = 20;

    /**
     * 炮弹容器;
     */
    LinkedHashSet<Missile> MISSILES = new LinkedHashSet<>(50);

    /**
     * 炮管长度;
     */
    int GUN_BARREL_LENGTH = 40;
    /**
     * 炮管宽度;
     */
    int GUN_BARREL_WIDTH=10;

    ScheduledThreadPoolExecutor THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(10);

}
