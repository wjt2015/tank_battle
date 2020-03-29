package com.wjt.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Time 2020/3/29/18:00
 * @Author jintao.wang
 * @Description
 */
public interface Constants {

    ScheduledThreadPoolExecutor THREAD_POOL_EXECUTOR=new ScheduledThreadPoolExecutor(10);

}
