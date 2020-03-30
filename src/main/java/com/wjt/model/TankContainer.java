package com.wjt.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Time 2020/3/30/20:02
 * @Author jintao.wang
 * @Description
 */
public class TankContainer {
    public ConcurrentHashMap<Tank, Object> playerTanks = new ConcurrentHashMap<>(3);
    public ConcurrentHashMap<Tank, Object> enemyTanks = new ConcurrentHashMap<>(50);

    @Override
    public String toString() {
        return "TankContainer{" +
                "playerTanks=" + playerTanks +
                ", enemyTanks=" + enemyTanks +
                '}';
    }
}
