package com.wjt.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @Time 2020/3/30/19:17
 * @Author jintao.wang
 * @Description 玩家类型;其中PLAYER_A,PLAYER_B,PLAYER_C为玩家预留;PLAYER_D为敌人预留;
 */
public enum PlayerType {
    PLAYER_A(1, "player_A"),
    PLAYER_B(1, "player_B"),
    PLAYER_C(1, "player_C"),
    PLAYER_D(1, "player_D"),;


    public final int code;
    public final String desc;


    PlayerType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, PlayerType> CODE_MAP_TYPE = new HashMap<>();

    public static PlayerType codeOf(int code) {
        return CODE_MAP_TYPE.get(code);
    }

}
