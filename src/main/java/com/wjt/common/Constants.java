package com.wjt.common;

import com.wjt.model.Missile;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Time 2020/3/29/18:00
 * @Author jintao.wang
 * @Description
 */
public interface Constants {

    /**
     * 坦克的默认速度大小;
     */
    int TANK_XV = 5;
    int TANK_YV = 5;

    /**
     * 最初默认的坦克位置(INIT_X,INIT_Y);
     */
    int INIT_X = TANK_XV;
    int INIT_Y = TANK_YV;

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
    int GUN_BARREL_WIDTH = 10;

    /**
     * 随机整数发生器;
     */
    Random RANDOM = new Random();

    ScheduledThreadPoolExecutor THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(10);

}
