package com.wjt.common;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Time 2020/3/29/18:00
 * @Author jintao.wang
 * @Description
 */
public interface Constants {
    //最初的坦克位置(INIT_X,INIT_Y);
    int INIT_X = 0;
    int INIT_Y = 0;
    //坦克的速度大小;
    int XV = 10;
    int YV = 10;

    ScheduledThreadPoolExecutor THREAD_POOL_EXECUTOR=new ScheduledThreadPoolExecutor(10);

}
