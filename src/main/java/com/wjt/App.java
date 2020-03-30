package com.wjt;

import com.wjt.common.Constants;
import com.wjt.model.TankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        tank1();
        log.info("Hello World!");
    }


    public static void tank1() {
        TankClient tankClient = new TankClient();
        //每50ms刷新一次;
        Constants.THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(tankClient, 10, 30, TimeUnit.MILLISECONDS);
        log.info("tankClient finish!tankClient={}!", tankClient);
    }


}
