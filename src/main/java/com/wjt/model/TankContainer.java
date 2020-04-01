package com.wjt.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Time 2020/3/30/20:02
 * @Author jintao.wang
 * @Description
 */
public class TankContainer {
    public final ConcurrentHashMap<Tank, Object> playerTanks = new ConcurrentHashMap<>(3);
    public final ConcurrentHashMap<Tank, Object> enemyTanks = new ConcurrentHashMap<>(50);


    public void clear() {
        this.playerTanks.clear();
        this.enemyTanks.clear();
    }

    public boolean isEmpty() {
        return (this.playerTanks.isEmpty() && this.enemyTanks.isEmpty());
    }

    @Override
    public String toString() {
        return "TankContainer{" +
                "playerTanks=" + playerTanks +
                ", enemyTanks=" + enemyTanks +
                '}';
    }
}
