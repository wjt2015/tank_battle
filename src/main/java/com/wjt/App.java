package com.wjt;

import com.wjt.common.Constants;
import com.wjt.model.Tank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Tank tank = new Tank();
        Constants.THREAD_POOL_EXECUTOR.submit(tank);
        log.info("tank finish!tank={}!", tank);
    }


}
