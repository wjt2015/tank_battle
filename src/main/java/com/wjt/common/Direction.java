package com.wjt.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @Time 2020/3/29/23:48
 * @Author jintao.wang
 * @Description
 */
public enum Direction {

    NONE(0, "静止"),
    LEFT(1, "左"),
    LEFT_UPPER(2, "左上"),
    UPPER(3, "上"),
    RIGHT_UPPER(4, "右上"),
    RIGHT(5, "右"),
    RIGHT_BOTTOM(6, "右下"),
    BOTTOM(7, "下"),
    LEFT_BOTTOM(8, "左下");

    private final int code;
    private final String desc;

    Direction(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, Direction> CODE_MAP_DIR = new HashMap<>();

    static {
        for (Direction direction : values()) {
            CODE_MAP_DIR.put(direction.code, direction);
        }
    }

    public static Direction codeOf(int code) {
        return CODE_MAP_DIR.get(code);
    }


    public Direction reverseDirection() {
        if (this.code >= 1 && this.code <= 4) {
            return codeOf(this.code + 4);
        } else if (this.code >= 5 && this.code <= 8) {
            return codeOf(this.code - 4);
        } else {
            return this;
        }
    }

}
