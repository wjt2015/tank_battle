package com.wjt.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @Time 2020/3/29/18:10
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class Utils {
    public static void sleep(long ts) {
        try {
            Thread.sleep(ts);
        } catch (InterruptedException e) {
            log.error("The thread has been interrupted while sleeping!ts={}ms;", ts, e);
        }
    }
}
